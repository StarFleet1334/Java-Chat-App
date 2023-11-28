package models;


import com.fasterxml.jackson.annotation.JsonProperty;

public class IndividualServer {
    private String port;
    private String server;

    @JsonProperty("port")
    public String getPort() {
        return port;
    }

    @JsonProperty("port")
    public void setPort(String port) {
        this.port = port;
    }

    @JsonProperty("server")
    public String getServer() {
        return server;
    }

    @JsonProperty("server")
    public void setServer(String server) {
        this.server = server;
    }

    @Override
    public String toString() {
        return "Server{" +
                "port='" + port + '\'' +
                ", server='" + server + '\'' +
                '}';
    }
}