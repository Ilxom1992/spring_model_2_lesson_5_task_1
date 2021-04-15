package com.example.demo.security;

import com.example.demo.entity.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.Set;


@Component
public class JwtProvider {
    /**
     * TOKENGA VAQIT BERISH UCHUN ISHLATILADI
     */
    private final long expireTime=1000*60*60*24;
    /**
     * BU YERDA TOKEN UCHUN MAXFIY SO'Z YOZILGAN KALIT SO'ZI
     */
    private  static final String secretKey="Mahfiy soz";

    /**
     * TOKENNI YARATIB QAYTARADI
     * @param username
     * @param roles
     * @return
     */
    public String generateToken(String username, Set<Role> roles){
        Date expireDate = new Date(System.currentTimeMillis() + expireTime);
        String token = Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .claim("roles", roles)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
        return token;
    }

    /**
     * KELGAN TOKEENI EMAILGA AYLANTIRIB QAYTARADI TEKSHIRIB BERADI
     * @param token
     * @return
     */
    public  String getUserEmailFromToken(String token){
        try {
            String email = Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
                return email ;
        }catch (Exception e){
            return null;
        }
    }
    public String getRoleNameFromToken(String token) {

        Object role = Jwts
                .parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("role");
        return role.toString();
    }
}
