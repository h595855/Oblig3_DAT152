package no.hvl.dat152.obl3.controller;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.hvl.dat152.obl3.database.AppUser;
import no.hvl.dat152.obl3.database.AppUserDAO;
import no.hvl.dat152.obl3.util.Crypto;
import no.hvl.dat152.obl3.util.Role;
import no.hvl.dat152.obl3.util.ServerConfig;
import no.hvl.dat152.obl3.util.Validator;

@WebServlet("/newuser")
public class NewUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("dictconfig", ServerConfig.DEFAULT_DICT_URL);
		request.getRequestDispatcher("newuser.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		boolean successfulRegistration = false;

		String username = Validator.validString(request.getParameter("username"));
		String password = Validator.validString(request.getParameter("password"));
		String confirmedPassword = Validator.validString(request.getParameter("confirm_password"));
		String firstName = Validator.validString(request.getParameter("first_name"));
		String lastName = Validator.validString(request.getParameter("last_name"));
		String mobilePhone = Validator.validString(request.getParameter("mobile_phone"));
		String preferredDict = Validator.validString(request.getParameter("dicturl"));

		AppUser user = null;

		if (password.equals(confirmedPassword) && PassordValidering(password)) {
			AppUserDAO userDAO = new AppUserDAO();

			user = new AppUser(username, Crypto.generateMD5Hash(password), firstName, lastName, mobilePhone,
					Role.USER.toString(), Crypto.generateRandomCryptoCode());
			if (ValidateUser(user)) {
				successfulRegistration = userDAO.saveUser(user);

				if (successfulRegistration) {
					request.getSession().setAttribute("user", user);
					Cookie dicturlCookie = new Cookie("dicturl", preferredDict);
					dicturlCookie.setMaxAge(60 * 10);
					response.addCookie(dicturlCookie);

					response.sendRedirect("searchpage");
				}

			} else {
				request.setAttribute("message", "Registration failed!");
				request.getRequestDispatcher("newuser.jsp").forward(request, response);
			}
		} else {
			request.setAttribute("message",
					"Password Must have at least one numeric character\r\n"
							+ "Must have at least one lowercase character\r\n"
							+ "Must have at least one uppercase character\r\n"
							+ "Must have at least one special symbol among @#$%\r\n"
							+ "Password length should be between 8 and 20");
			request.getRequestDispatcher("newuser.jsp").forward(request, response);
		}
	}

	public static boolean ValidateUser(AppUser appuser) {
		Pattern pattern = Pattern.compile("^[A-Za-z0-9]+$");
		Matcher UsernameMatch = pattern.matcher(appuser.getUsername());
		Matcher FirstnameMatch = pattern.matcher(appuser.getFirstname());
		Matcher LastnameMatch = pattern.matcher(appuser.getLastname());
		Matcher MobileMatch = pattern.matcher(appuser.getMobilephone());

		return (UsernameMatch.matches() && FirstnameMatch.matches() && LastnameMatch.matches()
				&& MobileMatch.matches());
	}

	public static boolean PassordValidering(String password) {
		String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher passwordMatch = pattern.matcher(password);
		return passwordMatch.matches();
	}

}
