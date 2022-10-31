package no.hvl.dat152.obl3.idp.oauth.utility;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.hvl.dat152.obl3.database.AppUser;
import no.hvl.dat152.obl3.util.Crypto;
import no.hvl.dat152.obl3.util.TokenSingleton;

/**
 * Servlet implementation class AuthorizeHelper
 */
@WebServlet("/authorizehelper")
public class AuthorizeHelper extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AuthorizeHelper() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
//		String client_id = request.getParameter(Constants.CLIENT_ID);
		AppUser user = (AppUser) request.getSession().getAttribute(Constants.USER);
		OpenIDUser ui = (OpenIDUser) request.getSession().getAttribute(user.getClientID());
		
		String consentinfo = "{";
		
		String profile = request.getParameter("profile");
		String phone = request.getParameter("phone");

		if(profile != null && phone != null) {
			consentinfo += "\"sub\" : \""+user.getClientID()+"\","+"\"name\" : \""+user.getFirstname()+" "+user.getLastname()+"\","
					+"\"family_name\" : \""+user.getLastname()+"\","+"\"given_name\" : \""+user.getFirstname()+"\",";
			consentinfo += "\"phone_number\" : \""+user.getMobilephone()+"\"}";
			
			// persist userinfo				
			// generate authorization code
			String code = Crypto.authorizationCode(user.getClientID());	// generate and store in a map
			
			TokenSingleton.Instance().addConsents(user.getClientID(), consentinfo);
			
			// collect the client callback (redirect_uri) and construct the redirect_uri
			String redirect_url = ui.getRedirectURI()+"?code="+code+"&state="+ui.getState();
			
			// redirect -> forward the authorization code to the client via the redirect_uri
			response.setStatus(302);
			response.sendRedirect(redirect_url+"&consent=complete");
			
		} else {
			// log user out
			// clear session data
			request.getSession().invalidate();
			response.sendRedirect(ui.getRedirectURI()+"?consent=incomplete");
		}		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
