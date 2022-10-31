/**
 * 
 */
package no.hvl.dat152.obl3.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.digest.DigestUtils;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;


public class Crypto {
	
//	private static final String AES_CBC_PKCS5Padding = "AES/CBC/PKCS5Padding";
//	private static final String AES_OFB_PKCS5Padding = "AES/OFB/PKCS5Padding";
	
	public static String generateUUIDCode() {
		return UUID.randomUUID().toString();
	}
	
	public static String generateMD5Hash(String value) {
		return DigestUtils.md5Hex(value);
	}
	
	public static String generateSHA1Hash(String value) {
		
		return DigestUtils.shaHex(value);
	}
	
	public static String generateRandomCryptoCode() {
		
		SecureRandom sr = null;
		try {
			sr = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			//
		}
		byte[] ivsecret = new byte[16];
		sr.nextBytes(ivsecret);
		
		return DatatypeConverter.printHexBinary(ivsecret);
	}
	
	public static String authorizationCode(String clientId) {
		String code = generateRandomCryptoCode();
		String expirydate = getExpiryDate(60);			// valid for only 60seconds
		String hash_auth_code = generateSHA1Hash(code+expirydate);
		
		TokenSingleton.Instance().addAuthCode(clientId, code+expirydate);
		
		return hash_auth_code;
	}
	
	public static void writer(String file, String value) {
		// store the client info in a file on server. To be used to verify the auth_code and discarded after
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(value);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	private static String getExpiryDate(int amount) {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, amount); 		
		
		return cal.getTime().toString();
	}
	
	public static Date expiryDate(int amount) {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, amount); 		
		
		return cal.getTime();
	}

	public static boolean verifyAuthorizationCode(String clientId, String code) {

		String codeandexpirydate = TokenSingleton.Instance().getAuthCode().get(clientId);

		String hash_auth_code = generateSHA1Hash(codeandexpirydate);
		
		return hash_auth_code.equals(code);
	}
	
	public static void secretKeyGenerator() {
		
		SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
		String secret = Encoders.BASE64.encode(key.getEncoded());
		
		// save secretkey in a file
		String keypath = "WebContent/WEB-INF/keys/secretkey.enc";		
		writer(keypath, secret);
	}
	
	public static void asymmetricKeyGenerator() {
		
		KeyPair key = Keys.keyPairFor(SignatureAlgorithm.RS256);
		String privateKey = Encoders.BASE64.encode(key.getPrivate().getEncoded());
		String publicKey = Encoders.BASE64.encode(key.getPublic().getEncoded());
		
		// save keys in a file	
		String keypath = "WebContent/WEB-INF/keys/privatekey.enc";
		writer(keypath, privateKey);
		
		keypath = "WebContent/WEB-INF/keys/publickey.enc";
		writer(keypath, publicKey);
	}
	
	public static SecretKey loadSecretKey(String path) {
		
		String secretkey = readKeys(path);
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretkey));
		
		return key;
	}
	
	public static PublicKey loadPublicKey(String path) {
		
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
	
	public static PrivateKey loadPrivateKey(String path) {
		
		PrivateKey privkey = null;
		String privatekey = readKeys(path);
		KeyFactory kf;

		try {
			kf = KeyFactory.getInstance("RSA");
			byte[] privateKeyBytes = Decoders.BASE64.decode(privatekey);
			PKCS8EncodedKeySpec pkcsspec = new PKCS8EncodedKeySpec(privateKeyBytes);
			privkey = kf.generatePrivate(pkcsspec);
		} catch(NoSuchAlgorithmException | InvalidKeySpecException e) {
			//
		}

		
		return privkey;
	}
	
//	public static String encrypt(String message, SecretKey key, byte[] ivsecret) {
//		
//		IvParameterSpec iv = new IvParameterSpec(ivsecret);
//		try {
//			
//			Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5Padding);
//			cipher.init(Cipher.ENCRYPT_MODE, key, iv);
//			byte[] text = message.getBytes("UTF-8");
//			byte[] ciphertext = cipher.doFinal(text);
//			
//			return Base64.getEncoder().encodeToString(ciphertext);
//			
//		} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException e) {
//			e.printStackTrace();
//		} 
//		
//		return null;
//	}
//	
//	public static String decrypt(String ciphertext, SecretKey key, byte[] ivsecret) {
//		
//		IvParameterSpec iv = new IvParameterSpec(ivsecret);
//		try {
//			Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5Padding);
//			cipher.init(Cipher.DECRYPT_MODE, key, iv);
//			byte[] ctext = ciphertext.getBytes("UTF-8");
//			byte[] plaintext = cipher.doFinal(Base64.getDecoder().decode(ctext));
//			
//			String text = new String(plaintext);
//			
//			return text;
//			
//		} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | UnsupportedEncodingException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException e) {
//			e.printStackTrace();
//		} 
//		
//		return null;
//	}
	
	public static String readKeys(String path) {

		String line = "";
		try (BufferedReader br = new BufferedReader(new FileReader(path))){
			
			line=br.readLine();
			
			
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		return line;
	}

}
