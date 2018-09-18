package com.zfw.Jedis;

import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtil {
	private JedisUtil() {}
	private static class RedisUtilHolder{
		private static final JedisUtil instance=new JedisUtil();
	}
	public static JedisUtil getInstance() {
		return RedisUtilHolder.instance;
	}
	
	private static Map<String, JedisPool> maps=new HashMap<>();
	
	private static JedisPool getPool(String ip,int port) {
		String key=ip+":"+port;
		JedisPool jedisPool=null;
		if (!maps.containsKey(key)) {
			JedisPoolConfig poolConfig=new JedisPoolConfig();
			jedisPool=new JedisPool(poolConfig, ip, port, 3000, "123456");
			maps.put(key, jedisPool);
		}else {
			jedisPool=maps.get(key);
		}
		return jedisPool;
	}
	public Jedis getJedis(String ip,int port) {
		return getPool(ip, port).getResource();
	}
	public void closeJedis(Jedis jedis) {
		if (null!=jedis) {
			jedis.close();
		}
	}
	
}
