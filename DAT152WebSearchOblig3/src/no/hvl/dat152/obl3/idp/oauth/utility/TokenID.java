/**
 * 
 */
package no.hvl.dat152.obl3.idp.oauth.utility;

public class TokenID {
	
	private String id_token;
	private String access_token;
	private String token_type;
	private String expires_in;
	private String refresh_token;
	
	private UserClaims userclaims;
	
	public TokenID() {
		
	}

	/**
	 * @return the id_token
	 */
	public String getId_token() {
		return id_token;
	}

	/**
	 * @param id_token the id_token to set
	 */
	public void setId_token(String id_token) {
		this.id_token = id_token;
	}

	/**
	 * @return the access_token
	 */
	public String getAccess_token() {
		return access_token;
	}

	/**
	 * @param access_token the access_token to set
	 */
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	/**
	 * @return the token_type
	 */
	public String getToken_type() {
		return token_type;
	}

	/**
	 * @param token_type the token_type to set
	 */
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	/**
	 * @return the expires_in
	 */
	public String getExpires_in() {
		return expires_in;
	}

	/**
	 * @param expires_in the expires_in to set
	 */
	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	/**
	 * @return the refresh_token
	 */
	public String getRefresh_token() {
		return refresh_token;
	}

	/**
	 * @param refresh_token the refresh_token to set
	 */
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	/**
	 * @return the userConsentData
	 */
	public UserClaims getUserClaims() {
		return userclaims;
	}

	/**
	 * @param userConsentData the userConsentData to set
	 */
	public void setUserClaims(UserClaims userclaims) {
		this.userclaims = userclaims;
	}

}
