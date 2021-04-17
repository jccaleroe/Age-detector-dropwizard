package models.resources;

import models.core.ModelServer;
import models.views.ImageView;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import models.pool.AgeServerConnectionPool;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.*;

@Path("/v1/files")
@Produces(MediaType.TEXT_HTML)
public class FileResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileResource.class);

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public ImageView uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException {
        // TODO: uploadFileLocation should come from config.yml
        String uploadedFileLocation = "src/main/resources/assets/" + fileDetail.getFileName();
        LOGGER.info(uploadedFileLocation);
        writeToFile(uploadedInputStream, uploadedFileLocation);
        ModelServer server = AgeServerConnectionPool.getInstance().getClient();
        server.sendTask(fileDetail.getFileName());
        AgeServerConnectionPool.getInstance().returnClient(server);
        return new ImageView("1" + fileDetail.getFileName());
    }

    // save uploaded file to new location
    private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) throws IOException {
        int read;
        final int BUFFER_LENGTH = 1024;
        final byte[] buffer = new byte[BUFFER_LENGTH];
        OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
        while ((read = uploadedInputStream.read(buffer)) != -1)
            out.write(buffer, 0, read);
        out.flush();
        out.close();
    }
}
