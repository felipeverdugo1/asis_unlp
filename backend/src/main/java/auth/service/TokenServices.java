package auth.service;


import dao.UsuarioDAO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import model.Rol;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequestScoped
public class TokenServices {
    final static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static Key getKey() {
        return key;
    }

    public String generateToken(Long id, Set<Rol> roles) {

        List<String> nombresRoles = roles.stream()
                .map(Rol::getNombre) // Asegurate que `Rol` tenga un método `getNombre()`
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("roles", nombresRoles) // Agrega roles como claim
                .signWith(key)
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

}