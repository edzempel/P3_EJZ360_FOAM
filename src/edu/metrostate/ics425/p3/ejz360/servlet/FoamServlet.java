package edu.metrostate.ics425.p3.ejz360.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
		request.getRequestDispatcher("/error-404.jsp").forward(request, response);
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
		String welcome = "Welcome to the Freedonia Olympic" + "Athlete Management System (FOAMS).";
		request.setAttribute("welcome", welcome);

		// get current action
		String action = request.getParameter("action");
		if (action == null) {
			action = "view";
		}

		if (action.equals("view")) {

			// Create new Athlete
		} else if (action.equals("create-new")) {
			boolean readyToAdd = false;
			HashMap<String, String> errList = new HashMap<String, String>();
			try {
				String newId = request.getParameter("newId").trim();
				String errId = null;
				String feedbackIdMessage = null;
				if(newId.isBlank()) {
					errId = "true";
					feedbackIdMessage = String.format("Required field");
					errList.put("National Id missing", "required field");
				}
				else if(rosterDB.isOnRoster(newId)) {
					errId = "true";
					feedbackIdMessage = String.format("'%s' is already in roster", newId);
					errList.put("DupId",
							String.format("%s is a duplicate id.", newId));
				} else {
					errId = "false";
				}

				String newLast = request.getParameter("newLast").trim();
				String errLast = null;
				String feedbackLastMessage = null;
				if(newLast.isBlank()) {
					errLast = "true";
					feedbackLastMessage = String.format("Required field");
					errList.put("Last name missing", "required field");
				}else {
					errLast = "false";
				}

				String newFirst = request.getParameter("newFirst").trim();
				String errFirst = null;
				String feedbackFirstMessage = null;
				if(newFirst.isBlank()) {
					errFirst = "true";
					feedbackFirstMessage = String.format("Required field");
					errList.put("First name missing", "required field");
				} else {
					errFirst = "false";
				}

				String newDobString = request.getParameter("newDob");
				String errDob = null;
				String feedbackDobMessage = null;

				LocalDate newDob = null;
				if (newDobString.isBlank()) {
					newDob = null;
				} else {
					try {
						newDob = LocalDate.parse(newDobString);
						if (newDob != null && newDob.isBefore(LocalDate.parse("1900-01-01"))) {
							errDob = "true";
							feedbackDobMessage = "The date of birth must be after 1900-01-01.";
							errList.put("Date of birth out of range", String.format(
									"'%s' is out of range. The date of birth must be after 1900-01-01.", newDobString));
						} else if (newDob == null) {
							errDob = null;
						} else {
							errDob = "false";
							feedbackDobMessage = "Looks good!";
						}
					} catch (DateTimeParseException dtpex) {
						errDob = "true";
						feedbackDobMessage = String.format("'%s' is an invalid date.", newDobString);
						errList.put("Date format error",
								String.format("'%s' must be in yyyy-MM-dd format.", request.getParameter("newDob")));
					}
				}
				
				readyToAdd = errList.isEmpty();
				if (readyToAdd) {
					// create new athlete
					AthleteBean newAthlete = createAthlete(newId, newLast, newFirst, newDob);
					// add athlete to roster
					boolean added = rosterDB.add(newAthlete);

					// one last check for duplicate id in case it was added while waiting for user input
					if (!added) {
						errId = "true";
						feedbackIdMessage = String.format("%s is already in roster", newId);
						errList.put("DupId",
								String.format("%s is a duplicate id.\n Cannot add: %s.", newId, newAthlete));
					} else {
						errId = "false";
					}
				}

				// send all other messages to the view
				request.setAttribute("errId", errId);
				request.setAttribute("feedbackIdMessage", feedbackIdMessage);
				request.setAttribute("errFirst", errFirst);
				request.setAttribute("feedbackFirstMessage", feedbackFirstMessage);
				request.setAttribute("errLast", errLast);
				request.setAttribute("feedbackLastMessage", feedbackLastMessage);
				request.setAttribute("errDob", errDob);
				request.setAttribute("feedbackDobMessage", feedbackDobMessage);

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
