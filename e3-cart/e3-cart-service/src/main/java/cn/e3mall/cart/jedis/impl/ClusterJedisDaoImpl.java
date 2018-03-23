package cn.e3mall.cart.jedis.impl;


import cn.e3mall.cart.jedis.JedisDao;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.JedisCluster;

@Repository
public class ClusterJedisDaoImpl implements JedisDao {
	
	//注入集群对象
	@Autowired
	private JedisCluster jc;

	@Override
	public String set(String key, String value) {
		// TODO Auto-generated method stub
		String set = jc.set(key, value);
		return set;
	}

	@Override
	public String get(String key) {
		// TODO Auto-generated method stub
		String value = jc.get(key);
		return value;
	}

	@Override
	public Long hset(String key, String field, String value) {
		// TODO Auto-generated method stub
		Long hset = jc.hset(key, field, value);
		return hset;
	}

	@Override
	public String hget(String key, String field) {
		// TODO Auto-generated method stub
		String value = jc.hget(key, field);
		return value;
	}

	@Override
	public Long hdel(String key, String fields) {
		// TODO Auto-generated method stub
		Long hdel = jc.hdel(key, fields);
		return hdel;
	}

	@Override
	public Long expire(String key, int seconds) {
		// TODO Auto-generated method stub
		Long expire = jc.expire(key, seconds);
		return expire;
	}

	@Override
	public Long ttl(String key) {
		// TODO Auto-generated method stub
		Long ttl = jc.ttl(key);
		return ttl;
	}

	//判断hash数据类型中field是否存在
	public Boolean hexists(String key, String field) {
		// TODO Auto-generated method stub
		Boolean hexists = jc.hexists(key, field);
		return hexists;
	}

	//sorted-set有序集合添加方法
	public Long zadd(String key,Double score, String member) {
		// TODO Auto-generated method stub
		Long zadd = jc.zadd(key, score, member);
		return zadd;
	}

	//反向获取sorted-set集合数据,从大到小获取
	public Set<String> zrevrange(String key, long start, long end) {
		// TODO Auto-generated method stub
		Set<String> zrevrange = jc.zrevrange(key, start, end);
		return zrevrange;
	}

	//删除sorted-set集合成员
	public Long zrem(String key,String member) {
		// TODO Auto-generated method stubr
		Long zrem = jc.zrem(key, member);
		return zrem;
	}
}
