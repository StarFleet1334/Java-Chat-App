package models;



import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ServerConfig(@JsonProperty("servers") List<IndividualServer> individualServerList) {
}