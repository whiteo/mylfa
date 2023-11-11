package de.whiteo.mylfa.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Leo Tanas (<a href="https://github.com/whiteo">github</a>)
 */

@Slf4j
@Component
public class JwtTokenUtil {

    private static final String TOKEN_PREFIX = "Bearer ";

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.expiration.time}")
    private long expirationTime;

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sessionId", UUID.randomUUID().toString());

        return Jwts.builder()
                .claims().empty().add(claims).and()
                .subject(userDetails.getUsername())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public String getUser(String token) {
        JwtParser jwtParser = getJwtParser();
        return jwtParser.parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            JwtParser jwtParser = getJwtParser();
            jwtParser.parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims string is empty");
        }
        return false;
    }

    public String getToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            return header.substring(TOKEN_PREFIX.length());
        }
        throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Invalid authorization type");
    }

    private JwtParser getJwtParser() {
        return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes())).build();
    }
}