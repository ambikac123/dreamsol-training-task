package com.dreamsol.securities;

import com.dreamsol.entities.Permission;
import com.dreamsol.entities.Role;
import com.dreamsol.entities.User;
import com.dreamsol.services.impl.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtHelper
{
    @Value("${jwt.validity.access-token}")
    private long JWT_TOKEN_VALIDITY;
    private static final String SECRET_KEY = "fhsdgfhgdhfggfsgdfghdgfhdsgfhgsdhfgshdgfsgfshfhskjjgkhlkhhskhhjdnvjdjghdghdjbdhadhjhhgeueyueyuienvxnvbjfbfh";
    private static final Key key = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS512.getJcaName());

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl)userDetails;
        User user = userDetailsImpl.getUser();
        String ipAddress = userDetailsImpl.getIpAddress();
        claims.put("Id",user.getUserId());
        claims.put("Name",user.getUserName());
        claims.put("Email",user.getUserEmail());
        claims.put("Mobile No.",user.getUserMobile());
        claims.put("Roles", List.of(user.getRoles().stream().map(Role::getRoleType).toArray()));
        claims.put("Permissions",List.of(user.getPermissions().stream().map(Permission::getPermissionType).toArray()));
        claims.put("IP",ipAddress);
        String subject = userDetails.getUsername();
        return doGenerateToken(claims, subject);
    }
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY))
                .signWith(key,SignatureAlgorithm.HS512)
                .compact();
    }
    private Boolean isTokenExpired(String token)
    {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
    }
    public Boolean validateToken(String token, UserDetails userDetails) {
            final String usernameFromToken = getUsernameFromToken(token);
            final String usernameFromUserDetails = userDetails.getUsername();
            return (usernameFromToken.equals(usernameFromUserDetails) && !isTokenExpired(token));
    }
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    public String getUsernameFromToken(String token)
    {
       return getClaimFromToken(token, Claims::getSubject);
    }
    public Date getExpirationDateFromToken(String token)
    {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    private Claims getAllClaimsFromToken(String token)
    {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
