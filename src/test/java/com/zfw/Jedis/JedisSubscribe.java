package com.zfw.Jedis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
/**
 * 消息订阅者
 * @author zhang
 *
 */
public class JedisSubscribe {
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
	
	@Test
	public void publicMsg() {
		JedisPubSub jedisPubSub=new JedisPubSub() {
			@Override
			public void onMessage(String channel, String message) {
				System.out.println("接到"+channel+"发布的消息："+message);
			}
		};
		jedis.subscribe(jedisPubSub, "fabuxiaoxi");
	}
}
