# P3_EJZ360_FOAM
* Edward Zempel
* ICS 425, Fall 2019

## Overview
1. Tested with WildFly 17.0

### Instructions
Run application on Servlet and navigate to FoamServlet

### Test scenarios
1. National ID must be unique. Adding an athlete with an ID already in use tells the user that the id they are using is a duplicate and allows them to edit the form containing their previously entered information.

### Date of Birth

Scenario - Eligibility  - Age displayed

 * Age greater than 16 on day of Olympics | eligible | displays age in years
 * Turns 16 on the day of the Olympics - eligible, displays age of 16
 * Turns 16 the day after the Olympics - ineligible, displays age of 15
 * Born on the day of the olympics - ineligible, age of 0
 * Born after the Olympics - ineligible, no age displayed
 
### delete and update conflicts

* If a user attempts to delete an athlete twice the system reports the id is no longer on the roster
* If a user attempts to edit an athlete that is deleted the system reports the id is no longer on the roster and allows the user to add the current information as a new athelete.