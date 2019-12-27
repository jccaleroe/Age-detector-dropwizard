package pool;

import co.mil.imi.image_loader.core.ModelServer;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.ArrayList;

public class EntityServerConnectionPool {
    private static EntityServerConnectionPool instance = null;
    private GenericObjectPool<ModelServer> pool;

    private EntityServerConnectionPool() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxWaitMillis(60000);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnCreate(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setBlockWhenExhausted(true);
        poolConfig.setMinEvictableIdleTimeMillis(120000);
        poolConfig.setTimeBetweenEvictionRunsMillis(60000);
        poolConfig.setMaxTotal(64);
        poolConfig.setMinIdle(4);
        poolConfig.setMaxIdle(8);
        ArrayList<String> list = new ArrayList<>(4);
        list.add("venv/bin/python");
        list.add("get_entities.py");
        pool = new GenericObjectPool<>(new PooledModelServerConnectionFactory("../empty_chair/", list), poolConfig);
    }

    public static EntityServerConnectionPool getInstance() {
        if (instance == null) {
            synchronized (EntityServerConnectionPool.class) {
                if (instance == null) {
                    instance = new EntityServerConnectionPool();
                }
            }
        }
        return instance;
    }

    public ModelServer getClient() {
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void returnClient(ModelServer modelServer) {
        pool.returnObject(modelServer);
    }
}
