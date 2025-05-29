package com.lyq.bookManageSystem;

import com.lyq.bookManageSystem.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RedisIntegrationTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate; // 使用自定义的 RedisTemplate



    @Test
    public void testRedis() {
        Set<String> keys = redisTemplate.keys("jwt:*");  // 查看以jwt:开头的键
        for (String key : keys) {
            System.out.println("token键");
            System.out.println(key);  // 打印每个键
        }

    }

    @Test
    public void testRedisConnection() {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set("testKey", "Spring Boot 2.5.6");
        Object value = ops.get("testKey");
        System.out.println("Retrieved value: " + value); // 打印值
        assertEquals("Spring Boot 2.5.6", value);
        redisTemplate.delete("testKey");
    }


    @Test
    public void testObjectSerialization() {
        User user = new User();
        user.setId(1L);
        user.setUserName("admin7397");

        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set("user:1", user);

        User cachedUser = (User) ops.get("user:1"); // 显式类型转换
        System.out.println("cachedUser测试结果:"+cachedUser.getUserName());
        assertNotNull(cachedUser);
        assertEquals("admin7397", cachedUser.getUserName());
    }
}
