package co.mil.imi.image_loader.resources;

import co.mil.imi.image_loader.views.FileView;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/client")
@Produces(MediaType.TEXT_HTML)
@Consumes(MediaType.APPLICATION_JSON)
public class ClientResource {

    @GET
    @Path("/uploadFile")
    public FileView uploadFile() {
        return new FileView();
    }
}
