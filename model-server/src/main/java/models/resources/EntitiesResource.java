package models.resources;

import models.api.Entity;
import models.core.ModelServer;
import models.views.EntitiesView;
import models.views.UploadView;
import models.pool.EntityServerConnectionPool;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

@Path("/client")
@Produces(MediaType.TEXT_HTML)
public class EntitiesResource {

    static HashMap<String, String> map = new HashMap<>(20000);
    static ArrayList<Entity> entities = new ArrayList<>(20000);

    static {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(new File("entities/entities.txt"))));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tmp = line.split("\t");
                entities.add(new Entity(tmp[0], tmp[1]));
                map.put(tmp[0], tmp[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Path("/entities")
    public UploadView uploadView() {
        return new UploadView(entities);
    }

    @POST
    @Path("/uploadEntities")
    public EntitiesView uploadClient(@FormParam("text") String text) {
        ModelServer server = EntityServerConnectionPool.getInstance().getClient();
        String s = server.sendTask(text.replaceAll("\n", " "));
        EntityServerConnectionPool.getInstance().returnClient(server);
        ArrayList<Entity> repeated = new ArrayList<>();
        ArrayList<Entity> notRepeated = new ArrayList<>();
        for (String line : s.split("\n")) {
            String[] tmp = line.split("\t");
            if (map.containsKey(tmp[0]))
                repeated.add(new Entity(tmp[0], tmp[1]));
            else
                notRepeated.add(new Entity(tmp[0], tmp[1]));
        }
        return new EntitiesView(repeated, notRepeated);
    }
}
