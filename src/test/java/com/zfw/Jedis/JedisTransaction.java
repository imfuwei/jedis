package com.zfw.Jedis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class JedisTransaction {
	private static final String IP="192.168.1.222";
	private static final int PORT=6379;
	private static Jedis jedis=null;
	@Before
	public void init() {
		jedis=JedisUtil.getInstance().getJedis(IP, PORT);
		//清空当前库
		jedis.flushDB();
	}
	@After
	public void destroy() {
		JedisUtil.getInstance().closeJedis(jedis);
	}
	@SuppressWarnings("unused")
	//正常，不使用事务
	@Test
	public void noTransaction() {
		try {
			jedis.set("k1", "v1");
			//模拟出错
			int i=100/0;
			jedis.set("k2", "v2");
		} catch (Exception e) {
			System.err.println("by/zero");
		}finally {
			System.out.println("异常之前被提交了，之后没有提交，查询所有key："+jedis.keys("*"));
		}
	}
	
	//使用事务
	@SuppressWarnings("unused")
	@Test
	public void Transaction() {
		try {
			Transaction transaction = jedis.multi();
			transaction.set("k1", "v1");
			int i=10/0;
			transaction.set("k2", "v2");
			transaction.exec();
		} catch (Exception e) {
			System.err.println("by/zero");
			System.out.println("发生异常，所有事务里操作都没被提交："+jedis.keys("*"));
		}
	}
	
	
	@Test
	public void wacth() {
		jedis.set("k1", "v1");
		//当key被watch时，这个key如果在事务之前被修改，事务是不会再认这个key的，会失败的
		jedis.watch("k1");
		jedis.set("k1", "v11");
		
		Transaction transaction = jedis.multi();
		transaction.set("k1", "v111");
		transaction.exec();
		
		jedis.unwatch();
		System.out.println(jedis.get("k1"));
	}
	
	
}
