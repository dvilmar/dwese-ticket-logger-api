package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Autowired
    private KeyPair jwtKeyPair;

    private static final long JWT_EXPIRATION = 3600000; // 1 hora

    /**
     * Extrae el nombre de usuario (claim "sub") del token.
     * El nombre de usuario suele ser el identificador del usuario autenticado.
     *
     * @param token el token JWT del cual se extraerá el claim.
     * @return el nombre de usuario contenido en el token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Método genérico para extraer cualquier claim del token JWT.
     *
     * @param token el token JWT.
     * @param claimsResolver función para resolver el claim deseado.
     * @return el valor del claim extraído.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrae todos los claims (payload) del token JWT.
     *
     * Utiliza el parser de JJWT configurado con la clave pública del par de claves.
     * Este método valida la integridad y autenticidad del token antes de extraer los claims.
     *
     * @param token el token JWT.
     * @return los claims contenidos en el token.
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtKeyPair.getPublic()) // Configura la clave para verificar la firma
                .build()
                .parseSignedClaims(token) // Verifica el token y lo parsea
                .getPayload(); // Devuelve el cuerpo del JWT (claims)
    }

    /**
     * Genera un token JWT para un usuario con roles específicos.
     *
     * Incluye los roles en el token como parte de los claims, configura
     * una fecha de emisión y una duración de 1 hora.
     *
     * @param username el nombre del usuario para el cual se genera el token.
     * @param roles la lista de roles del usuario (por ejemplo, ["USER", "ADMIN"]).
     * @return el token JWT generado.
     */
    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .subject(username) // Configura el claim "sub" (nombre de usuario)
                .claim("roles", roles) // Incluye los roles como claim adicional
                .issuedAt(new Date()) // Fecha de emisión del token
                .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION)) // Expira en 1 hora
                .signWith(jwtKeyPair.getPrivate(), Jwts.SIG.RS256) // Firma el token con la clave secreta
                .compact(); // Genera el token en formato JWT
    }

    /**
     * Valida un token JWT verificando:
     * 1. Que el nombre de usuario extraído del token coincida con el esperado.
     * 2. Que el token no haya expirado.
     *
     * @param token el token JWT.
     * @param username el nombre de usuario esperado.
     * @return true si el token es válido, false en caso contrario.
     */
    public boolean validateToken(String token, String username) {
        Claims claims = Jwts.parser()
                .verifyWith(jwtKeyPair.getPublic())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return username.equals(claims.getSubject()) && !isTokenExpired(claims);
    }

    /**
     * Verifica si un token JWT ha expirado.
     *
     * Extrae el claim "exp" (fecha de expiración) y lo compara con la fecha actual.
     *
     * @param claims los claims del token JWT.
     * @return true si el token ha expirado, false si aún es válido.
     */
    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}