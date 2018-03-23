package cn.e3mall.content.Jedis;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Description:
 * @Author: Likh
 * @CreateDate: 2018/3/15 13:16
 */
public class SpringJedis {
    @Test
    public void testSpringJedis(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-jedis.xml");
        JedisPool pool = context.getBean(JedisPool.class);
        Jedis resource = pool.getResource();
        resource.set("name","spring整合jedis");
        String name = resource.get("name");
        System.out.println(name);

    }
}
