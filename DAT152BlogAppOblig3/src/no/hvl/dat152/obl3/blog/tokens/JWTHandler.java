/**
 * 
 */
package no.hvl.dat152.obl3.blog.tokens;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import com.google.gson.Gson;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.SignatureException;


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
	
	public static JWT getJWT(String jwt) {
		
		int begin = jwt.indexOf(".");
		int end = jwt.lastIndexOf(".");
		String body = jwt.substring(begin+1, end);
		String decBody = new String(Base64.getUrlDecoder().decode(body));

		Gson gson = new Gson();
		JWT _jwt = gson.fromJson(decBody.trim(), JWT.class);
		
		return _jwt;
	}
	
	public static boolean verifyJWT(String jwt, String webpath) {
		
		// this is asymmetric key - public key is used to verify the signature
		
		// decode JWT
		// check issued at
		// check expiry date
		// check the signature
		
		String keypath = webpath+"keys/publickey.enc";
		PublicKey puk = loadPublicKey(keypath);

		try {
			
			Jwts.parserBuilder().setSigningKey(puk).build().parse(jwt);		
			
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
	
	public static boolean verifyJWTSignature(String jws, String webpath) {
		
		// this is asymmetric key - public key of the IdP is used to verify the signature
		
		// decode JWT
		// check issued at
		// check expiry date
		// check the signature
		
		String keypath = webpath+"keys/publickey.enc";
		PublicKey puk = loadPublicKey(keypath);
		
		//Jws<Claims> jwsc;
		try {
			
			Jwts.parserBuilder().setSigningKey(puk).build().parseClaimsJws(jws);

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
	
	private static PublicKey loadPublicKey(String path) {
		
		PublicKey pubkey = null;
		KeyFactory kf;
		X509EncodedKeySpec x509spec;
		try {
			String publickey = readKeys(path);
			kf = KeyFactory.getInstance("RSA");
			byte[] publicKeyBytes = Decoders.BASE64.decode(publickey);
			x509spec = new X509EncodedKeySpec(publicKeyBytes);
			pubkey = kf.generatePublic(x509spec);
		} catch(NoSuchAlgorithmException | InvalidKeySpecException e) {
			//
		}
			
		return pubkey;
	}
	
	private static String readKeys(String path) {

		String line = "";
		try (BufferedReader br = new BufferedReader(new FileReader(path))){
			
			line=br.readLine();			
			
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		return line;
	}
	
}
