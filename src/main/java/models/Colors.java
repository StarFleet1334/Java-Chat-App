package models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Colors {
    private String name;
    private String color_code;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("color_code")
    public String getColor_code() {
        return color_code;
    }
}
