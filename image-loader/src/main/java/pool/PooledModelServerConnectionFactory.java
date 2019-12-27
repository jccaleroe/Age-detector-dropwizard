package pool;

import co.mil.imi.image_loader.core.ModelServer;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.ArrayList;

public class PooledModelServerConnectionFactory extends BasePooledObjectFactory<ModelServer> {

    private ArrayList<String> commands;
    private String directory;

    public PooledModelServerConnectionFactory(String directory, ArrayList<String> commands) {
        this.commands = commands;
        this.directory = directory;
    }

    @Override
    public ModelServer create() {
        return new ModelServer(directory, commands);
    }

    @Override
    public PooledObject<ModelServer> wrap(ModelServer modelServer) {
        return new DefaultPooledObject<>(modelServer);
    }

    @Override
    public boolean validateObject(PooledObject<ModelServer> p) {
        return p.getObject().validate();
    }
}
