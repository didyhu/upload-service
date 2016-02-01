package info.didyapp.upload.api;

import info.didyapp.upload.domain.Upload;
import info.didyapp.upload.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author Didy
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    UploadService uploadService;

    @Value("${upload-service.maxage}")
    Long maxAge;

    @ModelAttribute
    public void allowCrossAccess(HttpServletResponse res) {
        res.setHeader("Access-Control-Allow-Origin", "*");
    }

    @RequestMapping(method = RequestMethod.POST)
    public Upload post(MultipartFile file, String domain) throws IOException {
        return uploadService.createUpload(file, domain);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable String id) throws IOException {
        Upload upload = uploadService.getUpload(id);
        Assert.notNull(upload);
        File file = uploadService.getFile(id);
        FileSystemResource resource = new FileSystemResource(file);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", upload.getContentType());
        responseHeaders.setContentLength(upload.getContentLength());
        responseHeaders.setCacheControl("public,max-age=" + maxAge);
        responseHeaders.setETag("\"" + id + "\"");
        responseHeaders.setLastModified(upload.getCreatedTime().getTime());
        return new ResponseEntity(resource, responseHeaders, HttpStatus.OK);
    }
}
