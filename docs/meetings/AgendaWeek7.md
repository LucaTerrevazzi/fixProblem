# Agenda CSEP Meeting Week 7 — Group 71
**Friday, 9 January 2026 at 14:45**

| Name                         | Role          | P   | A   |
|------------------------------|---------------|-----|-----|
| Daphne Charaki               |               | [ ] | [ ] |
| Doğa Gürer                   |               | [ ] | [ ] |
| Karsten van den Heuvel       | Minute Taker? | [ ] | [ ] |
| Bo Li                        | Chair         | [ ] | [ ] |
| Luca Terrevazzi              |               | [ ] | [ ] |
| Melchior Besançon            |               | [ ] | [ ] |


---

## Agenda

### Opening & Setup (1 - 2 minutes)
- Opening 
- Agenda overview 
- Quick review of previous minute taker notes 
- Reminder of formative "implemented features" grade this week

### Updates & Progress Review (5 minutes)
 Potentially shippable product check
    -  Demo of current application state

  **Demo Items**

  | Feature / Component         | Demo Status       | Notes                                      |
  |-----------------------------|-------------------|--------------------------------------------|
  | UI Scenes                   | Almost complete   | Scene switching works, missing some scenes |
  | Recipe creation and editing | TBD               | Needs more testing                         |
  | Recipe deletion             | TBD               |                                            |
  | Ingredient creation         | TBD               |                                            |
        - What elements are missing for it to be considered shippable? 
        (2 mins)

## TA Feedback (2 - 4 minutes)
- TA feedback on demo
- TA announcements or questions
- Team questions for TA

### Tasks & Planning – What We Need to Fix (10 minutes)

### Priority
1. Create the scene for ingredient overview
2. Create the scene for adding recipe
3. Deleting recipe button (non-clickable if no recipe selected)
4. Edit button for recipe ( either on the recipe overview scene OR in a window identical to creating a new recipe) that sends changes to the server.
5. Fixing cloning based on backlog
6. Test printable recipe (as in seeing if it works like the backlog says)
7. Make already existing language code into ENUM

### Based on "Technology" feedback:

| Component/Class      | Problem                                                             | Solution                                       |
|----------------------|---------------------------------------------------------------------|------------------------------------------------|
| ServerUtils (Client) | New client created for each method                                  | Injecting the client with dependency injection |
| Utils (Client)       | "Util" classes acting like services implemented as static functions |                                                |
| Controllers          | both field injection and constructor injection are used             |                                                |

### Other

1. Implement all children issues of recipeController
2. Implement all children issues of IngredientController
3. Testing all 3 repositories  
4. Testing the controllers
5. Make a file with all improvements we need


### Discussion of basic requirements missing
- What is left missing? (4 mins)
    - Refresh button logic (client side)
    - Frontend UI
    - Client-Server integration (connecting UI to backend)
    - Cloning a recipe
    - Final version of backend (to be finished this week)

- Finish basic requirements and tests (2 min)


### Planning & Task Distribution (5 - 10 minutes)
- Review of Task distribution 
- Plans for next week


### Team Collaboration & Inclusion (2+ minutes)
#### Ensuring everyone is heard (quick round)
- Any extra discussion point?
- Any disagreements?

### Buffer Topics (if time allows)
###### The topics will be discussed in the next TA-less meeting if not touched upon in the meeting.
- Use of WebSockets.
- Testing of HTTP requests.
- Use of CSS to style the application.

### Closing (5 minutes)
- Recap of decisions and tasks
- Plan next TA-less meeting / role division 
- Final TA questions or announcements 
- Anything else? 


---
---