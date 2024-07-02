package med.voll.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import med.voll.api.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    //Dizendo para Spring ler este atributo
    @Value("${api.security.token.secret}")
    private String secret;

    private static final String ISSUER = "API Voll.med";

    public String gerarToken(Usuario usuario){

        try {

            Algorithm algoritmo = Algorithm.HMAC512(secret);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(tempoExpiracao())
                    .sign(algoritmo);

        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }

    }

    public String getSubject (String tokenJWT){

        try {

            Algorithm algoritmo = Algorithm.HMAC512(secret);

            return JWT.require(algoritmo)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(tokenJWT)
                    .getSubject();

        }catch (JWTVerificationException e){
            throw new RuntimeException("Token JWT inválido ou expirado: " + tokenJWT);
        }


    }

    //Token deve ter uma validade
    //Definindo tempo de expiracao do token
    public Instant tempoExpiracao(){

        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));

    }

}
