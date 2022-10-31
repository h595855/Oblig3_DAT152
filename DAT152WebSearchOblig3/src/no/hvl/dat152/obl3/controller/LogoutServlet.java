package no.hvl.dat152.obl3.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.hvl.dat152.obl3.util.TokenSingleton;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 
		String client_id = request.getParameter("client_id");
		if(client_id != null) {
			String redirect_uri = request.getParameter("redirect_uri");
			if(RequestHelper.isLoggedInSSO(request, client_id)) {
				removeClientTokenData(client_id);
				request.getSession().invalidate();
				response.sendRedirect(redirect_uri+"?loggedout=success");
			} else {
				response.sendRedirect(redirect_uri+"?loggedout=nossologin");
			}
		} else {
			request.getSession().invalidate();
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		
	}
	
	private void removeClientTokenData(String client_id) {
		
		TokenSingleton token = TokenSingleton.Instance();
		token.deleteAuthCode(client_id);
		token.deleteConsents(client_id);
		token.deleteOpenIDUser(client_id);
		token.deleteRole(client_id);
		token.deleteUserPhones(client_id);
	}

}
