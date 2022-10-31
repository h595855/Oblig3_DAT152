package no.hvl.dat152.obl3.blog.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.hvl.dat152.obl3.blog.Constants;
import no.hvl.dat152.obl3.blog.util.Util;

/**
 * Servlet implementation class BlogServlet
 */
@WebServlet("/blogservlet")
public class BlogServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BlogServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String fpath = getServletContext().getRealPath("/WEB-INF/blogdb.txt");	/* file for storing comments */
		
		String newtoken = request.getParameter("newtoken");  /* check that request is coming from callback */
		if(newtoken !=null) {
			if(newtoken.equals("yes")){	
				String id_token = request.getSession().getAttribute("id_token").toString();
				// save token in cookie
				Cookie tokencookie = new Cookie("id_token", id_token);
				tokencookie.setMaxAge(1000000);
				response.addCookie(tokencookie);

				RequestHelper.doJWT(request, id_token);
				// load previous comments
				doComments(request, fpath);
				response.sendRedirect("blogview.jsp");
			}
		} else {		// otherwise, the request is from index.jsp
		
			boolean validSession = RequestHelper.isLoggedIn(request);
			
			if(validSession){
				// load previous comments
				doComments(request, fpath);
				response.sendRedirect("blogview.jsp");
			} else {
				request.setAttribute("message", "Session timed out or invalid SSO auth token");
				request.getRequestDispatcher("login").forward(request, response);
			}
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		boolean validSession = RequestHelper.isLoggedIn(request);
		//System.out.println(validSession+"|"+validSSOSession);
		if(validSession) {			
			processRequest(request, response);						
		} else {
			String pubkeypath = getServletContext().getRealPath("/WEB-INF/");
			boolean validSSOSession = RequestHelper.isLoggedInSSO(request, pubkeypath);		
			if(validSSOSession) {
				processRequest(request, response);
			}  else {
				request.setAttribute("state", Constants.STATE);
				request.setAttribute("redirect_url", Constants.SP_CALLBACK_ADDRESS);
				request.setAttribute("message", "Session timed out or invalid SSO auth token");
				request.getRequestDispatcher("login").forward(request, response);
			}
		}

	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			String fpath = getServletContext().getRealPath("/WEB-INF/blogdb.txt");
			
			String button = request.getParameter("submit");
			
			if(button.equals("Delete Comments")){
					Util.deleteComments(fpath);
			} else if(button.equals("Post Comment")) {

				String comment = request.getParameter("comment");
				if(comment != null){
					String user = request.getSession().getAttribute("loggeduser").toString();
					Util.saveComments(fpath, comment, user);				
				}
			}
			
			doComments(request, fpath);
			
			response.sendRedirect("blogview.jsp");
		}catch(Exception e) {
			request.setAttribute("state", Constants.STATE);
			request.setAttribute("redirect_url", Constants.SP_CALLBACK_ADDRESS);
			request.setAttribute("message", "Session timed out or invalid SSO auth token");
			request.getRequestDispatcher("login").forward(request, response);
		}

	}
	
	private void doComments(HttpServletRequest request, String fpath) {
		List<String> comments = Util.getComments(fpath);
		request.getSession().setAttribute("comments", comments);
	}

}
