package com.gjgs.gjgs.modules.utils.jwt;

import com.gjgs.gjgs.modules.exception.token.ExpiredTokenException;
import com.gjgs.gjgs.modules.exception.token.InvalidTokenException;
import com.gjgs.gjgs.modules.member.dto.login.TokenDto;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.enums.Authority;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtTokenUtil {

    public static final String BEARER_TYPE = "Bearer";
    public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 360;            // 360분 -> 6시간
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 14;  // 14일
    public static final long REFRESH_TOKEN_REISSUE_TIME = 1000L * 60 * 60 * 24 * 7; // 7일

    private final Key key;

    public JwtTokenUtil(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    public TokenDto generateTokenDto(Member member) {
        Date currentDate = new Date();
        long currentDateMillis = currentDate.getTime();
        Date accessTokenExpiresIn = new Date(currentDateMillis + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = generateToken(member.getUsername(), currentDate, accessTokenExpiresIn);
        Date refreshTokenExpiresIn = new Date(currentDateMillis + REFRESH_TOKEN_EXPIRE_TIME);
        String refreshToken = generateToken(member.getUsername(), currentDate, refreshTokenExpiresIn);
        return TokenDto.of(BEARER_TYPE, accessToken, refreshToken,
                getRemainingMilliSeconds(accessToken), getRemainingMilliSeconds(refreshToken));
    }

    public TokenDto generateTokenDto(Authentication authentication) {
        Date date = new Date();
        long now = date.getTime();

        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = generateToken(authentication.getName(), date, accessTokenExpiresIn);

        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
        String refreshToken = generateToken(authentication.getName(), date, refreshTokenExpiresIn);

        return TokenDto.of(BEARER_TYPE, accessToken, refreshToken,
                getRemainingMilliSeconds(accessToken), getRemainingMilliSeconds(refreshToken));
    }


    public String generateAccessToken(Authentication authentication) {
        Date date = new Date();
        long now = date.getTime();
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        return generateToken(authentication.getName(), date, accessTokenExpiresIn);
    }


    public Authentication getAuthentication(String accessToken, Authority authority) {
        Claims claims = getClaims(accessToken);
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = List.of(new SimpleGrantedAuthority(authority.name()));
        UserDetails principal = new User(claims.getSubject(), "", simpleGrantedAuthorities);
        return new UsernamePasswordAuthenticationToken(principal, "", simpleGrantedAuthorities);
    }


    private Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }


    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }


    public long getRemainingMilliSeconds(String token) {
        Date expiration = getClaims(token).getExpiration();
        return expiration.getTime() - (new Date()).getTime();
    }

    public boolean validateToken(String token) {
        try {
            // 토큰파싱해서 문제 없으면 true
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            throw new ExpiredTokenException();
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }


    /**
     * 3일짜리 refresh 토큰 만들기 -> test 용도로 필요함
     */
    public String generateRefreshTokenRemain3Day(Member member) {
        Date date = new Date();
        long now = date.getTime();
        Date refreshTokenExpiresIn = new Date(now + 1000L * 60 * 60 * 24 * 3);
        return generateToken(member.getUsername(), date, refreshTokenExpiresIn);
    }


    private String generateToken(String username, Date issuedAt, Date expiration) {
        return Jwts.builder()
                .setSubject(username) // kakaoId
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}
