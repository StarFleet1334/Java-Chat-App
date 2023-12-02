package models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Emoji {
    private String emoji;

    private String url;


    @JsonProperty("emoji")
    public String getEmoji() {
        return emoji;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Emoji{" +
                "emoji='" + emoji + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
