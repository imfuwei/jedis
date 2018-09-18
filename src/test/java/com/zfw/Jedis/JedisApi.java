package com.zfw.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;

public class JedisApi {
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
	
	
	@Test
	public void testString() {
		jedis.flushDB();
		jedis.set("test", "test");
		jedis.mset("key1","value1","key2","value2");
		List<String> mget = jedis.mget("key1","key2");
		System.out.println(mget);
	}
	
	@Test
	public void testList() {
		jedis.flushDB();
		jedis.lpush("lists", "list1","list2");
		jedis.lpush("lists", "list3");
		//-1倒数第一个，-2倒数第二个
		System.out.println(jedis.lrange("list", 0, -1));
	}

	@Test
	public void testSet() {
		jedis.flushDB();
		jedis.sadd("sets1", "set1","set2");
		jedis.sadd("sets2", "set1","set3");
		System.out.println(jedis.smembers("sets"));
		System.out.println("交集"+jedis.sinter("sets1","sets2"));
		System.out.println("并集"+jedis.sunion("sets1","sets2"));
		System.out.println("差集"+jedis.sdiff("sets1","sets2"));
		System.out.println("差集"+jedis.sdiff("sets2","sets1"));
	}
	
	//排序set集合
	@Test
	public void testSortSet() {
		jedis.flushDB();
		Map<String,Double> scoreMembers=new HashMap<>();
		scoreMembers.put("张三", 100d);
		scoreMembers.put("李四", 90d);
		scoreMembers.put("王二", 95d);
		scoreMembers.put("麻子", 95d);
		jedis.zadd("zset", scoreMembers);
		System.out.println("输出数据："+jedis.zrange("zset", 0, -1));
		System.out.println("王二分数："+jedis.zscore("zset", "王二"));
		System.out.println("李四以score降序排列："+jedis.zrevrank("zset", "李四"));
		System.out.println("李四子以score升序排列："+jedis.zrank("zset", "李四"));
	}
	
	@Test
	public void testHash() {
		jedis.flushDB();
		Map<String,String> hash=new HashMap<>();
		hash.put("k1", "v1");
		hash.put("k2", "v2");
		hash.put("k3", "v3");
		hash.put("k4", "v4");
		jedis.hmset("hash", hash);
		System.out.println(jedis.hgetAll("hash"));
		System.out.println(jedis.hget("hash","k1"));
		System.out.println(jedis.hmget("hash", "k1","k2"));
		System.out.println("所有keys"+jedis.hkeys("hash"));
		System.out.println("所有values"+jedis.hvals("hash"));
	}
	
	
	
}
