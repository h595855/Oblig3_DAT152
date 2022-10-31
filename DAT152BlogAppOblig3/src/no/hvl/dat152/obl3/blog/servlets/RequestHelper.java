package no.hvl.dat152.obl3.blog.servlets;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import no.hvl.dat152.obl3.blog.Constants;
import no.hvl.dat152.obl3.blog.tokens.JWT;
import no.hvl.dat152.obl3.blog.tokens.JWTHandler;

public class RequestHelper {

	public static String getCookieValue(HttpServletRequest request,
			String cookieName) {

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equals(cookieName.trim())) {
					return c.getValue();
				}
			}
		}
		return null;
	}
	
	public static Cookie getCookie(HttpServletRequest request,
			String cookieName) {

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equals(cookieName.trim())) {
					return c;
				}
			}
		}
		return null;
	}
	
	public static boolean isLoggedIn(HttpServletRequest request) {
		return request.getSession().getAttribute("user") != null;
	}
	
	public static boolean isLoggedInSSO(HttpServletRequest request, String keypath) {
		String id_token = RequestHelper.getCookieValue(request, "id_token");
		doJWT(request, id_token);
		
		return JWTHandler.verifyJWT(id_token, keypath);
		
	}
	
	public static void doJWT(HttpServletRequest request, String id_token) {
		JWT jwt = JWTHandler.getJWT(id_token);
		String role = jwt.getRole();
		String user = jwt.getUsername();
		request.getSession().setAttribute("loggeduser", user);
		request.getSession().setAttribute("role", role);
		request.getSession().setAttribute("logoutep", Constants.IDP_LOGOUT_ENDPOINT);
		request.getSession().setAttribute("clientid", Constants.CLIENT_ID);
		request.getSession().setAttribute("redirectep", Constants.SP_CALLBACK_ADDRESS);
	}
}
