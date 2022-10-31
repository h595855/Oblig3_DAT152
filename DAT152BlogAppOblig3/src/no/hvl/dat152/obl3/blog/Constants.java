/**
 * 
 */
package no.hvl.dat152.obl3.blog;


public class Constants {

	public static String CLIENT_ID = "";			// this is assigned to the client by the Identity Provider
	//public static String CLIENT_SECRET = "";		// this is issued to the client by the identity Provider during registration
	
	public static final String STATE = "abcdef";		// this should be a secure random number (not used in this example)
	
	public static final int IDP_PORT = 9092;
	public static final String IDP_AUTH_ENDPOINT = "http://localhost:"+IDP_PORT+"/DAT152WebSearch/login";
	public static final String IDP_LOGOUT_ENDPOINT = "http://localhost:"+IDP_PORT+"/DAT152WebSearch/logout";
	public static final String IDP_TOKEN_ENDPOINT = "http://localhost:"+IDP_PORT+"/DAT152WebSearch/token";
	public static final String IDP_USERCLAIMS_ENDPOINT = "http://localhost:"+IDP_PORT+"/DAT152WebSearch/userinfo";
	public static final String IDP_REGISTER_ENDPOINT = "http://localhost:"+IDP_PORT+"/DAT152WebSearch/register";
	
	public static final String IDP_PATH = "/DAT152WebSearch";
	
	// client
	public static final int SP_CALLBACK_PORT = 9091;
	public static final String SP_CALLBACK_ADDRESS = "http://localhost:"+SP_CALLBACK_PORT+"/blogapp/callback";
	
}
