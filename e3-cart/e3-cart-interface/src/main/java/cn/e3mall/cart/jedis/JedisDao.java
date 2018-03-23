package cn.e3mall.cart.jedis;

import java.util.Set;

public interface JedisDao {
	
	//抽取jedis常用命令方法
	//string
	//hash	
	//String类型
	public String set(String key, String value);

	//String get
	public String get(String key);

	//hash类型
	public Long hset(String key, String field, String value);

	//hash get
	public String hget(String key, String field);

	//hash delete
	public Long hdel(String key, String fields);

	//设置数据过期
	public Long expire(String key, int seconds);

	//测试过期时间过程
	public Long ttl(String key);

	// 判断hash数据类型中field是否存在
	public Boolean hexists(String key, String field);

	// sorted-set有序集合添加方法
	public Long zadd(String key, Double score, String member);

	// 反向获取sorted-set集合数据,从大到小获取
	public Set<String> zrevrange(String key, long start, long end);

	// 删除sorted-set集合成员
	public Long zrem(String key, String member);

}
