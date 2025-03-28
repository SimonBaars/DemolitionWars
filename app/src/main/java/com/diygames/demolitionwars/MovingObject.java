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
     * Update method called every frame.
     * Handles gravity, momentum and movement.
     */
    @Override
    public void update() {
        super.update();
        
        // Apply gravity if needed
        if ((inAir > 0 && !(this instanceof ItemBullet)) || this instanceof Player) {
            gravity();
        }
        
        // Apply any scheduled momentum additions
        if (autoMomentumAddition[0] >= 1) {
            autoMomentumAddition[0]--;
            addToMomentum(autoMomentumAddition[1], autoMomentumAddition[2]);
        }
        
        // Process all momentum vectors
        takeMomentum();
    }

    /**
     * Applies gravity to the object using optimized collision detection
     */
    protected void gravity() {
        boolean fall = true;
        airtime++;
        
        // Early exit if no nearby objects to check against
        if (nearbyObjects.isEmpty()) {
            addToMomentum(180, airtime);
            return;
        }
        
        // Parameters for collision detection
        final int maxIterations = 10;
        final int collisionPointOffset = 10;
        int iterations = 0;
        
        while (iterations < maxIterations) {
            iterations++;
            fall = true;
            
            // Calculate vertical position based on current airtime
            int sizeY = getY() + airtime + (6 * game.tileSize);
            if (this instanceof Block) {
                sizeY -= 3 * game.tileSize;
            }
            
            // Check collision at multiple points along the base
            int checkX = getX() + 25;
            
            // Check collisions against nearby blocks
            for (MovingObject object : nearbyObjects) {
                if (!(object instanceof Block)) {
                    continue;
                }
                
                Block block = (Block)object;
                if (game.imageLoader.hasNoCollision(block) && !(block instanceof BlockLadder)) {
                    continue;
                }
                
                // Check multiple points for better collision detection
                if (checkCollisionWithBlock(block, checkX, sizeY, collisionPointOffset)) {
                    fall = false;
                    hasCollided = false;
                    if (inAir == 2) { 
                        inAir = 0;
                    }
                    break;
                }
            }
            
            // Adjust airtime if collision detected
            if (!fall && airtime >= 1) {
                airtime--;
            } else {
                break;
            }
        }
        
        // Apply momentum based on airtime
        applyGravityMomentum();
    }
    
    /**
     * Checks collision with a block at multiple points
     */
    private boolean checkCollisionWithBlock(Block block, int centerX, int sizeY, int offset) {
        return block.collidesWith(centerX, sizeY) || 
               block.collidesWith(centerX - offset, sizeY) || 
               block.collidesWith(centerX + offset, sizeY);
    }
    
    /**
     * Applies appropriate momentum based on airtime
     */
    private void applyGravityMomentum() {
        if (airtime > 0) {
            addToMomentum(180, airtime);
        } else {
            if (!(this instanceof Bot)) {
                addToMomentum(180, 0);
            }
            if (this instanceof Obtainables) {
                inAir = 0;
            }
        }
    }

    /**
     * Add momentum to be applied over multiple steps
     * @param steps Number of steps to apply this momentum
     * @param degrees Direction in degrees
     * @param speed Speed/magnitude of momentum
     */
    public void addXTimesToMomentum(int steps, double degrees, double speed) {
        autoMomentumAddition[0] = steps;
        autoMomentumAddition[1] = degrees;
        autoMomentumAddition[2] = speed;
    }

    /**
     * Add momentum to be applied immediately
     * @param degrees Direction in degrees
     * @param speed Speed/magnitude of momentum
     */
    public void addToMomentum(double degrees, double speed) {
        momentum.add(degrees);
        momentum.add(speed);
    }

    /**
     * Process all momentum values and apply the resulting movement.
     * Uses vector addition to combine all momentum vectors.
     */
    public void takeMomentum() {
        // Nothing to do if no momentum
        if (momentum.isEmpty()) {
            return;
        }
        
        // Simple case: only one momentum vector
        if (momentum.size() == 2) {
            setDirectionSpeed(momentum.get(0), momentum.get(1));
            momentum.clear();
            return;
        }
        
        // Complex case: multiple momentum vectors to combine
        if (momentum.size() > 2) {
            // Convert polar vectors to cartesian components and sum them
            double totalX = 0;
            double totalY = 0;
            
            for (int i = 0; i < momentum.size(); i += 2) {
                double angle = Math.toRadians(momentum.get(i));
                double magnitude = momentum.get(i + 1);
                
                totalX += magnitude * Math.cos(angle);
                totalY += magnitude * Math.sin(angle);
            }
            
            // Convert back to polar form
            double resultantMagnitude = Math.sqrt(totalX * totalX + totalY * totalY);
            double resultantAngle = Math.toDegrees(Math.atan2(totalY, totalX));
            
            // Adjust negative angles
            if (resultantAngle < 0) {
                resultantAngle += 360;
            }
            
            // Apply the resultant momentum
            setDirectionSpeed(resultantAngle, resultantMagnitude);
            momentum.clear();
        }
    }

    /**
    * Calculate Manhattan distance between this object and another object
    * More efficient than Euclidean distance for simple collision checks
    */
    public double distanceToThisObject(MovingObject object) {
        // Use absolute values for cleaner code
        return Math.abs(getX() - object.getX()) + 
               Math.abs(getY() - object.getY());
    }

    /**
    * Optimize collision detection by storing nearby objects
    */
    public void optimizeCollisions() {
        nearbyObjects.clear();
        lastCollisionScanPosition[0] = getX();
        lastCollisionScanPosition[1] = getY();
        
        int playerX = getX();
        int playerY = getY();
        int maxDistance = 40 * game.tileSize;
        
        // Quick bounding box check for better performance
        int minX = playerX - maxDistance;
        int maxX = playerX + maxDistance;
        int minY = playerY - maxDistance;
        int maxY = playerY + maxDistance;
        
        // Use foreach loop for cleaner iteration
        for (MovingObject object : game.world.blocks) {
            if (object instanceof Obtainables) {
                int objX = object.getX();
                int objY = object.getY();
                
                // Simple bounding box check first (much faster than full distance calculation)
                if (objX >= minX && objX <= maxX && objY >= minY && objY <= maxY) {
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
