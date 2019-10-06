package edu.metrostate.ics425.p3.ejz360.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.metrostate.ics425.p3.ejz360.model.AthleteBean;

/**
 * Servlet implementation class FoamServlet
 * 
 * @author ezempel
 */
@WebServlet({ "/FoamServlet", "/view", "/add", "/edit", "/delete"})
public class FoamServlet extends HttpServlet {
	private static final long serialVersionUID = 20191002L;
	private ArrayList<AthleteBean> roster = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FoamServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		processRequest(request, response);
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		roster = new ArrayList<AthleteBean>();

		var sc = getServletContext();

		// get current action
		String action = request.getParameter("action");
		if (action == null) {
			action = "add";
		}

		if (action == "view") {
			String welcome = "Welcome to the Freedonia Olympic" + "Athlete Management System (FOAMS).";
			request.setAttribute("welcome", welcome);

		} else if (action == "add") {
//			AthleteBean newAthlete = createAthlete(request.getParameter("newID"), request.getParameter("newFirst"),
//					request.getParameter("newDob"), LocalDate.parse(request.getParameter("newLast")));
//			roster.add(newAthlete);
			AthleteBean anotherAthlete = createAthlete("Peter", "Simon", "aaa1111", LocalDate.parse("2000-04-01"));
			roster.add(anotherAthlete);
		} else {
			request.setAttribute("errMsg", "Invalid input");
		}

		request.setAttribute("roster", roster);
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

	private AthleteBean createAthlete(String newID, String newFirst, String newLast, LocalDate newDob) {
		AthleteBean ab = new AthleteBean();
		ab.setFirstName(newFirst);
		ab.setLastName(newLast);
		ab.setNationalID(newID);
		ab.setDateOfBirth(newDob);
		return ab;
	}

}
