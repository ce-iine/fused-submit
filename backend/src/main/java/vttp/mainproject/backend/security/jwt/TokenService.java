package vttp.mainproject.backend.security.jwt;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import vttp.mainproject.backend.security.model.ApplicantPrincipal;
import vttp.mainproject.backend.security.model.BusinessPrincipal;

@Service
public class TokenService {

    @Value("${jwt.key.secret}")
    private String secretKey;
    
    public String generateAppToken(ApplicantPrincipal auth) {
        Instant now = Instant.now();

        return JWT.create()
                .withIssuer("Fused")
                .withIssuedAt(now)
                .withSubject(String.valueOf(auth.getApplicant().getId()))
                .withExpiresAt(now.plus(1, ChronoUnit.HOURS))
                .withClaim("id", auth.getApplicant().getId())
                .withClaim("role", auth.getApplicant().getRole())
                .sign(Algorithm.HMAC256(secretKey));
    }

    public String generateBizToken(BusinessPrincipal auth) {
        Instant now = Instant.now();

        return JWT.create()
                .withIssuer("Fused")
                .withIssuedAt(now)
                .withSubject(String.valueOf(auth.getBusiness().getId()))
                .withExpiresAt(now.plus(1, ChronoUnit.HOURS))
                .withClaim("id", auth.getBusiness().getId())
                .withClaim("role", auth.getBusiness().getRole())
                .withClaim("premium", auth.getBusiness().getPremium())
                // .withClaim("scope", scope)
                .sign(Algorithm.HMAC256(secretKey));
    }
    
}
