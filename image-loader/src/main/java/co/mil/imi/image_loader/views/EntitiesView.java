package co.mil.imi.image_loader.views;

import co.mil.imi.image_loader.api.Entity;
import io.dropwizard.views.View;

import java.util.ArrayList;

public class EntitiesView extends View {

    private final ArrayList<Entity> repeated;
    private final ArrayList<Entity> notRepeated;

    public EntitiesView(ArrayList<Entity> repeated, ArrayList<Entity> notRepeated) {
        super("/views/entities.mustache");
        this.repeated = repeated;
        this.notRepeated = notRepeated;
    }

    public ArrayList<Entity> getRepeated() {
        return repeated;
    }

    public ArrayList<Entity> getNotRepeated() {
        return notRepeated;
    }
}
