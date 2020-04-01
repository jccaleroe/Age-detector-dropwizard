package co.mil.imi.models.resources;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/upload")
@Produces(MediaType.TEXT_HTML)
public class UploadResource {

    @POST
    @Path("/uploadEntities")
    public void uploadFile(@FormParam("file") String text) {

    }
}
