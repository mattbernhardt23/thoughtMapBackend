package com.data.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.data.backend.model.nested.Location;
import com.data.backend.model.nested.Name;
import com.data.backend.model.nested.Login;
import com.data.backend.model.nested.Dob;
import com.data.backend.model.nested.Registered;
import com.data.backend.model.nested.GovId;
import com.data.backend.model.nested.Picture;
import java.util.List;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String gender;
    private Name name;
    private Location location;
    private String email;
    private Login login;
    private Dob dob;
    private Registered registered;
    private String phone;
    private String cell;
    private GovId govId;
    private Picture picture;
    private String nat;
    private boolean admin = false;
    private boolean contributor = false;
    private boolean moderator = false;
    private String bio;
    private List<String> topicVotes; // References to Topic IDs
    private List<String> argumentVotes; // References to Argument IDs
}
