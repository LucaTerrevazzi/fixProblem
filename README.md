# CSEP Template Project

This repository contains the template for the CSE project. Please extend this README.md with sufficient instructions that will illustrate for your TA and the course staff how they can run your project.

To run the template project from the command line, you either need to have [Maven](https://maven.apache.org/install.html) installed on your local system (`mvn`) or you need to use the Maven wrapper (`mvnw`). You can then execute

	mvn -pl server -am spring-boot:run

to run the server and

	mvn -pl client -am javafx:run

to run the client. Please note that the server needs to be running, before you can start the client.

Get the template project running from the command line first to ensure you have the required tools on your sytem.

Once it is working, you can try importing the project into your favorite IDE. Especially the client is a bit more tricky to set up there due to the dependency on a JavaFX SDK.
To help you get started, you can find additional instructions in the corresponding README of the client project.


# How to use the application

Once you run the app, you should start by adding some new ingredients.
Press the button on the top right of the list of recipes to go the the ingredients section (this might look like 2 arrows).
Now press the Add ingredient button in the bottom left of the screen.
Here you can create an ingredient, and specify its nutritional values by typing them in the corresponding boxes.
Press create to finish the ingredient.
Once you have your desired ingredients, navigate back to the recipe menu by using the same button as you used to get to the ingredients.
Press the Add recipe button in the bottom left of the screen.
In this new screen, first enter a name for your recipe in the corresponding text box at the top.
Next up you can click on the dropdown menu for your list of available ingredients you made, specify an amount and in which unit.
Once you are sure you have the right ingredient, you can press the Add button to add it to the list.
If you made a mistake, you can always remove specific ingredients with the buttons below the ingredients section.
To finish of your recipe, write some instructions in the specified textbox and press the button next to it to add them.
Once you have completed your recipe, simply press the create button to finish your masterpiece.
When you are back in the recipe overview, you can look at a recipe by clicking on it.