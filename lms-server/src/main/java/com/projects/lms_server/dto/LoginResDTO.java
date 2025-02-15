package com.projects.lms_server.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResDTO {
    private String accessToken;
    private String refreshToken;
}
