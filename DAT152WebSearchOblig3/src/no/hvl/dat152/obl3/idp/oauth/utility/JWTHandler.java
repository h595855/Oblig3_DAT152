/**
 * 
 */
package no.hvl.dat152.obl3.idp.oauth.utility;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.SecretKey;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import no.hvl.dat152.obl3.util.Crypto;

public class JWTHandler {

	/**
	 * 
	 */
	// Header
	// RS256	RSA256	RSASSA-PKCS1-v1_5 with SHA-256
	// HS256	HMAC256	HMAC with SHA-256
	
	// JWT registered claims: https://datatracker.ietf.org/doc/html/rfc7519#section-4.1
	/*
	 * "iss" (Issuer) Claim
	 * "sub" (Subject) Claim
	 * "aud" (Audience) Claim
	 * "exp" (Expiration Time) Claim
	 * "nbf" (Not Before) Claim
	 * "iat" (Issued At) Claim
	 * "jti" (JWT ID) Claim
	 * 
	 */
	
	public static String createJWTAsymmetricKey(JWT jwt, String webpath) {
		
		// we use asymmetric keys here - private key is used to sign the token
		String keypath = webpath+"keys/privatekey.enc";
		PrivateKey prk = Crypto.loadPrivateKey(keypath);
		
		String jws = Jwts.builder()
				.setIssuer(jwt.getIss())
				.setSubject(jwt.getSub())
				.setAudience(jwt.getAud())
				.setExpiration(jwt.getExp())
				.setIssuedAt(jwt.getIat())
				.addClaims(jwt.getCustomClaims())
				.signWith(prk)
				.compact();
		
		//System.out.println(jws);
		
		return jws;
	}
	
	public static String createJWTSymmetricKey(JWT jwt, String webpath) {
		
		// we use symmetric key here
		String keypath = webpath+"keys/secretkey.enc";
		SecretKey secret = Crypto.loadSecretKey(keypath);
		
		String jws = Jwts.builder()
				.setIssuer(jwt.getIss())
				.setSubject(jwt.getSub())
				.setAudience(jwt.getAud())
				.setExpiration(jwt.getExp())
				.setIssuedAt(jwt.getIat())
				.addClaims(jwt.getCustomClaims())
				.signWith(secret)
				.compact();
		
		return jws;
	}
	
//	public static String getSubjectFromJWT(String jws, String webpath) {
//		
//		String keypath = webpath+"keys/secretkey.enc";
//		SecretKey secret = Crypto.loadSecretKey(keypath);
//		
//		return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(jws).getBody().getSubject();
//	}
//	
//	public static String getAudienceFromJWT(String jws, String webpath) {
//		
//		String keypath = webpath+"keys/secretkey.enc";
//		SecretKey secret = Crypto.loadSecretKey(keypath);
//		
//		return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(jws).getBody().getAudience();
//	}
//	
//	public static String getSubjectFromJWTAccessToken(String jws, String webpath) {
//		
//		String keypath = webpath+"keys/publickey.enc";
//		PublicKey puk = Crypto.loadPublicKey(keypath);
//		
//		return Jwts.parserBuilder().setSigningKey(puk).build().parseClaimsJws(jws).getBody().getSubject();
//	}
	
	public static String getAudienceFromJWT(String jws, String webpath) {
		
		String keypath = webpath+"keys/publickey.enc";
		PublicKey puk = Crypto.loadPublicKey(keypath);
		
		return Jwts.parserBuilder().setSigningKey(puk).build().parseClaimsJws(jws).getBody().getAudience();
	}
	
	public static boolean verifyJWTAsymmetric(String jws, String webpath) {
		
		// this is asymmetric key - public key is used to verify the signature
		
		// decode JWT
		// check issued at
		// check expiry date
		// check the signature
		
		String keypath = webpath+"keys/publickey.enc";
		PublicKey puk = Crypto.loadPublicKey(keypath);
		
		//Jws<Claims> jwsc;
		try {
			Jwts.parserBuilder().setSigningKey(puk).build().parseClaimsJws(jws);
			//System.out.println(jwsc);
			return true;
		}catch(ExpiredJwtException  e) {
			// token expired
			return false;
		}catch(SignatureException e) {
			// invalid signature
			return false;
		}catch(JwtException e) {
			// other problems with JWT
			return false;
		}

	}
	
	public static boolean verifyJWTSymmetric(String jws, String webpath) {
		// decode JWT
		// check issued at
		// check expiry date
		// check the signature
		
		String keypath = webpath+"keys/secretkey.enc";
		SecretKey secret = Crypto.loadSecretKey(keypath);
		
		//Jws<Claims> jwsc;
		try {
			Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(jws);
			//System.out.println(jwsc);
			return true;
		}catch(ExpiredJwtException  e) {
			// token expired
			return false;
		}catch(SignatureException e) {
			// invalid signature
			return false;
		}catch(JwtException e) {
			// other problems with JWT
			return false;
		}

	}
	
	public static String getClientIdFromJson(String jwt) {
		int begin = jwt.indexOf(".");
		int end = jwt.lastIndexOf(".");
		String body = jwt.substring(begin+1, end);
		String decBody = new String(Base64.getUrlDecoder().decode(body));
		JsonObject jobj = new Gson().fromJson(decBody, JsonObject.class);
		String id = jobj.get("aud").getAsString();
		
		return id;
	}

}
