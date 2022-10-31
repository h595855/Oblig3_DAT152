package no.hvl.dat152.obl3.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import no.hvl.dat152.obl3.database.AppUser;
import no.hvl.dat152.obl3.idp.oauth.utility.Constants;
import no.hvl.dat152.obl3.idp.oauth.utility.JWTHandler;
import no.hvl.dat152.obl3.idp.oauth.utility.OpenIDUser;
import no.hvl.dat152.obl3.util.TokenSingleton;

public class RequestHelper {

	public static String getCookieValue(HttpServletRequest request,
			String cookieName) {

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equals(cookieName)) {
					return c.getValue();
				}
			}
		}
		return null;
	}
	
	public static boolean isLoggedIn(HttpServletRequest request) {
		return request.getSession().getAttribute("user") != null;
	}
	
	public static boolean isLoggedInSSO(HttpServletRequest request, String clientid) {

		// check whether sso user has not been revoked from the backend or whether it exists in the first place
		OpenIDUser oidc_user = TokenSingleton.Instance().getOpenIDUser(clientid);	
		
		// check whether the current request is coming from the same user who has this session
		OpenIDUser ui = (OpenIDUser) request.getSession().getAttribute(clientid);

		return oidc_user != null && ui != null;
	}
	
	public static void cacheSSOData(HttpServletRequest request, String client_id) {
		String response_type = request.getParameter(Constants.RESPONSE_TYPE);
		String scope = request.getParameter(Constants.SCOPE);
		String redirect_uri = request.getParameter(Constants.REDIRECT_URI);
		String state = request.getParameter(Constants.STATE);
		
		request.setAttribute(Constants.CLIENT_ID, client_id);
		request.setAttribute(Constants.RESPONSE_TYPE, response_type);
		request.setAttribute(Constants.SCOPE, scope);
		request.setAttribute(Constants.REDIRECT_URI, redirect_uri);
		request.setAttribute(Constants.STATE, state);
	}

}
