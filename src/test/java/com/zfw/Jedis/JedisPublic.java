package com.zfw.Jedis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;
/**
 * 消息发送者
 * @author zhang
 *
 */
public class JedisPublic {
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
	public void publicMsg() throws InterruptedException {
		for (int i = 0; i < 100; i++) {
			jedis.publish("fabuxiaoxi", "重复第"+i+"遍:乡亲们，鬼子进村了……");
			Thread.sleep(1000);
		}
	}
}
