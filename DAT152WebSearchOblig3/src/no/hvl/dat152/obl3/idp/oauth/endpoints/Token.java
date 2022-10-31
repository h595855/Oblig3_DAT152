package no.hvl.dat152.obl3.idp.oauth.endpoints;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import no.hvl.dat152.obl3.database.AppUserDAO;
import no.hvl.dat152.obl3.idp.oauth.utility.Constants;
import no.hvl.dat152.obl3.idp.oauth.utility.JWT;
import no.hvl.dat152.obl3.idp.oauth.utility.JWTHandler;
import no.hvl.dat152.obl3.idp.oauth.utility.OpenIDUser;
import no.hvl.dat152.obl3.idp.oauth.utility.TokenID;
import no.hvl.dat152.obl3.idp.oauth.utility.UserClaimHelper;
import no.hvl.dat152.obl3.idp.oauth.utility.UserClaims;
import no.hvl.dat152.obl3.util.Crypto;
import no.hvl.dat152.obl3.util.Role;
import no.hvl.dat152.obl3.util.TokenSingleton;

/**
 * Servlet implementation class Token
 */
@WebServlet("/token")
public class Token extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Token() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		

		String grantType = request.getParameter("grant_type");

		try {
			// check that the grant_type = authorization_code
			if(grantType.equals("authorization_code")) {
				
				authorizationCodeRequest(request, response);
				
			} else if(grantType.equals("refresh_token")){
				
				refreshAccessTokenRequest(request, response);
				
			} else {
				PrintWriter out = response.getWriter();
				out.write("Unauthorized!\nYou must specify the grant_type parameter in your request");
				out.flush();
			}

		} catch(Exception e) {
			e.printStackTrace();
			PrintWriter out = response.getWriter();
			out.write("Unauthorized! \nYou must login to the IdP using the clientID provided by the IdP (DAT152WebSearch)");
			out.flush();
		}
		
	}
	
	private void authorizationCodeRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// validate client using the client_ID (No client_secret here)
		String header = request.getHeader("Authorization");
		header = header.split(" ")[1].trim();
		String clientID = new String(Base64.getUrlDecoder().decode(header));
		
		AppUserDAO appUser = new AppUserDAO();
		
		TokenSingleton token = TokenSingleton.Instance();
		String redirectURI = request.getParameter("redirect_uri");
		
		if(appUser.clientIDExist(clientID.trim())) {
			
			// validate authorization code
			String code = request.getParameter("code");
			
			try {
				if(Crypto.verifyAuthorizationCode(clientID, code)) {
					
					// does the client request some other claims?
					UserClaims ucd = UserClaimHelper.getUserClaimObject(clientID);
					
					String webpath = getServletContext().getRealPath("WEB-INF/");
					
					// generate authentication token (id_token) - TODO (Incomplete)
					JWT jwt = new JWT();
					jwt.setIss(Constants.ISSUER_IDP);
					jwt.setSub(redirectURI);
					jwt.setAud(clientID);
					jwt.setIat(new Date());

					// add a custom username 
					if(ucd != null) {
						jwt.addCustomClaims("username", ucd.getGiven_name());
					}
					// add a custom role (admin or user)
					String role = token.getRoles().get(clientID);
					
					if(role.equals(Role.ADMIN.name()))
						jwt.addCustomClaims("role", Role.ADMIN.toString());
					else
						jwt.addCustomClaims("role", Role.USER.name());
					String id_token = JWTHandler.createJWTAsymmetricKey(jwt, webpath);		// JWS = Signed JWT (asymmetric key)
					
					// generate refresh token (refresh_token)
					String refreshToken = Crypto.generateRandomCryptoCode();
					token.addRefreshToken(refreshToken);
					
					// generate JWT access token (access_token) - TODO (Incomplete)
					jwt = new JWT();
					jwt.setIss(Constants.ISSUER_IDP);
					jwt.setSub(redirectURI);
					jwt.setAud(clientID);
					jwt.setIat(new Date());

					String access_token = JWTHandler.createJWTAsymmetricKey(jwt, webpath);		// JWS = Signed JWT (asymmetric key)
					
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.setHeader("Cache-Control", "no-store");
					response.setHeader("Pragma", "no-cache");
					
					TokenID tokens = new TokenID();
					tokens.setId_token(id_token);
					tokens.setAccess_token(access_token);
					tokens.setRefresh_token(refreshToken);
					tokens.setToken_type("Bearer");						
				
					if(ucd != null) {
						tokens.setUserClaims(ucd);
					}

					Gson gson = new Gson();
					String claims_json = gson.toJson(tokens);
					
					PrintWriter out = response.getWriter();
					out.write(claims_json);
					out.flush();						
				} else {
					PrintWriter out = response.getWriter();
					out.write("Unauthorized! Authorization code is invalid!");
					out.flush();
				}
			} catch(Exception e) {
				PrintWriter out = response.getWriter();
				out.write("Unauthorized! Authorization verification failed!");
				out.flush();
			}

		} else {
			PrintWriter out = response.getWriter();
			out.write("Unauthorized! Can't find client!");
			out.flush();
		}
	}

	private void refreshAccessTokenRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String clientId = request.getParameter(Constants.CLIENT_ID);
		String refresh_token = request.getParameter("refresh_token");

		String webpath = getServletContext().getRealPath("WEB-INF/");

		boolean refresh_token_valid = TokenSingleton.Instance().refreshTokenExist(refresh_token.trim());
		if(refresh_token_valid) {

			// generate a new access_token
			OpenIDUser ui = TokenSingleton.Instance().getOpenIDUser(clientId);
			JWT jwt = new JWT();
			jwt.setIss(Constants.ISSUER_IDP);
			jwt.setSub(ui.getRedirectURI());
			jwt.setAud(clientId);
			jwt.setIat(new Date());
			
			String access_token = JWTHandler.createJWTAsymmetricKey(jwt, webpath);		// JWS = Signed JWT (asymmetric key)
		
			TokenID claims = new TokenID();
			claims.setRefresh_token(refresh_token);
			claims.setAccess_token(access_token);
			claims.setToken_type("Bearer");	
			Gson gson = new Gson();
			String claims_json = gson.toJson(claims);
			
			PrintWriter out = response.getWriter();
			out.write(claims_json);
			out.flush();
		} else {
			PrintWriter out = response.getWriter();
			out.write("Unauthorized! \nRefresh token is invalid!");
			out.flush();
		}
		
	}

}
