package com.example.intern_vnpttech_libmanagement.configurations.open_api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan(basePackages = "com.example.intern_vnpttech_libmanagement.configurations")
public class OpenAPIConfig {

    @Bean
    public OpenAPI appOpenAPI()
    {
        List<Server> serverList = new ArrayList<>();
        serverList.add(new Server().url("http://localhost:8082").description("Local Server"));
        OpenAPI openAPI = new OpenAPI().servers(serverList)
                .info(new Info().contact(new Contact().email("chiendao1808@gmail.com")
                                                        .name(": Chien Dao - Solution and Product Center VNPT Technology")));
        return openAPI;

    }
}
