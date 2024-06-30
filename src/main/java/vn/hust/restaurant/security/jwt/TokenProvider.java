package vn.hust.restaurant.security.jwt;

import io.jsonwebtoken.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import vn.hust.restaurant.service.dto.authorities.AuthenticationDTO;
import vn.hust.restaurant.web.rest.vm.LoginVM;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Component
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    private static final String INVALID_JWT_TOKEN = "Invalid JWT token.";

    private final String jwtSecret;

    private final int jwtExpirationInMs;

    private final JwtParser jwtParser;


    public TokenProvider(@Value("${app.jwtExpirationInMs}") int jwtExpirationInMs,
                         @Value("${app.jwtSecret}") String jwtSecret) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationInMs =jwtExpirationInMs;
        jwtParser = Jwts.parser()
            .verifyWith(getSigningKey())
            .build();
    }

    public String createToken(AuthenticationDTO authentication, LoginVM loginVM) {
        String authorities = authentication
            .getAuthentication()
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
        long now = (new Date()).getTime();
        Date validity;
       {
            validity = new Date(now + this.jwtExpirationInMs);
        }
        return Jwts
            .builder()
            .setSubject(authentication.getAuthentication().getName())
            .claim(AUTHORITIES_KEY, authorities)
            .claim("id", authentication.getId())
            .claim("companyId", authentication.getCompanyId())
            .claim("companyName", authentication.getCompanyName())
            .signWith(getSigningKey())
            .setExpiration(validity)
            .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        Collection<? extends GrantedAuthority> authorities = Arrays
            .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .filter(auth -> !auth.trim().isEmpty())
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String authToken) {
        try {
            jwtParser.parseClaimsJws(authToken);

            return true;
        } catch (SignatureException ex) {
            // Invalid JWT signature
            System.out.println("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            // Invalid JWT token
            System.out.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            // Expired JWT token
            System.out.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            // Unsupported JWT token
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            // JWT claims string is empty
            System.out.println("JWT claims string is empty");
        } catch (JwtException ex) {
            log.error(ex.getMessage(), (Object) ex.getStackTrace());
        }
        return false;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = this.jwtSecret.getBytes(StandardCharsets.UTF_8);
        ;
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }
}
