package no.hvl.dat152.obl3.idp.oauth.endpoints;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import no.hvl.dat152.obl3.database.AppUserDAO;
import no.hvl.dat152.obl3.util.TokenSingleton;

/**
 * Servlet implementation class Register
 */
@WebServlet("/register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        // TODO Auto-generated constructor stub
    }
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Register a new appuser and return the client_id. We are not using client_secret in this simple scheme
		//String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		
		if(phone != null) {
			AppUserDAO appUser = new AppUserDAO();
			String clientId = appUser.getUserClientID(phone.trim());
			
			// register the clientid against the phone number
			if(clientId != null) {
				//TokenSingleton.Instance().addUserPhones(clientId, phone);
				PrintWriter out = response.getWriter();
				JsonObject jobj = new JsonObject();
				jobj.addProperty("clientId", clientId);
				jobj.addProperty("phone", phone);
				out.write(jobj.toString());
				out.flush();
			} else {
				response.sendError(400, "Error during registration. Check that you enter the correct phone number and try again");
			}

		} else {
			response.sendError(400, "Error during registration - Phone number is null! Enter the correct phone number and try again");
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
