/**
 * 
 */
package no.hvl.dat152.obl3.idp.oauth.utility;


import com.google.gson.Gson;

import no.hvl.dat152.obl3.util.TokenSingleton;

public class UserClaimHelper {

	
	public static UserClaims getUserClaimObject(String clientid) {

		UserClaims ucd = null;
		String userconsent = TokenSingleton.Instance().getConsents().get(clientid);
		if(!userconsent.isEmpty()) {
			ucd = new Gson().fromJson(userconsent, UserClaims.class);			
		}
		
		
		return ucd;
	}

}
