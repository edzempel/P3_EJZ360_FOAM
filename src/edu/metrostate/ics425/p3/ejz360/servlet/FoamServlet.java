package edu.metrostate.ics425.p3.ejz360.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.metrostate.ics425.foam.data.Roster;
import edu.metrostate.ics425.foam.data.RosterException;
import edu.metrostate.ics425.p3.ejz360.model.AthleteBean;

/**
 * Servlet implementation class FoamServlet
 * 
 * @author ezempel
 */
@WebServlet({ "/FoamServlet", "/view", "/add", "/edit", "/delete" })
public class FoamServlet extends HttpServlet {
	private static final long serialVersionUID = 20191002L;

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

		var sc = getServletContext();
		String url = "index.jsp";
		Roster rosterDB = (Roster) sc.getAttribute("rosterDB");

		// get current action
		String action = request.getParameter("action");
//		String action = request.getRequestURL().
		if (action == null) {
			action = "view";
		}

		if (action.equals("view")) {
			String welcome = "Welcome to the Freedonia Olympic" + "Athlete Management System (FOAMS).";
			request.setAttribute("welcome", welcome);

		} else if (action.equals("create-new")) {
			HashMap<String, String> errList = new HashMap<String, String>();
			try {
				String newId = request.getParameter("newId");
				String newLast = request.getParameter("newLast");
				String newFirst = request.getParameter("newFirst");
				String newDobString = request.getParameter("newDob");

				LocalDate newDob = newDobString.isBlank() ? null : LocalDate.parse(newDobString);
				AthleteBean newAthlete = createAthlete(newId, newLast, newFirst, newDob);
				if (newDob != null && newDob.isBefore(LocalDate.parse("1900-01-01"))) {
					request.setAttribute("errDob", true);
					request.setAttribute("feedbackDob", "invalid-feedback");
					request.setAttribute("feedbackDobMessage", "The date of birth must be after 1900-01-01.");
					errList.put("errDob", String.format("%s is invalid. The date of birth must be after 1900-01-01.",
							newDob.toString()));
				} else {
					request.setAttribute("feedbackDob", "valid-feedback");
					request.setAttribute("feedbackDobMessage", "Looks good!");
				}

				boolean added = rosterDB.add(newAthlete);
				if (!added)
					errList.put("DupId", String.format("%s is a duplicate id.\n Cannot add: %s.", newId, newAthlete));

			} catch (RosterException e) {
				e.printStackTrace();
				errList.put("errRoster",
						String.format("Unable to add athlete: %s. ", request.getParameter("newId")) + e.getMessage());
			} catch (Exception ex) {
				errList.put("errMsg", String.format("%s.", ex.getMessage()));
			} finally {
				if (!errList.isEmpty()) {
					request.setAttribute("errMsg", errList);
					url = "/add.jsp";
				} else {
					url = "/index.jsp";
				}

			}
		} else {
			request.setAttribute("errMsg", "Invalid action");
			url = "/index.jsp";
		}

		// Send lists of all athletes to the view
		try {
			request.setAttribute("roster", rosterDB.findAll());
		} catch (RosterException e) {
			e.printStackTrace();
			request.setAttribute("errMsg", "Unable to send roster to browser. " + e.getMessage());
		}

		// Forward control to the view
		request.getRequestDispatcher(url).forward(request, response);
	}

	@Override
	public void init() throws ServletException {
		super.init();
		var sc = getServletContext();
		String virtualPath = sc.getInitParameter("filePath");
		String realPath = sc.getRealPath(virtualPath);

		if (realPath != null) {
			Roster.initialize(realPath);

			try {
				Roster rosterDB = Roster.getInstance();
				sc.setAttribute("rosterDB", rosterDB);
				sc.setAttribute("roster", rosterDB.findAll());
			} catch (RosterException e) {
				System.out.println("Unable to create roster. " + e.getMessage());
				e.printStackTrace();
			}
		}

	}

	private AthleteBean createAthlete(String newID, String newLast, String newFirst, LocalDate newDob) {
		AthleteBean ab = new AthleteBean();
		ab.setNationalID(newID);
		ab.setLastName(newLast);
		ab.setFirstName(newFirst);
		ab.setDateOfBirth(newDob);
		return ab;
	}

}
