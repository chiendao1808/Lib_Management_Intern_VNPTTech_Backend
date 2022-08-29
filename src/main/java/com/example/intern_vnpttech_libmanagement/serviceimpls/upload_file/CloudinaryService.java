package com.example.intern_vnpttech_libmanagement.serviceimpls.upload_file;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.intern_vnpttech_libmanagement.services.upload_file.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class CloudinaryService implements UploadService {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Map upload(MultipartFile file, Map config) {
        try{
            log.trace("Run upload image from Cloudinary Service");
            File uploadFile = convertMultipartToFile(file);
            Map uploadResult = cloudinary.uploader().upload(uploadFile,config);
            boolean isDeleted = uploadFile.delete();
            if(isDeleted)
                log.info("The file is deleted after uploading");
            else log.info("Delete the file error");
            return uploadResult;
        } catch (Exception ex)
        {
            log.error("Upload image :" +file.getOriginalFilename() +" error" ,ex);
            return ObjectUtils.emptyMap();
        }
    }

    // custom for upload book's images
    public String upload(MultipartFile file,String resourceType)
    {
        try{
            Map uploadConfig = ObjectUtils.asMap();
            uploadConfig.put("resource_type","image");
            uploadConfig.put("filename_override",file.getOriginalFilename());
            Map uploadRes = upload(file,uploadConfig);
            if(uploadRes.isEmpty())
                throw  new RuntimeException("Upload result response is empty");
            String imageURL = uploadRes.get("url").toString(); // get("secure_url") -> https
            return imageURL;
        } catch (Exception ex)
        {
            log.error("Upload book's image error",ex);
            return null;
        }
    }


    @Override
    public File convertMultipartToFile(MultipartFile file) throws IOException {
        File covertedFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(covertedFile);
        fos.write(file.getBytes());
        fos.close();
        return covertedFile;
    }
}
