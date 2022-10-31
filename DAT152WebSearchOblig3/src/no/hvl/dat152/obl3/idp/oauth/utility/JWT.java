/**
 * 
 */
package no.hvl.dat152.obl3.idp.oauth.utility;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWT {

	// JWT registered claims: https://datatracker.ietf.org/doc/html/rfc7519#section-4.1
	/*
	 * "iss" (Issuer) Claim
	 * "sub" (Subject) Claim
	 * "aud" (Audience) Claim
	 * "exp" (Expiration Time) Claim
	 * "nbf" (Not Before) Claim
	 * "iat" (Issued At) Claim
	 * "jti" (JWT ID) Claim
	 */
	private String iss;
	private String sub;
	private String aud;
	private Date exp;
	private Date nbf;
	private Date iat;
	private String jti;
	
	private Map<String, Object> customClaims;
	/**
	 * 
	 */
	public JWT() {
		customClaims = new HashMap<>();
	}

	/**
	 * @return the iss
	 */
	public String getIss() {
		return iss;
	}

	/**
	 * @param iss the iss to set
	 */
	public void setIss(String iss) {
		this.iss = iss;
	}

	/**
	 * @return the sub
	 */
	public String getSub() {
		return sub;
	}

	/**
	 * @param sub the sub to set
	 */
	public void setSub(String sub) {
		this.sub = sub;
	}

	/**
	 * @return the aud
	 */
	public String getAud() {
		return aud;
	}

	/**
	 * @param aud the aud to set
	 */
	public void setAud(String aud) {
		this.aud = aud;
	}

	/**
	 * @return the exp
	 */
	public Date getExp() {
		return exp;
	}

	/**
	 * @param exp the exp to set
	 */
	public void setExp(Date exp) {
		this.exp = exp;
	}

	/**
	 * @return the nbf
	 */
	public Date getNbf() {
		return nbf;
	}

	/**
	 * @param nbf the nbf to set
	 */
	public void setNbf(Date nbf) {
		this.nbf = nbf;
	}

	/**
	 * @return the iat
	 */
	public Date getIat() {
		return iat;
	}

	/**
	 * @param iat the iat to set
	 */
	public void setIat(Date iat) {
		this.iat = iat;
	}

	/**
	 * @return the jti
	 */
	public String getJti() {
		return jti;
	}

	/**
	 * @param jti the jti to set
	 */
	public void setJti(String jti) {
		this.jti = jti;
	}

	/**
	 * @return the customClaims
	 */
	public Map<String, Object> getCustomClaims() {
		return customClaims;
	}
	
	public void addCustomClaims(String name, String value) {
		customClaims.put(name, value);
	}

}
