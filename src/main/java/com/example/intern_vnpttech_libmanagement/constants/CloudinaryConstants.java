package com.example.intern_vnpttech_libmanagement.constants;

import org.springframework.beans.factory.annotation.Value;

public class CloudinaryConstants {

    @Value("${cloudinary.cloud.apikey}")
    public  String apiKey;

    @Value("${cloudinary.cloud.name}")
    public  String cloudName;

    public static String CLOUD_NAME ;

    @Value("${cloudinary.cloud.apisecret}")
    public  String apiSecret;

    public static String API_SECRET;


}
