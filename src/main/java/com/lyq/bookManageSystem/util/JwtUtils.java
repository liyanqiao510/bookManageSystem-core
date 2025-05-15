package com.lyq.bookManageSystem.util;

import com.lyq.bookManageSystem.model.DTO.UserDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

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

    /**
     * 解析验证JWT令牌
     * @param token JWT字符串
     * @return 解析后的声明体
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey()) // 新版验证方式
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
