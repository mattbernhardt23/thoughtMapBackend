package com.data.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.data.backend.model.nested.Picture;
import com.data.backend.model.nested.Name;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private Name name;
    private Picture picture;
    private Boolean contributor;
    private Boolean admin;
    private Boolean moderator;
}
