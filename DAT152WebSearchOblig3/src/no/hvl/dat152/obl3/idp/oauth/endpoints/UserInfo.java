package no.hvl.dat152.obl3.idp.oauth.endpoints;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import no.hvl.dat152.obl3.idp.oauth.utility.JWTHandler;
import no.hvl.dat152.obl3.idp.oauth.utility.UserClaimHelper;
import no.hvl.dat152.obl3.idp.oauth.utility.UserClaims;

/**
 * Servlet implementation class UserInfo
 */
@WebServlet("/userinfo")
public class UserInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// build the user info based on the scope
		
		/** OpenID specification
		 * <!-- scope: 
		 * openid -> OpenID authentication and ID token
		 * email {email, email_verified},
		 * phone {phone_number, phone_number_verified},
		 * profile {name, family_name, given_name, middle_name, nickname, profile, picture, website, gender, birthdate, etc},
		 * address {address} 
		**/
		String auth_header = request.getHeader("Authorization");
		String[] header = auth_header.split(" ");
		String access_token = "";
		if(header[0].equals("Bearer")) {
			access_token = header[1].trim();
		}
		
		//String keypath = getServletContext().getRealPath("WEB-INF/");

		String clientId = JWTHandler.getClientIdFromJson(access_token.trim());

		if(!clientId.isEmpty()) {		// TODO 
			// 
			UserClaims ucd = UserClaimHelper.getUserClaimObject(clientId);
			Gson gson = new Gson();
			String user_claims = gson.toJson(ucd);
			
			PrintWriter out = response.getWriter();
			out.write(user_claims);
			out.flush();
		} else {
			response.sendError(401, "Unauthorized");
			
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
