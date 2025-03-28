package com.diygames.demolitionwars;

import android.gameengine.icadroids.objects.MoveableGameObject;

import com.diygames.Blocks.Block;
import com.diygames.Blocks.BlockLadder;
import com.diygames.Humans.Bot;
import com.diygames.Humans.Human;
import com.diygames.Humans.King;
import com.diygames.Humans.Player;
import com.diygames.Items.ItemBullet;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Simon Baars on 3/24/2015.
 */
public class MovingObject extends MoveableGameObject implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<Double> momentum = new ArrayList<Double>();

    public int airtime;

    public byte inAir = 0;

    double[] autoMomentumAddition = new double[3];

    public ArrayList<MovingObject> nearbyObjects = new ArrayList<MovingObject>();

    protected int[] lastCollisionScanPosition = new int[2];
    
    public transient DemolitionWars game;

    /**
    * Init the moving object
     */
    public MovingObject(DemolitionWars game){
        super(game);
        this.game = game;
        autoMomentumAddition[0]=0;
    }

    /**
    * Check for gravity, if there's auto momentum (add x steps to momentum) it adds that momentum and it takes the momentum
     */
    public void update(){
        super.update();
        if((inAir>0 && !(this instanceof ItemBullet)) || this instanceof Player){
        gravity();
        }
        if(autoMomentumAddition[0]>=1){
            autoMomentumAddition[0]--;
            addToMomentum(autoMomentumAddition[1], autoMomentumAddition[2]);
        }
        takeMomentum();
    }

    /**
    * Makes everything fall :). Uses optimized collisions.
     */
    protected void gravity() {
        boolean fall = true;
        airtime++;
        
        // Skip unnecessary checks if no nearby objects
        if (nearbyObjects.isEmpty()) {
            addToMomentum(180, airtime);
            return;
        }
        
        // Maximum iterations to prevent infinite loops but increased for better detection
        int maxIterations = 10; // Increased from 5 to 10
        int iterations = 0;
        
        while(iterations < maxIterations) {
            iterations++;
            fall = true;
            
            int sizeY = this.getY() + airtime + (6 * game.tileSize);
            if(this instanceof Block) {
                sizeY -= 3 * game.tileSize;
            }
            
            int checkX = this.getX() + 25;
            
            // Expanded collision check radius
            for (MovingObject object : nearbyObjects) {
                if(object instanceof Block) {
                    Block block = (Block)object;
                    if (!game.imageLoader.hasNoCollision(block) || block instanceof BlockLadder) {
                        // Check multiple points for better collision detection
                        if (block.collidesWith(checkX, sizeY) || 
                            block.collidesWith(checkX - 10, sizeY) || 
                            block.collidesWith(checkX + 10, sizeY)) {
                            fall = false;
                            hasCollided = false;
                            if (inAir == 2) { 
                                inAir = 0;
                            }
                            break;
                        }
                    }
                }
            }
            
            if(!fall && airtime >= 1) {
                airtime--;
            } else {
                break;
            }
        }
        
        if (airtime > 0) {
            addToMomentum(180, airtime);
        } else {
            if(!(this instanceof Bot)) {
                addToMomentum(180, 0);
            }
            if(this instanceof Obtainables) {
                inAir = 0;
            }
        }
    }

    /**
    * Add a momentum the next x steps
     */
    public void addXTimesToMomentum(int x, double degrees, double speed){
        autoMomentumAddition[0]=x;
        autoMomentumAddition[1]=degrees;
        autoMomentumAddition[2]=speed;
    }

    /**
    * Add a momentum 1 time
     */
    public void addToMomentum(double degrees, double speed){
        momentum.add(degrees);
        momentum.add(speed);
    }

    /**
    * Instead of speed to specific directions we're using momentum in this simulation.
    * This is because this game is made of complex momentums, speeds and directions. Literally.
    * Why literally? We're actually using complex numbers and stuff to calculate it all. I love math :).
    * All credit goes to Stackexchange user Bye_World and Simon Baars who've been working all night
    * on figuring out this formula. All permissions for this code go to Simon Baars :).
     */

    public void takeMomentum(){
        if(momentum.size() <= 0) {
            return; // Nothing to do if no momentum
        }
        
        if(momentum.size() == 2) {
            setDirectionSpeed(momentum.get(0), momentum.get(1));
            momentum.clear();
            return;
        }
        
        if(momentum.size() > 2) {  // We have two or more vectors
            // Cache the trigonometric calculations to avoid redundant calculations
            double[] cosValues = new double[momentum.size()/2];
            double[] sinValues = new double[momentum.size()/2];
            
            // Pre-calculate all trigonometric values
            for(int i = 0; i < momentum.size(); i += 2) {
                double angle = Math.toRadians(momentum.get(i));
                cosValues[i/2] = Math.cos(angle);
                sinValues[i/2] = Math.sin(angle);
            }
            
            // Combine vectors by adding X and Y components
            double totalX = 0;
            double totalY = 0;
            
            for(int i = 0; i < momentum.size(); i += 2) {
                double magnitude = momentum.get(i+1);
                totalX += magnitude * cosValues[i/2];
                totalY += magnitude * sinValues[i/2];
            }
            
            // Convert back to polar form
            double r = Math.sqrt(totalX*totalX + totalY*totalY);
            double theta = Math.toDegrees(Math.atan2(totalY, totalX));
            
            // Adjust negative angles
            if(theta < 0) {
                theta += 360;
            }
            
            // Set the direction and speed
            setDirectionSpeed(theta, r);
            momentum.clear();
        }
    }

    /**
    * The distance between object and this
     */
    public double distanceToThisObject(MovingObject object){
        double distance =0;
        if(getX()>object.getX()){
            distance+=getX()-object.getX();
        } else if(getX()<object.getX()){
            distance+=object.getX()-getX();
        }
        if(getY()>object.getY()){
            distance+=getY()-object.getY();
        } else if(getY()<object.getY()){
            distance+=object.getY()-getY();
        }
        return distance;
    }

    /**
    * Writes all nearby blocks to the nearby blocks array to be used in collision calculations. Speeds up the collision detection.
     */
    public void optimizeCollisions(){
        nearbyObjects.clear();
        lastCollisionScanPosition[0]=getX();
        lastCollisionScanPosition[1]=getY();
        
        int playerX = getX();
        int playerY = getY();
        int maxDistance = 40 * game.tileSize; // Increased from 30 to 40 for better collision detection
        
        // Quick bounding box check for better performance
        int minX = playerX - maxDistance;
        int maxX = playerX + maxDistance;
        int minY = playerY - maxDistance;
        int maxY = playerY + maxDistance;
        
        for(MovingObject object : game.world.blocks){
            if(object instanceof Obtainables){
                int objX = object.getX();
                int objY = object.getY();
                
                // Simple bounding box check first (much faster than full distance calculation)
                if(objX >= minX && objX <= maxX && objY >= minY && objY <= maxY) {
                    // Include all objects in bounding box to avoid missing collisions
                    nearbyObjects.add(object);
                }
            }
        }
    }

    /**
    * When this object breaks and calculation of money
     */
    public void die(){
        game.deleteGameObject(this);
        game.world.blocks.remove(this);
        if(this instanceof Human){
            if(((Human)this).team==game.world.humans.get(0).team){
                ((Player)game.world.humans.get(0)).money-=250;
            } else {
                ((Player)game.world.humans.get(0)).money+=250;
                ((Player)game.world.humans.get(0)).killCounter++;
            }
        }else if(this instanceof Block){
            ((Player)game.world.humans.get(0)).money+=1;
        }
        if(this instanceof King){
            if(((Human)this).team==game.world.humans.get(0).team){
                game.setGameOver();
            } else {
                game.setGameWon();
            }
        }
    }
}
