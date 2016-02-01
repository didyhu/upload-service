package info.didyapp.upload.service;


import info.didyapp.upload.domain.Upload;
import info.didyapp.upload.domain.UploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author Didy
 */
@Service
public class UploadService {

    @Autowired
    UploadRepository uploadRepository;

    String storage;

    @Autowired
    public UploadService(@Value(value = "${upload-service.storage}") String storage) {
        File file = new File(storage);
        if (!file.exists()) {
            file.mkdirs();
        }
        this.storage = file.getAbsolutePath();
    }


    public Upload createUpload(MultipartFile file, String domain) throws IOException {
        Upload upload = new Upload();
        upload.setContentLength(file.getSize());
        upload.setContentType(file.getContentType());
        upload.setFilename(file.getName());
        upload.setDomain(domain);
        upload.setDeleted(false);
        uploadRepository.save(upload);
        file.transferTo(new File(storage, upload.getId()));
        return upload;
    }

    public void deletedUpload(String id) {
        Upload upload = uploadRepository.findOne(id);
        upload.setDeleted(true);
        uploadRepository.save(upload);
    }

    public Upload getUpload(String id) throws IOException {
        Upload upload = uploadRepository.findOne(id);
        return upload;
    }

    public Page<Upload> getUploads(Pageable pageable) {
        return uploadRepository.findAll(pageable);
    }

    public File getFile(String id) {
        Upload upload = uploadRepository.findOne(id);
        File file = new File(storage, upload.getId());
        return file;
    }

}
