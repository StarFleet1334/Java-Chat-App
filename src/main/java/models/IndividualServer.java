    package models;


    import com.fasterxml.jackson.annotation.JsonProperty;

    public class IndividualServer {
        private String port;
        private String server;

        private int numberOfUsersJoined;

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

        public int getNumberOfUsersJoined() {
            return numberOfUsersJoined;
        }

        // Increment the number of users joined by 1
        public void incrementNumberOfUsersJoined() {
            this.numberOfUsersJoined++;
        }

    }