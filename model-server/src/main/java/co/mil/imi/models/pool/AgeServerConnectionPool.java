package co.mil.imi.models.pool;

import co.mil.imi.models.core.ModelServer;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.ArrayList;

public class AgeServerConnectionPool {
    private static AgeServerConnectionPool instance = null;
    private GenericObjectPool<ModelServer> pool;

    private AgeServerConnectionPool() {
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
        list.add("detect_from_file.py");
        pool = new GenericObjectPool<>(new PooledModelServerConnectionFactory("../detector/", list), poolConfig);
    }

    public static AgeServerConnectionPool getInstance() {
        if (instance == null) {
            synchronized (AgeServerConnectionPool.class) {
                if (instance == null) {
                    instance = new AgeServerConnectionPool();
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
