package models.views;

import models.api.Entity;
import io.dropwizard.views.View;

import java.util.ArrayList;

public class UploadView extends View {

    private final ArrayList<Entity> entities;

    public UploadView(ArrayList<Entity> entities) {
        super("/views/upload_entities.mustache");
        this.entities = entities;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }
}
