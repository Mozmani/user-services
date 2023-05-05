package io.mozmani.userservices.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.tomcat.util.codec.binary.Base64;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Simple JWT utilities class for authorization handling.
 */
public class JWTUtility {

    /**
     * Simple method to generate a user JWT.
     * @param privateKey privat ekey string.
     * @param publicKey public key string.
     * @param userID user's id.
     * @return String jwt.
     * @throws GeneralSecurityException on token minting error.
     */
    public static String generateUserToken(String privateKey, String publicKey, String userID) throws GeneralSecurityException {
        RSAPrivateKey rsaPrivateKey = getPrivateKeyFromString(privateKey);
        RSAPublicKey rsaPublicKey = getPublicKeyFromString(publicKey);
        Instant current = Instant.now();

        return JWT.create().withIssuer("Mozmani.io")
                .withClaim("userId", userID)
                .withIssuedAt(Date.from(current))
                .withExpiresAt(Date.from(current.plus(2, ChronoUnit.HOURS)))
                .sign(Algorithm.RSA256(rsaPublicKey, rsaPrivateKey));
    }


    /**
     * Simple JWT verification method.
     * @param token String version of a JWT token.
     * @param publicKey public RSA key for verification.
     * @return Decoded JWT
     * @throws GeneralSecurityException if any issues in verification.
     */
    public static DecodedJWT verifyUserJWT(
            String token, String publicKey) throws GeneralSecurityException {
        RSAPublicKey rsaPublicKey = getPublicKeyFromString(publicKey);
        Algorithm verification = Algorithm.RSA256(rsaPublicKey, null);
        JWTVerifier verifier = JWT.require(verification)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt;
    }

    /**
     * Generates java based RSA private key from a private pem key.
     * @param key key string.
     * @return RSA private key.
     * @throws GeneralSecurityException on security issue.
     */
    private static RSAPrivateKey getPrivateKeyFromString(String key) throws GeneralSecurityException {
        String privateKeyPEM = key;
        privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----\n", "");
        privateKeyPEM = privateKeyPEM.replace("-----END PRIVATE KEY-----", "");
        byte[] encoded = Base64.decodeBase64(privateKeyPEM);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return (RSAPrivateKey) kf.generatePrivate(keySpec);
    }

    /**
     * Generates java based RSA public key from a public pem key.
     * @param key key string.
     * @return RSA public key.
     * @throws GeneralSecurityException on issue.
     */
    private static RSAPublicKey getPublicKeyFromString(String key) throws GeneralSecurityException {
        String publicKeyPEM = key;
        publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----\n", "");
        publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");
        byte[] encoded = Base64.decodeBase64(publicKeyPEM);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));
    }
}
