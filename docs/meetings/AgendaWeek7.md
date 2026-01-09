# Agenda CSEP Meeting Week 7 — Group 71
**Friday, 9 January 2026 at 14:45**

| Name                         | Role         |
|------------------------------|--------------|
| Daphne Charaki               |              |
| Doğa Gürer                   |              | 
| Karsten van den Heuvel       | Minute Taker | 
| Bo Li                        | Chair        | 
| Luca Terrevazzi              |              |
| Melchior Besançon            |              |


---

## Agenda

### Opening & Setup (1 - 2 minutes)
- Opening 
  - How was the first week back from the break?
- Agenda overview 
- Quick review of previous minute taker notes 
- Reminder of formative "implemented features" grade this week

---

### Updates & Progress Review (8 - 10 minutes)            

- Review of task distribution 
- Review of what has been done and what is missing from this week

#### Potentially shippable product check

| Feature / Component                | Notes                                                                         |
|------------------------------------|-------------------------------------------------------------------------------|
| UI Scenes                          | Scene switching works, missing some scenes but not part of basic requirements |
| Recipe (creation and deletion)     |                                                                               |
| Ingredient (creation and deleting) | TBD                                                                           |

--- 

## TA Feedback (2 - 4 minutes)
- TA feedback on demo
- TA announcements or questions
- Team questions for TA

---

## Tasks & Planning – What needs to be fixed (15 minutes)

### Priority - what has been done and what is missing from last week
1. Create the scene for ingredient overview
2. Create the scene for adding recipe
3. Deleting recipe button (non-clickable if no recipe is selected)
4. Edit button for recipe (either on the recipe overview scene OR in a window identical to creating a new recipe)
5. Fixing cloning of a recipe based on backlog's description
6. Test printable recipe (does it work as mentioned in the backlog?)
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

- What elements are missing for it to be considered shippable?

---

### Planning & Task Distribution (8 - 10 minutes)
- Plans for next week
- How do we handle resits next week?

---

### Team Collaboration & Inclusion (2+ minutes)
#### Ensuring everyone is heard (quick round)
- Any extra discussion point?
- Any disagreements?

---

### Buffer Topics (if time allows)
- Anything missing in terms of planning?
  - Eg. organizing gitlab
- Going over what additional features still need to be implemented

---

### Closing (5 minutes)
- Recap of decisions and tasks
- Plan next TA-less meeting / role division 
- Final TA questions or announcements 
- Anything else? 

---