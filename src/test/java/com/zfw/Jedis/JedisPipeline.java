package com.zfw.Jedis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class JedisPipeline {
	private static final String IP="192.168.1.222";
	private static final int PORT=6379;
	private static Jedis jedis=null;
	@Before
	public void init() {
		jedis=JedisUtil.getInstance().getJedis(IP, PORT);
	}
	@After
	public void destroy() {
		JedisUtil.getInstance().closeJedis(jedis);
	}
	
	//不使用管道技术，插入10000条数据，耗时
	@Test
	public void noPipline(){
		jedis.flushDB();
		long before=System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			jedis.set("k"+i, "v"+i);
		}
		long after=System.currentTimeMillis();
		System.out.println("不使用管道技术，耗时："+(after-before)+"毫秒");
	}
	//使用pipline
	@Test
	public void Pipline(){
		jedis.flushDB();
		Pipeline pipelined = jedis.pipelined();
		long before=System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			pipelined.set("k"+i, "v"+i);
		}
		long after=System.currentTimeMillis();
		System.out.println("使用管道技术，耗时："+(after-before)+"毫秒");
	}
}
