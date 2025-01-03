package com.data.backend.model.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.data.backend.model.nested.Picture;
import com.data.backend.model.nested.Name;

@Data
@Document(collection = "users")
public class Creator {
    @Id
    private String id;
    private Name name;
    private Picture picture;
}
