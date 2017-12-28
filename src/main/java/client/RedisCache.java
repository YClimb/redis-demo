package client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.Serializable;

/**
 * redis 缓存配置
 * @author yclimb
 */
public class RedisCache implements Serializable {

    /**
     * 日志记录
     */
    private static final Log LOG = LogFactory.getLog(RedisCache.class);

    /**
     * redis 连接池
     */
    private JedisPool pool;
    public void setPool(JedisPool pool) {
        this.pool = pool;
    }

    /*static {
        if (pool == null) {
            //读取相关的配置
            ResourceBundle resourceBundle = ResourceBundle.getBundle("redis");
            int maxActive = Integer.parseInt(resourceBundle.getString("redis.maxActive"));
            int maxIdle = Integer.parseInt(resourceBundle.getString("redis.maxIdle"));
            int maxWait = Integer.parseInt(resourceBundle.getString("redis.maxWait"));

            String host = resourceBundle.getString("redis.host");
            int port = Integer.parseInt(resourceBundle.getString("redis.port"));
            String pass = resourceBundle.getString("redis.pass");

            JedisPoolConfig config = new JedisPoolConfig();
            //设置最大连接数
            config.setMaxTotal(maxActive);
            //设置最大空闲数
            config.setMaxIdle(maxIdle);
            //设置超时时间
            config.setMaxWaitMillis(maxWait);

            //初始化连接池
            pool = new JedisPool(config, host, port, 2000, pass);
        }
    }*/

    /**
     * 获取jedis
     *
     * @return jedis
     */
    public Jedis getResource() {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
        } catch (Exception e) {
            LOG.info("can't get the redis resource");
        }
        return jedis;
    }

    /**
     * 关闭连接
     *
     * @param jedis j
     */
    public void disconnect(Jedis jedis) {
        jedis.disconnect();
    }

    /**
     * 将jedis 返还连接池
     *
     * @param jedis j
     */
    public void returnResource(Jedis jedis) {
        if (null != jedis) {
            try {
                pool.returnResource(jedis);
            } catch (Exception e) {
                LOG.info("can't return jedis to jedisPool");
            }
        }
    }

    /**
     * 无法返还jedispool，释放jedis客户端对象
     *
     * @param jedis j
     */
    public void brokenResource(Jedis jedis) {
        if (jedis != null) {
            try {
                pool.returnBrokenResource(jedis);
            } catch (Exception e) {
                LOG.info("can't release jedis Object");
            }
        }
    }
}

