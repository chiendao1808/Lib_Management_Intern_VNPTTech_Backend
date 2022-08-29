package com.example.intern_vnpttech_libmanagement.configurations.cloudinary;

import com.cloudinary.Cloudinary;
import com.example.intern_vnpttech_libmanagement.constants.CloudinaryConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.cloud.apikey}")
    private String apiKey;

    @Value("${cloudinary.cloud.name}")
    private String cloudName;

    @Value("${cloudinary.cloud.apisecret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary(){
        Map<String,String> config = new HashMap<>();
        config.put("cloud_name",cloudName);
        config.put("api_key",apiKey);
        config.put("api_secret",apiSecret);
        Cloudinary cloudinary = new Cloudinary(config);
        return cloudinary;
    }

}
