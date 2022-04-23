package com.example.spring_graphql.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

/**
 * @Author Morton.Mei
 * @Description: Create and verify token
 * @Date 2022/4/22 17:05
 */
public class TokenUtil {

    static final long MILLIS_IN_HOUR = 1 * 60 * 60 * 1000;
    static final String ISSUER = "Morton.Mei";
    static final String USER_ID = "userId";
    static final Algorithm algorithm = Algorithm.HMAC256("morton.secret");

    /**
     * @Author Morton.Mei
     * @Description: create token
     * @Date 17:14 2022/4/22
     */
    public static String signToken(Integer userId, int expirationInHour) {
        String token = JWT.create()
                .withIssuer(ISSUER)
                .withClaim(USER_ID, userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationInHour * MILLIS_IN_HOUR))
                .sign(algorithm);
        return token;
    }

    /**
     * @Author Morton.Mei
     * @Description: verify token
     * @Date 17:22 2022/4/22
     */
    public static Integer verifyToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        Integer userId = jwt.getClaim(USER_ID).asInt();
        return userId;
    }
}
