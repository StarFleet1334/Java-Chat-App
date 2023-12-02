package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.ColorList;
import models.Colors;
import models.IndividualServer;
import models.ServerConfig;
import server_side.Server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Jackson {

    public static List<IndividualServer> individualServerList = new ArrayList<>();


    public Jackson() throws IOException {
        unmarshallingFromJsonToObject();
    }

    public void unmarshallingFromJsonToObject() throws IOException {
        File file = new File(
                Objects.requireNonNull(this.getClass().getClassLoader().getResource("servers.json")).getFile()
        );
        ObjectMapper mapper = new ObjectMapper();
        ServerConfig serverConfig = mapper.readValue(file,ServerConfig.class);
        individualServerList = serverConfig.individualServerList();
    }

    public static List<IndividualServer> getIndividualServerList() {
        return individualServerList;
    }

    public void marshallingFromObjectToJson(String port, String server) {
        IndividualServer individualServer = new IndividualServer();
        individualServer.setServer(port);
        individualServer.setServer(server);
        individualServerList.add(individualServer);

        // now here run the server
        Server.runSingleServer(port);
    }

}
