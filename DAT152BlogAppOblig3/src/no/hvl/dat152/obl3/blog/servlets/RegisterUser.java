package no.hvl.dat152.obl3.blog.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.hvl.dat152.obl3.blog.database.User;
import no.hvl.dat152.obl3.blog.database.UserXMLDbLogic;

/**
 * Servlet implementation class RegisterUser
 */
@WebServlet("/register")
public class RegisterUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = request.getParameter("username");
		String pwd = request.getParameter("password");
		String phone = request.getParameter("phone");
		
		if(!username.equals("") && !pwd.equals("")) {		
			/**
			 * use xml file located on the server to store users 
			 */
			String dbpath = getServletContext().getRealPath("WEB-INF/usersdb.xml");

			UserXMLDbLogic xmldb = new UserXMLDbLogic(dbpath);
			boolean success = xmldb.registerNewUserWithSalt(username, pwd, User.USER, phone);
			
			if(success) {
				request.setAttribute("message", "Registration successful. You can now login with your credentials");
				request.getRequestDispatcher("index.jsp").forward(request, response);

			}else {
				request.setAttribute("message", "Oops! something went wrong. Try to register again, e.g. with a new username");
				request.getRequestDispatcher("index.jsp").forward(request, response);
			}
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
