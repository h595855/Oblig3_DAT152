package no.hvl.dat152.obl3.idp.oauth.endpoints;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.hvl.dat152.obl3.controller.RequestHelper;
import no.hvl.dat152.obl3.idp.oauth.utility.Constants;
import no.hvl.dat152.obl3.idp.oauth.utility.OpenIDUser;
import no.hvl.dat152.obl3.util.TokenSingleton;

/**
 * Servlet implementation class Authorize
 */
@WebServlet("/authorize")
public class Authorize extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Authorize() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String client_id = request.getParameter(Constants.CLIENT_ID);
		OpenIDUser oidcu = (OpenIDUser) request.getSession().getAttribute(client_id);
		if(oidcu != null) {
			List<String> consents = oidcu.getConsents();
			request.getSession().setAttribute("consents", consents);
			
			// get the request and forward to obtain user consent
			request.getRequestDispatcher("userconsent.jsp").forward(request, response);
		} else {
			
			/* cache SSO data before authenticating */
			RequestHelper.cacheSSOData(request, client_id);
			request.getSession().invalidate();		// invalidate the user using the web browser for this same service...
			request.getRequestDispatcher("login.jsp").forward(request, response);
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
