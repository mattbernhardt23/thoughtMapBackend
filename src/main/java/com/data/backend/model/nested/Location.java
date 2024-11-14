package com.data.backend.model.nested;

import lombok.Data;

@Data
public class Location {
    private Street street;
    private String city;
    private String state;
    private String country;
    private String postcode;
    private Coordinates coordinates;
    private Timezone timezone;
}

@Data
class Street {
    private String number;
    private String name;
}
