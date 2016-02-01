package info.didyapp.upload.web;

import info.didyapp.upload.domain.Upload;
import info.didyapp.upload.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Didy on 2016-02-01.
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UploadService uploadService;

    @RequestMapping("/getUploads")
    public Page<Upload> getUploads(int page) {
        PageRequest pageRequest = new PageRequest(page, 10, Sort.Direction.DESC, "createdTime");
        return uploadService.getUploads(pageRequest);
    }
}
