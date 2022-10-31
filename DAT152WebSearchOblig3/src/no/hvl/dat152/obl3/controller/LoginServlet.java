package no.hvl.dat152.obl3.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.hvl.dat152.obl3.database.AppUser;
import no.hvl.dat152.obl3.database.AppUserDAO;
import no.hvl.dat152.obl3.idp.oauth.utility.Constants;
import no.hvl.dat152.obl3.idp.oauth.utility.OpenIDUser;
import no.hvl.dat152.obl3.util.Role;
import no.hvl.dat152.obl3.util.ServerConfig;
import no.hvl.dat152.obl3.util.TokenSingleton;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String client_id = request.getParameter(Constants.CLIENT_ID);

		// check if the user is currently logged in (has an authenticated session)?
		if(RequestHelper.isLoggedIn(request)) {
			//1. check if the user is requesting a SSO (we do this by checking if the request comes with a client_id
			client_id = request.getParameter(Constants.CLIENT_ID);	
			if(client_id != null) {
				// check if the user still has a valid SSO token?
				if (RequestHelper.isLoggedInSSO(request, client_id)) {
					
					request.getRequestDispatcher("authorize").forward(request, response);
				} else {
					/* No SSO, validate or doSSOAuthentication*/
					validateOrForwardUserReq(request, response, client_id);
				}
			} else { /* Not a SSO - logged-in request is local */
				// forward user to searchpage
				request.getRequestDispatcher("searchpage").forward(request, response);
			}
		} else {			
			client_id = request.getParameter(Constants.CLIENT_ID);
			if(client_id != null) {
				/* cache SSO data before authenticating */
				RequestHelper.cacheSSOData(request, client_id);
			}
			request.getRequestDispatcher("login.jsp").forward(request, response);			
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
				
		/* 2. login the user (a "post" request from login.jsp is handled here) */
		
		String client_id = request.getParameter(Constants.CLIENT_ID);
		
		/* first, check that the user has a valid session */
		if(RequestHelper.isLoggedIn(request)) {
			
			handleLoginLogic(request, response, client_id);
			
		} else { /* otherwise, log in the user */ 
			boolean successfulLogin = login(request, response);

			if (successfulLogin) {
				
				handleLoginLogic(request, response, client_id);
				
			} else {
				String username = request.getParameter("username");
				request.setAttribute("message", "Username " + username + ": Login failed!..");
				request.getRequestDispatcher("login.jsp").forward(request, response);
			}
		}

	}
	
	private void handleLoginLogic(HttpServletRequest request, HttpServletResponse response, String client_id) throws IOException, ServletException {
		// check if the user is requesting a SSO (we do this by checking if the request comes with a client_id				
		if(!client_id.isEmpty()) {	/* SSO request */
			
			validateOrForwardUserReq(request, response, client_id);

		} else { /* Not a SSO - logged-in request is local */
			/* forward user to searchpage */
			request.getRequestDispatcher("searchpage").forward(request, response);
		}
	}
	
	private void validateOrForwardUserReq(HttpServletRequest request, HttpServletResponse response, String client_id) throws IOException, ServletException {
		/* check that this user owns the current authenticated session */
		AppUser appUser = (AppUser) request.getSession().getAttribute(Constants.USER);
		if(appUser.getClientID().equals(client_id)) {
			/* do SSO authentication */
			doOpenIDSSOAuthentication(request, response);
		} else { /* otherwise, invalidate the current user and redirect this user with the client_id to authenticate*/
			
			/* cache SSO data before invalidating */
			RequestHelper.cacheSSOData(request, client_id);
			request.getSession().invalidate();
			request.setAttribute("message", "Current session does not belong to the user with Id:"+ client_id+"\nLogin again with the credentials for this user..");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}
	
	private void doOpenIDSSOAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		/**
		 * Authentication: OpenID specification (parameters)
		 * response_type
		 * scope
		 * client_id
		 * state (OPTIONAL)
		 * redirect_uri
		 */
			
		String client_id = request.getParameter(Constants.CLIENT_ID);
		OpenIDUser oidc_user = createOpenIDUser(request);
		
		AppUser appUser = (AppUser) request.getSession().getAttribute(Constants.USER);
		
		request.getSession().setAttribute(client_id, oidc_user);
		TokenSingleton.Instance().addOpenIDUser(client_id, oidc_user);
		TokenSingleton.Instance().addRole(client_id, appUser.getRole());
			
		// redirect to authorization endpoint
		request.getRequestDispatcher("authorize").forward(request, response);			
	}
	
	private OpenIDUser createOpenIDUser(HttpServletRequest request) {
		
		String response_type = request.getParameter(Constants.RESPONSE_TYPE);
		String scope = request.getParameter(Constants.SCOPE);
		String client_id = request.getParameter(Constants.CLIENT_ID);
		String redirect_uri = request.getParameter(Constants.REDIRECT_URI);
		String state = request.getParameter(Constants.STATE);
		
		// handle client's scope
		String[] scopes = scope.split(" ");
		List<String> consents = new ArrayList<>();
		for(String _scope : scopes) {
			consents.add(_scope);
		}
		OpenIDUser oidc_user = new OpenIDUser(client_id);
		oidc_user.setConsents(consents);
		oidc_user.setResponseType(response_type);
		oidc_user.setRedirectURI(redirect_uri);
		
		if(state != null)
			oidc_user.setState(state);
		
		return oidc_user;
	}
	
	private boolean login(HttpServletRequest request,
			HttpServletResponse response) {
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		boolean successfulLogin = false;

		if (username != null && password != null) {

			AppUserDAO userDAO = new AppUserDAO();
			AppUser authUser = userDAO.getAuthenticatedUser(username, password);

			if (authUser != null) {
				successfulLogin = true;
				request.getSession().setAttribute(Constants.USER, authUser);
				request.getSession().setAttribute("updaterole", "");
			
				// set dictionary url in a cookie
				Cookie dicturl = new Cookie("dicturl",ServerConfig.DEFAULT_DICT_URL);
				dicturl.setMaxAge(60*10);
				response.addCookie(dicturl);
				
				// admin issues
				if(authUser.getRole().equals(Role.ADMIN.name())) {
					List<String> usernames = userDAO.getUsernames();
					request.getSession().setAttribute("usernames", usernames);
					request.getSession().setAttribute("updaterole", "<a href=\"updaterole\">Update Role</a>");
				}
			}
		}
		
		return successfulLogin;
	}
}
