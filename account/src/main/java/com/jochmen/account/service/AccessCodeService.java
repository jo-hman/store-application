package com.jochmen.account.service;

import com.jochmen.account.controller.schema.response.AccessCode;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
public class AccessCodeService {

    private static final String SECRET = "123981723987129837189273981273981723981723987123";
    private static final int EXPIRATION_MILLIS = 60 * 60 * 1000;

    public AccessCode createAccessCode(UUID accountId) {
        String accessCode = Jwts.builder()
                .setSubject(accountId.toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MILLIS))
                .signWith(getKey(), SignatureAlgorithm.HS256).compact();
        return new AccessCode(accessCode);
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Optional<UUID> verifyAccessCode(AccessCode accessCode) {
        try {
            var claims = Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(accessCode.jwt());
            var accountId = UUID.fromString(claims.getBody().getSubject());
            return Optional.of(accountId);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
