package com.zfw.Jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

public class Sentinel {
	@Test
	public void testSentinel() {
		JedisSentinelPool jedisSentinelPool=null;
		Jedis jedis=null;
		try {
			Set<String> sentinels=new HashSet<>();
			sentinels.add("192.168.1.222:26380");
			sentinels.add("192.168.1.222:26381");
			sentinels.add("192.168.1.222:26382");
			String password="123456";
			jedisSentinelPool = new JedisSentinelPool("mymaster", sentinels,password);
			jedis = jedisSentinelPool.getResource();
			for (int i = 0; i < 1000; i++) {
				jedis.set("key"+i, "value"+i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (jedis!=null) {
				jedis.close();
			}
		}
		jedisSentinelPool.close();
		
		
	}
}
