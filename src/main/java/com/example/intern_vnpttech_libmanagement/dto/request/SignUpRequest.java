package com.example.intern_vnpttech_libmanagement.dto.request;

import com.example.intern_vnpttech_libmanagement.entities.Staff;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    private String staffPassword ;

    private Staff staffInfo;
}
