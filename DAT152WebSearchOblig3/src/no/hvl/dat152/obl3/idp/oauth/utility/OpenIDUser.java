/**
 * 
 */
package no.hvl.dat152.obl3.idp.oauth.utility;

import java.util.Collections;
import java.util.List;

public class OpenIDUser {

	private String clientID;
	private String responseType;
	private String redirectURI;
	private String state;
	
	private List<String> consents;
	

	public OpenIDUser(String clientID) {
		
		this.clientID = clientID;
	}

	/**
	 * @return the clientID
	 */
	public String getClientID() {
		return clientID;
	}

	/**
	 * @return the responseType
	 */
	public String getResponseType() {
		return responseType;
	}

	/**
	 * @param responseType the responseType to set
	 */
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	/**
	 * @return the redirectURI
	 */
	public String getRedirectURI() {
		return redirectURI;
	}

	/**
	 * @param redirectURI the redirectURI to set
	 */
	public void setRedirectURI(String redirectURI) {
		this.redirectURI = redirectURI;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @param consents the consents to set
	 */
	public void setConsents(List<String> consents) {
		
		this.consents = consents;
	}
	
	/**
	 * @return the consents
	 */
	public List<String> getConsents() {
		return Collections.unmodifiableList(consents);
	}

}
