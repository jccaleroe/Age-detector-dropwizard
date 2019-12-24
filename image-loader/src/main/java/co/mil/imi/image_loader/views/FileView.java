package co.mil.imi.image_loader.views;

import io.dropwizard.views.View;

public class FileView extends View {

    public FileView() {
        super("/views/file.mustache");
    }
}
