//this is the package name
package demo.src.main.java.asteroid_app.initial;

// Application is the base class for all JavaFX applications
//need javafx to display the window
import javafx.application.Application;
// Stage is the top level JavaFX container
import javafx.stage.Stage;
// Scene is the container for all content
import javafx.scene.Scene;
// Pane is the base class for all layout panes
import javafx.scene.layout.Pane;
//import circle to draw a circle
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
//import polygon to draw a polygon
import javafx.scene.shape.Polygon;
// Label for the text inside the window
import javafx.scene.control.Label;
//import java.util.* to create a list for asteroids
import java.util.*;
//import javafx.scene.control.Button to display button
import javafx.scene.control.Button;
//import VBox to manage the nodes in a pane
import javafx.scene.layout.VBox;
//import Font to set the size of labels
import javafx.scene.text.Font;
//import Fontweight to set the style of labels and buttons
import javafx.scene.text.FontWeight;
//import BorderPanet to set the position of the nodes
import javafx.scene.layout.BorderPane;
import javafx.geometry.Pos;

//Stuff for keypresses
import java.util.HashMap;
import javafx.scene.input.KeyCode;
import java.util.Map;
import javafx.animation.AnimationTimer;

//window extends the application class from javafx
public class window extends Application {

    // define the size of the screen can be accessed by all classes
    public static int WIDTH = 800;
    public static int HEIGHT = 600;
    // the window class overrides the start mehtod from the application class
    // takes a single parameter of type stage
    // inside the start method is where the User interface is created

    @Override
    public void start(Stage stage) throws Exception {
        // create a pane and set size
        Pane pane = new Pane();

        pane.setPrefSize(WIDTH, HEIGHT);
        // create a scene and label

        Scene scene = new Scene(pane);
        Label label = new Label("This is how text is added to the screen.");
        pane.getChildren().add(label);

        // Object creation:
        // create the characters

        // Asteroid
        // At the beginning, use a list to create several asteroid
        List<Asteroid> asteroids = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            double x = new Random().nextDouble() * 1000;
            double y = new Random().nextDouble() * 1000;
            Asteroid asteroid = new Asteroid(x, y, 3);
            asteroids.add(asteroid);
        }

        // Add these asteroids to Pane
        asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getChar()));

        // as the level increase, add a for loop to increase asteroid
        // Polygon asteroid = Asteroid.createAsteroid();
        // pane.getChildren().add(asteroid);

        // Circle
        // create circle location from top left is 300x 200y and radius is 50
        Circle circle = new Circle(100, 100, 30);
        pane.getChildren().add(circle);

        // Alien
        Alien alien_ship = new Alien(200, 300);// test to see where to put it
        alien_ship.createAlienShip(200, 300);
        pane.getChildren().add(alien_ship.getChar());

        // Ship
        // create a user_ship object and initialize location
        User_ship ship = new User_ship(WIDTH / 2, HEIGHT / 2);
        // add the user_ship to the pane
        pane.getChildren().add(ship.getChar());

        // Start Menu
        // set a border pane to ocntain and set the position of nodes
        BorderPane borderPane = new BorderPane();

        // create a new scene
        Scene startMenuScene = new Scene(borderPane, WIDTH, HEIGHT);

        // labels on the screen
        Label headline = new Label("ASTEROIDS");
        headline.setFont(Font.font("Monospaced", FontWeight.BOLD, 100));
        Button playGame = new Button("PLAY GAME");
        Button highScores = new Button("HIGH SCORES");
        Label website = new Label("www.freevideogamesonline.com");

        // create a Vbox to manage the nodes on the start menu
        VBox vBox = new VBox(30, headline, playGame, highScores, website);

        // set the position of vBox
        vBox.setAlignment(Pos.CENTER);

        // add vBox into root
        borderPane.setCenter(vBox);

        // set the title of the window
        stage.setTitle("Group 11-Asteroids Game");
        stage.setScene(startMenuScene);

        // when click on play game button, enter the game play scene
        playGame.setOnAction(e -> stage.setScene(scene));

        // display the stage
        stage.show();

        // Key Presses:
        // create a hash map(key value pairs stored in a hash table) to store the key
        // presses
        Map<KeyCode, Boolean> key_press = new HashMap<>();
        // add key pressed handler
        scene.setOnKeyPressed(event -> {
            key_press.put(event.getCode(), Boolean.TRUE);
        });
        // add key release handler
        scene.setOnKeyReleased(event -> {
            key_press.put(event.getCode(), Boolean.FALSE);
        });

        // Animation controls:
        // use an animation timer to update the screen
        new AnimationTimer() {
            // check if j key was pressed so we dont repeatedly go into hyperspace
            // inserted here to prevent multiple jumps
            private boolean jPress = false;

            @Override
            public void handle(long now) {
                // if the left key is pressed
                if (key_press.getOrDefault(KeyCode.LEFT, false)) {
                    // rotate the user_ship left
                    ship.turnLeft();
                }
                // if the right key is pressed
                if (key_press.getOrDefault(KeyCode.RIGHT, false)) {
                    // rotate the user_ship right
                    ship.turnRight();
                }

                // if the up key is pressed
                if (key_press.getOrDefault(KeyCode.UP, false)) {
                    // accelerate the user_ship
                    ship.accelerate();
                }

                // if the J key is pressed for jump and has not already jumped
                if (key_press.getOrDefault(KeyCode.J, false) && jPress == false) {
                    // jump to a new location and if successful set flag to true
                    ship.hyperspaceJump();
                    jPress = true;
                }
                // if the J key is released
                if (!key_press.getOrDefault(KeyCode.J, false)) {
                    jPress = false; // reset the flag
                }
                // update the ship's movement
                ship.move();
                alien_ship.move();

                // Move the asteriods
                asteroids.forEach(asteroid -> asteroid.move());

                // When the collision happens
                asteroids.forEach(asteroid -> {
                    if (asteroid.collision(ship)) {

                        // Remove the collided asteroid from the pane and asteroids list when collision
                        // happens
                        pane.getChildren().remove(asteroid.getChar());
                        asteroids.remove(asteroid);

                        // Then add two new smaller asteroids on the scene and in the asteroids list
                        // If the size=3 asteroid is collided
                        if (asteroid.getInitialSize() == 3) {
                            for (int i = 0; i < 2; i++) {
                                Asteroid newAsteroid = new Asteroid(asteroid.getChar().getTranslateX(),
                                        asteroid.getChar().getTranslateY(), 2);
                                asteroids.add(newAsteroid);
                                pane.getChildren().add(newAsteroid.getChar());
                                newAsteroid.move();
                            }
                        }

                        // If the size=2 asteroid is collided:
                        if (asteroid.getInitialSize() == 2) {
                            for (int i = 0; i < 2; i++) {
                                Asteroid newAsteroid = new Asteroid(asteroid.getChar().getTranslateX(),
                                        asteroid.getChar().getTranslateY(), 1);
                                asteroids.add(newAsteroid);
                                pane.getChildren().add(newAsteroid.getChar());
                                newAsteroid.move();
                            }
                        }
                    }
                });

            }
        }.start();
    }

    // run the application
    public static void main(String[] args) {
        launch(args);
    }
}
