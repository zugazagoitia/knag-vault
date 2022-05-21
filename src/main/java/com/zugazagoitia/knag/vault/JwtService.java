package com.zugazagoitia.knag.vault;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Optional;

@Service("jwtService")
public class JwtService {

    private final Algorithm algorithmRS;

    public JwtService(@Value("#{keyConfig.pub}") String rawPubKey) {
        RSAPublicKey publicKey = (RSAPublicKey) KeyHelper.loadPublicKeyBase64(rawPubKey);
        algorithmRS = Algorithm.RSA512(publicKey, null);
    }

    /**
     * Get the decoded JWT from the token and verify it. If the token is valid, return the user id.
     *
     * @param token The token to verify.
     * @return The user id if the token is valid, otherwise empty.
     */
    public Optional<String> getUser(String token) {
        JWTVerifier verifier = JWT.require(algorithmRS).withIssuer("knag-users").build();
        DecodedJWT jwt;
        try {
            jwt = verifier.verify(token);

        } catch (Exception e) {
            return Optional.empty();
        }

        return Optional.ofNullable(jwt.getSubject());
    }

    private static class KeyHelper {

        private static final String PUBLIC_HEADER = "-----BEGIN PUBLIC KEY-----";
        private static final String PUBLIC_FOOTER = "-----END PUBLIC KEY-----";

        public static PublicKey loadPublicKeyBase64(String key) {
            PublicKey pub = null;
            try {
                key = key.replace(PUBLIC_HEADER, "");
                key = key.replace(PUBLIC_FOOTER, "");
                key = key.replace("\n", "");

                byte[] bytes = Base64.getDecoder().decode(key.getBytes());

                /* Generate public key. */
                X509EncodedKeySpec ks = new X509EncodedKeySpec(bytes);
                KeyFactory kf = KeyFactory.getInstance("RSA");
                pub = kf.generatePublic(ks);
            } catch (Exception x) {
                x.printStackTrace();
            }
            return pub;
        }
    }
}
