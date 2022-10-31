package no.hvl.dat152.obl3.blog;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import no.hvl.dat152.obl3.blog.tokens.TokenID;

/**
 * Servlet implementation class Callback
 */
@WebServlet("/callback")
public class Callback extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Callback() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String loggedout = request.getParameter("loggedout");
		//String loggedin = request.getParameter("loggedin");
		String consent = request.getParameter("consent");
		
		if(loggedout != null) {		/* This is from IdP logout endpoint*/
			if(loggedout.equals("success")) {
				
				request.setAttribute("message", "You are now logged out from SSO");
				request.getRequestDispatcher("login").forward(request, response);
				
			} else if(loggedout.equals("nossologin")) {
				// forward the user request back to the blog controller
				request.setAttribute("message", "You are not logged in with SSO.");
				request.getRequestDispatcher("blogservlet").forward(request, response);
			}			
		} else if(consent != null) { /* This is from IdP consent request*/
			
			if(consent.equals("incomplete")){
				request.setAttribute("message", "Consent not granted to IdP. \nYou can use your blogapp credentials to login");
				request.getRequestDispatcher("login").forward(request, response);
			} else if(consent.equals("complete")){
				String idp_response = "";
				try {
					
					String code = request.getParameter("code");
					//String state = request.getParameter("state");		// not used here!
					
					// use the authorization_code to request for authentication token (JWT)
					// Authorization header contains the client_id and the client_secret (optional)
					
					String token_endpoint_url_data = "grant_type=authorization_code&code="+code+"&redirect_uri="+Constants.SP_CALLBACK_ADDRESS;

					// we will use a direct back channel to submit the request to the IdP			
					HttpClient httpChannel = new HttpClient(Constants.IDP_TOKEN_ENDPOINT);
					
					idp_response = httpChannel.requestToken(Constants.CLIENT_ID, token_endpoint_url_data);
					
					TokenID token = getTokenID(idp_response, request, response);
					
					if(token == null)
						return;
					
					request.getSession().setAttribute("id_token", token.getId_token());
					
					// redirect the user to the blog controller upon successful checks.
					request.getRequestDispatcher("blogservlet?newtoken=yes").forward(request, response);
					
				}catch(Exception e) {
					request.setAttribute("message", "\nSSO login failed!\n"+idp_response);
					request.getRequestDispatcher("login").forward(request, response);
				}
			}
		}
			
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}
	
	private TokenID getTokenID(String idp_response, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		TokenID token = null;
		try {
			Gson gson = new Gson();
			token = gson.fromJson(idp_response.trim(), TokenID.class);
		} catch(Exception e) {
			request.setAttribute("message",idp_response+".\nBad response from authorization server");
			request.getRequestDispatcher("login").forward(request, response);
		}
		
		return token;
	}

}
