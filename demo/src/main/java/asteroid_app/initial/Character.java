package asteroid_app.initial;

//import polygon to draw a polygon
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
//import point 2d to represent the movement of any object
import javafx.geometry.Point2D;

//this character class is the parent class of all the characters in the game
public abstract class Character {
	// define a private polygon object to reprent each object
	// and a 2d point to represent the movement of each object
	private Polygon object;
	private Point2D movement;
	// test to see if the object is at it's maximum velocity
	private Point2D test;
	// variable for the dead/alive status of Character
	private Boolean alive;
	//velocity and angle of turn
	private static final double maxVelocity=0.7;
	private static final double TurnAngle=0.3;

	// constructor that each child object will call to
	public Character(Polygon polygon, double x, double y) {
		// create the object's shape and location
		this.object = polygon;
		this.object.setTranslateX(x);
		this.object.setTranslateY(y);

		// initialize the movement of the object to zero
		this.movement = new Point2D(0, 0);

		// when created all characters are alive
		this.alive = true;
	}

	// return the polygon of the current object
	public Polygon getChar() {
		return this.object;
	}

	// return the current movement of the object
	public Point2D getMovement() {
		return this.movement;
	}

	// set the movement for a character
	public void setMovement(Point2D point) {
		this.movement = point;
	}

	// get the living status of a character
	public Boolean getAlive() {
		return this.alive;
	}

	// set the living status of a character
	public void setAlive(Boolean live) {
		this.alive = live;
	}

	// turn it left
	public void turnLeft() {
		// set the rotation to its current-the turn angle
		this.object.setRotate(this.object.getRotate() - TurnAngle);
	}

	// turn it right
	public void turnRight() {
		// add the angle to turn right
		this.object.setRotate(this.object.getRotate() + TurnAngle);
	}

	// set the movement to zero
	public void stopMovement() {
		this.movement = new Point2D(0, 0);
	}

	// move the object
	public void move() {

		// add any acceleration to the current movement
		this.object.setTranslateX(this.object.getTranslateX() + this.movement.getX());
		this.object.setTranslateY(this.object.getTranslateY() + this.movement.getY());

		// if the object is out of the screen, move it to the other side
		// exit the screen from the left side, enter from the right side
		if (this.object.getTranslateX() < 0) {
			this.object.setTranslateX(this.object.getTranslateX() + Main.WIDTH);
		}
		// exit the screen from the right side, enter from the left side
		else if (this.object.getTranslateX() > Main.WIDTH) {
			this.object.setTranslateX(this.object.getTranslateX() - Main.WIDTH);
		}
		// exit the screen from the top, enter from the bottom
		if (this.object.getTranslateY() < 0) {
			this.object.setTranslateY(this.object.getTranslateY() + Main.HEIGHT);
		}
		// exit the screen from the bottom, enter from the top
		else if (this.object.getTranslateY() > Main.HEIGHT) {
			this.object.setTranslateY(this.object.getTranslateY() - Main.HEIGHT);
		}
	}

	// accelerate the object
	public void accelerate(double acceleration) {
		// Calculate acceleration by using trigonometery to calculate the change in the
		// x and y directions
		// through radian rotation
		double changeX = Math.cos(Math.toRadians(this.object.getRotate()));
		double changeY = Math.sin(Math.toRadians(this.object.getRotate()));
		// only need few percent of the possible acceleration
		changeX *= acceleration;
		changeY *= acceleration;
		// ensures that when the object is at maximum velocity
		// we can accelerate in a different direction instead of being stuck
		// at maximum velocity
		test = this.movement.add(changeX, changeY);

		// if our new velocity reduces our current velocity then go with the new
		// velocity
		if (this.movement.magnitude() <= maxVelocity && test.magnitude() <= maxVelocity) {
			this.movement = this.movement.add(changeX, changeY);
		}
	}

	// To check if the objects collide, we check if they have common space polygon
	public Boolean collision(Character other) {
		Shape collisionSpace = Shape.intersect(this.object, other.getChar());
		// If the width of the common space is >= 0, then they have collided
		return collisionSpace.getBoundsInLocal().getWidth() >= 0;
	}

	public Bullet fireBullet(){
		// at the same coordinates as current coordinates of the character with same rotation angle
		Bullet bullet = new Bullet(this.getChar().getTranslateX(),this.getChar().getTranslateY());
		bullet.getChar().setRotate(this.getChar().getRotate());
		return bullet;
}
}
