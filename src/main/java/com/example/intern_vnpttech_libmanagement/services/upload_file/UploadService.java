package com.example.intern_vnpttech_libmanagement.services.upload_file;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public interface UploadService {

    Map upload(MultipartFile file, Map config);

    File convertMultipartToFile(MultipartFile file) throws IOException;

}
