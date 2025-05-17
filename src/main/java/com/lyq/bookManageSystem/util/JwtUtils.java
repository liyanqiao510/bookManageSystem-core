package com.lyq.bookManageSystem.util;

import com.lyq.bookManageSystem.model.DTO.UserDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${jwt.secret}")
    private String secretStr; // 从配置文件读取的密钥字符串

    @Value("${jwt.expiration}")
    private long expiration; // 过期时间（单位：秒）

    // 生成符合HS256算法的密钥
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretStr.getBytes());
    }

    /**
     * 生成JWT令牌
     * @param userDTO 用户信息传输对象
     * @return 签发的JWT字符串
     */
    public String generateToken(UserDTO userDTO) {
        SecureDigestAlgorithm<SecretKey, SecretKey> algorithm = Jwts.SIG.HS256;
        Map<String, Object> claims = new HashMap<>();

        // 设置标准声明
        claims.put("sub", userDTO.getUserName());
        claims.put("userId", userDTO.getId());
        claims.put("role", userDTO.getRole());

        return Jwts.builder()
                .claims(claims) // 设置自定义声明
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(getSecretKey(), algorithm) // 新版签名方式
                .compact();
    }

    //   验证令牌有效性
    public boolean validateToken(String token) {

        try {
            if (redisTemplate.hasKey("jwt:blacklist:" + token)) {
                return false;
            }
            Claims claims = parseToken(token);  // 调用现有解析方法


            return !isTokenExpired(claims.getExpiration());  // 附加过期检查
        } catch (JwtException | IllegalArgumentException e) {
            // 捕获所有JWT相关异常（签名无效、格式错误等）
//            log.error("JWT验证失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 解析验证JWT令牌

     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey()) // 新版验证方式
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 辅助方法：检查令牌是否过期
    private boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }

//黑名单
    public void invalidateToken(String token) {
        Claims claims = parseToken(token);
        Date expiration = claims.getExpiration();
        long ttl = expiration.getTime() - System.currentTimeMillis();

        // 将未过期的token加入Redis黑名单
        if(ttl > 0) {
            redisTemplate.opsForValue().set(
                    "jwt:blacklist:" + token,
                    "invalid",
                    ttl,
                    TimeUnit.MILLISECONDS
            );
        }
    }


}
