package co.mil.imi.models.views;

import io.dropwizard.views.View;


public class ImageView extends View {

    private final String fileName;

    public ImageView(String fileName) {
        super("/views/image.mustache");
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
