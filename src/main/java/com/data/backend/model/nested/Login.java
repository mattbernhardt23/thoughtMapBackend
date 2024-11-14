package com.data.backend.model.nested;

import lombok.Data;

@Data
public class Login {
    private String uuid;
    private String username;
    private String password;
    private String hashedPassword;
    private String salt;
    private String md5;
    private String sha1;
    private String sha256;

    // Getters and Setters
}
