package no.hvl.dat152.obl3.blog.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.hvl.dat152.obl3.blog.Constants;

/**
 * Servlet implementation class SSOForwarder
 */
@WebServlet("/sso")
public class SSOForwarder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SSOForwarder() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String clientId = request.getParameter("client_id");
		Constants.CLIENT_ID = clientId;
		
		String scope = request.getParameter("scope");
		String response_type = request.getParameter("response_type");
		String state = request.getParameter("state");
		String redirect_uri = request.getParameter("redirect_uri");
		
		String idp_endpoint = Constants.IDP_AUTH_ENDPOINT;
		String ssourl = idp_endpoint+"?client_id="+clientId+"&scope="+scope+"&response_type="+response_type+"&state="+state+
				"&redirect_uri="+redirect_uri;

		response.sendRedirect(ssourl);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
