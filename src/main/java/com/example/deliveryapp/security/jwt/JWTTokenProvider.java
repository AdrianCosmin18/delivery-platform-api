package com.example.deliveryapp.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.deliveryapp.security.utils.Util.*;
import static java.util.Arrays.stream;

@Component
public class JWTTokenProvider {

    @Value("application.jwt.secretKey")
    private String secret;

    //mycode = numele celui care trimite
    //administration = info ~ numele proiectului
    //authorities: in token punem autorizatiile pe care le are userul
    //criptam cu secretul prin alg HMAC informatiile
    public String generateJwtToken(UserDetails userDetails){
        String[] claims = getClaimsFromUser(userDetails);
        return JWT.create().withIssuer(MY_CODE).withAudience(ADMINISTRATION)
                .withIssuedAt(new Date()).withSubject(userDetails.getUsername())
                .withArrayClaim(AUTHORITIES, claims).withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }

    //luam autoritatile/permisiunile pe care le are userul
    public List<GrantedAuthority> getAuthorities(String token){
        String[] claims = getClaimsFromToken(token);
        return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    //autentificarea -> folosim pentru a vedea cine este autentificat in acest moment
    public Authentication getAuthentication(String username, List<GrantedAuthority> authorities, HttpServletRequest request){
        UsernamePasswordAuthenticationToken userPasswordAuthToken =
                new UsernamePasswordAuthenticationToken(username, null, authorities);
        userPasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return userPasswordAuthToken;
    }

    //verif sa nu trimietm un username gol si verificam autenticitatea tokenului si apoi daca este expirat
    public boolean isTokenValid(String username, String token){
        JWTVerifier verifier = getJWTVerifier();
        return StringUtils.isNotEmpty(username) && !isTokenExpired(verifier, token);
    }

    // returnarea unui subject: adica al cui este: usernamen-ul
    public String getSubject(String token){
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getSubject();
    }

    //primim un verifier care va trece prin functia 'getJWTVerifier' pentru a vedea daca token-ul trimis este unul valid trimis de catre noi
    private boolean isTokenExpired(JWTVerifier verifier, String token){
        Date expiration = verifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
    }


    //returneaza o lista de autoritari pe care le are un user
    //Granted authority => autoritatea unui user
    private String[] getClaimsFromUser(UserDetails userDetails){
        List<String> authorities = new ArrayList<>();
        for(GrantedAuthority grantedAuthority: userDetails.getAuthorities()){
            authorities.add(grantedAuthority.getAuthority());
        }
        return authorities.toArray(new String[0]);
    }

    //se returneza permisiunile din token dupa ce am verificat daca este autentic
    private String[] getClaimsFromToken(String token){
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
    }


    //se face decodificarea token-ului sa vedem daca este autentic, generat de mine
    private JWTVerifier getJWTVerifier(){
        JWTVerifier verifier;
        try{
            Algorithm algorithm = Algorithm.HMAC512(secret);
            verifier =  JWT.require(algorithm).withIssuer(MY_CODE).build();
        }catch (JWTVerificationException exception){
            throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
        }
        return verifier;
    }
}
