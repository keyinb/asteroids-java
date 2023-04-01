//this is the package name
package asteroid_app.initial;
// Application is the base class for all JavaFX applications
//need javafx to display the window
import javafx.application.Application;
// Stage is the top level JavaFX container
import javafx.stage.Stage;
// Scene is the container for all content
import javafx.scene.Scene;
// Pane is the base class for all layout panes
import javafx.scene.layout.Pane;

//import java.util.* to create a list for asteroids
import java.util.*;
//import javafx.scene.control.Button to display button
import javafx.scene.control.Button;

//Stuff for keypresses
import java.util.HashMap;
import javafx.scene.input.KeyCode;
import java.util.Map;
import javafx.animation.AnimationTimer;


//window extends the application class from javafx
public class window extends Application {

    //define the size of the screen can be accessed by all classes
    public static int WIDTH = 1400;
    public static int HEIGHT = 1000;
    //the window class overrides the start mehtod from the application class
    //takes a single parameter of type stage
    //inside the start method is where the User interface is created


    
    @Override
    public void start(Stage stage) throws Exception {
        // create a pane and set size
        Pane pane = new Pane();

        pane.setPrefSize(WIDTH, HEIGHT);
        // create a scene and label

        Scene scene = new Scene(pane);

        // Object creation:
        // create the characters

        // Asteroid
        // At the beginning,  use a list to create several asteroid
	    List<Asteroid> asteroids = new ArrayList<>();
        for(int i=0; i<30; i++){
            double x = new Random().nextDouble()*1000;
            double y = new Random().nextDouble()*1000;
            Asteroid asteroid = new Asteroid(x, y, 3);
            asteroids.add(asteroid);
        }

        // Add these asteroids to Pane
        asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getChar())); 

        // as the level increase, add a for loop to increase asteroid
        // Polygon asteroid = Asteroid.createAsteroid();
        // pane.getChildren().add(asteroid);

        //Alien
        Alien alien_ship=new Alien (200,300);//test to see where to put it
        alien_ship.createAlienShip(200,300);
        pane.getChildren().add(alien_ship.getChar());

        //Ship
        //create a user_ship object and initialize location
        User_ship ship = new User_ship(WIDTH/2, HEIGHT/2);
        //add the user_ship to the pane
        pane.getChildren().add(ship.getChar());

        // Construct a new pane to manage the controls
        Pane startMenuPane = new Pane();
        startMenuPane.setPrefSize(WIDTH, HEIGHT);

        // Construct a new start menu scene
        Scene startMenuScene = new Scene(startMenuPane);

        // Labels on the screen
        // Label headline = new Label("ASTEROIDS");
        Button playGame = new Button("PLAY GAME");
        // Label highScores = new Label("HIGH SCORES");
        // Label website = new Label("www.freevideogamesonline.com");

        // Add labels into startMenuPane
        // startMenuPane.getChildren().add(headline);
        startMenuPane.getChildren().add(playGame);
        // startMenuPane.getChildren().add(highScores);
        // startMenuPane.getChildren().add(website);


        // set the title of the window
        stage.setTitle("Group 11-Asteroids Game");
        stage.setScene(startMenuScene);

        // When click on play game button, enter the game play scene
        playGame.setOnAction(e -> stage.setScene(scene));

        // Display the stage
        stage.show();

        //Key Presses:
        //create a hash map(key value pairs stored in a hash table) to store the key presses
        Map<KeyCode, Boolean> key_press=new HashMap<>();
        //add key pressed handler
        scene.setOnKeyPressed(event -> {key_press.put(event.getCode(), Boolean.TRUE);});
        //add key release handler
        scene.setOnKeyReleased(event -> {key_press.put(event.getCode(), Boolean.FALSE);});

        //Animation controls:
        //use an animation timer to update the screen
        new AnimationTimer(){
            //check if j key was pressed so we dont repeatedly go into hyperspace
            //inserted here to prevent multiple jumps
            private boolean jPress = false;
            private int initial=0;
            @Override
            public void handle(long now){
                //if the left key is pressed
                if(key_press.getOrDefault(KeyCode.LEFT, false)){
                    //rotate the user_ship left
                    ship.turnLeft();
                }
                //if the right key is pressed
                if(key_press.getOrDefault(KeyCode.RIGHT, false)){
                    //rotate the user_ship right
                    ship.turnRight();
                }

                //if the up key is pressed
                if(key_press.getOrDefault(KeyCode.UP, false)){
                    
                    if (initial==0){
                        //add the boosters to the pane
                        ship.accelerate();
                        Boosters boosters = new Boosters(ship.getX(), ship.getY(), ship.getAngle());
                        boosters.move();
                        pane.getChildren().add(boosters.getChar());
                        initial=1;}

                    else{
                        //remove the boosters from the pane
                        ship.accelerate();
                        pane.getChildren().remove(pane.getChildren().size()-1);
                        //add the boosters to the pane
                        Boosters boosters = new Boosters(ship.getX(), ship.getY(), ship.getAngle());
                        boosters.setFill("RED");
                        boosters.move();
                        pane.getChildren().add(boosters.getChar());
                    }                  
                }
                //remove the boosters from the pane
                
                
                // if the J key is pressed for jump and has not already jumped
                if (key_press.getOrDefault(KeyCode.J, false) && jPress==false) {
                    //jump to a new location and if successful set flag to true
                    ship.hyperspaceJump(pane);
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
                    if(asteroid.collision(ship)){

                        // Remove the collided asteroid from the pane and asteroids list when collision happens
                        pane.getChildren().remove(asteroid.getChar());
                        asteroids.remove(asteroid);

                        // Then add two new smaller asteroids on the scene and in the asteroids list
                        // If the size=3 asteroid is collided
                        if(asteroid.getInitialSize() == 3){
                            for(int i=0; i<2; i++){
                                Asteroid newAsteroid = new Asteroid(asteroid.getChar().getTranslateX(), asteroid.getChar().getTranslateY(), 2);
                                asteroids.add(newAsteroid);
                                pane.getChildren().add(newAsteroid.getChar());
                                newAsteroid.move();
                            }
                        }

                        // If the size=2 asteroid is collided:
                        if(asteroid.getInitialSize() == 2){
                            for(int i=0; i<2; i++){
                                Asteroid newAsteroid = new Asteroid(asteroid.getChar().getTranslateX(), asteroid.getChar().getTranslateY(), 1);
                                asteroids.add(newAsteroid);
                                pane.getChildren().add(newAsteroid.getChar());
                                newAsteroid.move();
                            }
                        }
                }});

            }
        }.start();
    }


    // run the application
    public static void main(String[] args) {
        launch(args);
    }
}
