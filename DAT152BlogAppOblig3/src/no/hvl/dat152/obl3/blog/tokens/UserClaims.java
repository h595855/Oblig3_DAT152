/**
 * 
 */
package no.hvl.dat152.obl3.blog.tokens;


public class UserClaims {

	// https://openid.net/specs/openid-connect-core-1_0.html#StandardClaims
	// we only implement a few here
	private String sub;
	private String name;
	private String given_name;
	private String family_name;
	private String middle_name;
	private String nickname;
	private String preferred_username;
	private String profile;
	private String email;
	private String email_verified;
	private String phone_number;

	
	public UserClaims() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return the subject
	 */
	public String getSub() {
		return sub;
	}


	/**
	 * @param subject the subject to set
	 */
	public void setSub(String subject) {
		this.sub = subject;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the given_name
	 */
	public String getGiven_name() {
		return given_name;
	}


	/**
	 * @param given_name the given_name to set
	 */
	public void setGiven_name(String given_name) {
		this.given_name = given_name;
	}


	/**
	 * @return the family_name
	 */
	public String getFamily_name() {
		return family_name;
	}


	/**
	 * @param family_name the family_name to set
	 */
	public void setFamily_name(String family_name) {
		this.family_name = family_name;
	}


	/**
	 * @return the middle_name
	 */
	public String getMiddle_name() {
		return middle_name;
	}


	/**
	 * @param middle_name the middle_name to set
	 */
	public void setMiddle_name(String middle_name) {
		this.middle_name = middle_name;
	}


	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}


	/**
	 * @param nickname the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}


	/**
	 * @return the preferred_username
	 */
	public String getPreferred_username() {
		return preferred_username;
	}


	/**
	 * @param preferred_username the preferred_username to set
	 */
	public void setPreferred_username(String preferred_username) {
		this.preferred_username = preferred_username;
	}


	/**
	 * @return the profile
	 */
	public String getProfile() {
		return profile;
	}


	/**
	 * @param profile the profile to set
	 */
	public void setProfile(String profile) {
		this.profile = profile;
	}


	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}


	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}


	/**
	 * @return the email_verified
	 */
	public String getEmail_verified() {
		return email_verified;
	}


	/**
	 * @param email_verified the email_verified to set
	 */
	public void setEmail_verified(String email_verified) {
		this.email_verified = email_verified;
	}

	/**
	 * @return the phone_number
	 */
	public String getPhone_number() {
		return phone_number;
	}

	/**
	 * @param phone_number the phone_number to set
	 */
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	
	@Override
	public String toString() {
		return "{sub:"+sub+", "
				+ "phone:"+phone_number+"}";
	}

}
