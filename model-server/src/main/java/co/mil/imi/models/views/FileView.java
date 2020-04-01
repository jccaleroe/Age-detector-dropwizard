package co.mil.imi.models.views;

import io.dropwizard.views.View;

public class FileView extends View {

    public FileView() {
        super("/views/file.mustache");
    }
}
