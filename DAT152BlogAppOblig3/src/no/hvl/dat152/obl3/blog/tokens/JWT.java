/**
 * 
 */
package no.hvl.dat152.obl3.blog.tokens;


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
	private String exp;
	private String nbf;
	private String iat;
	private String jti;
	private String role;
	private String username;

	/**
	 * 
	 */
	public JWT() {

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
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @param exp the exp to set
	 */
	public void setExp(String exp) {
		this.exp = exp;
	}

	/**
	 * @param nbf the nbf to set
	 */
	public void setNbf(String nbf) {
		this.nbf = nbf;
	}

	/**
	 * @param iat the iat to set
	 */
	public void setIat(String iat) {
		this.iat = iat;
	}

	/**
	 * @return the exp
	 */
	public String getExp() {
		return exp;
	}

	/**
	 * @return the nbf
	 */
	public String getNbf() {
		return nbf;
	}

	/**
	 * @return the iat
	 */
	public String getIat() {
		return iat;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

}
