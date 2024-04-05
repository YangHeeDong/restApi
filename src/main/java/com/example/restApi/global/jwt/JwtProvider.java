package com.example.restApi.global.jwt;

import com.example.restApi.global.util.Util;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JwtProvider {

    private SecretKey cachedSecretKey;

    @Value("${custom.jwt.secretKey}")
    private String originSecretKey;

    private SecretKey _getSecretKey () {

        String keyBase64Encoded =  Base64.getEncoder().encodeToString(originSecretKey.getBytes());

        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }

    public SecretKey getSecretKey () {
        if(cachedSecretKey == null) cachedSecretKey = _getSecretKey();
        return cachedSecretKey;
    }

    public String genToken (Map<String, Object> claims, int seconds) {

        long now = new Date().getTime();

        Date accessTokenExpiresIn = new Date( now + 1000L * seconds);

        return Jwts.builder()
                .claim("body", Util.json.toStr(claims))
                .setExpiration(accessTokenExpiresIn)
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean verify(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e ) {
            return false;
        }

        return true;
    }

    public Map<String, Object> getClaims(String token){

        String body = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("body",String.class);

        return Util.toMap(body);
    }


}
