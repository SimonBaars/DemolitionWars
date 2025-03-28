package com.diygames.demolitionwars;

import android.gameengine.icadroids.objects.MoveableGameObject;

import com.diygames.Blocks.Block;
import com.diygames.Blocks.BlockLadder;
import com.diygames.Humans.Bot;
import com.diygames.Humans.Human;
import com.diygames.Humans.King;
import com.diygames.Humans.Player;
import com.diygames.Items.ItemBullet;

import java.util.ArrayList;

/**
 * Created by Simon Baars on 3/24/2015.
 */
public class MovingObject extends MoveableGameObject {

    private ArrayList<Double> momentum = new ArrayList<Double>();

    public int airtime;

    public byte inAir = 0;

    double[] autoMomentumAddition = new double[3];

    public ArrayList<MovingObject> nearbyObjects = new ArrayList<MovingObject>();

    protected int[] lastCollisionScanPosition = new int[2];

    /**
    * Init the moving object
     */
    public MovingObject(DemolitionWars game){
        super(game);
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
        boolean fall;
        airtime++;
        while(true){
            fall=true;
                for (MovingObject object : nearbyObjects) {
                    if(object instanceof Block) {
                        Block block = (Block)object;
                        if (!game.imageLoader.hasNoCollision(block) || block instanceof BlockLadder) {
                            int sizeY = this.getY() + airtime + (6 * game.tileSize);
                            if(this instanceof Block){
                                sizeY-=3*game.tileSize;
                            }
                            if (block.collidesWith(this.getX() + 25, sizeY/* is size*/)) {
                                fall = false;
                                hasCollided = false;
                                if (inAir == 2) { //If inAir==2 means that the player is in the air and still holding the A button (this is not good as he's contantly creating momentum upwards.
                                    inAir=0;
                                }
                                break;
                            }
                        }
                    }
                }
            if(!fall && airtime>=1){
                airtime--;
            } else {
                break;
            }
        }
        if (airtime>0) {
            addToMomentum(180, airtime);
        } else {
            if(!(this instanceof Bot)) {
                addToMomentum(180, 0);
            }
            if(this instanceof Obtainables){
                inAir=0;
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
        if(momentum.size()>2){  //We have two or more vectors in the following form: a1eiθ1 and a2eiθ2
            while(momentum.size()>2){
                /*
                to add them you need to get them into Cartesian form. Use Euler's formula for that: reiθ=r(cos(θ)+isin(θ)).
                Then add up the real parts and imaginary parts. And finally, if you need the angle and length back you can convert
                them back into polar form via r=√x2+y2and θ=atan2(y,x), where x is the real part and y is the imaginary part.
                 */
                double r = Math.sqrt(Math.pow(momentum.get(1)*Math.cos( Math.toRadians(momentum.get(0)))+momentum.get(3)*Math.cos( Math.toRadians(momentum.get(2))),2)+Math.pow(momentum.get(1)*Math.sin( Math.toRadians(momentum.get(0)))+momentum.get(3)*Math.sin( Math.toRadians(momentum.get(2))),2));
                double theta = Math.toDegrees(Math.atan2(momentum.get(1)*Math.sin( Math.toRadians(momentum.get(0)))+momentum.get(3)*Math.sin( Math.toRadians(momentum.get(2))),momentum.get(1)*Math.cos( Math.toRadians(momentum.get(0)))+momentum.get(3)*Math.cos( Math.toRadians(momentum.get(2)))));
                if(theta<0){ //Compensation for an error with the formula
                    theta+=360;
                }
                momentum.set(0,theta);
                momentum.set(1,r);
                momentum.remove(2);
                momentum.remove(2);
            }
        }
        if(momentum.size()==2){
            setDirectionSpeed(momentum.get(0), momentum.get(1));
        }
        momentum.clear();
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
        for(MovingObject object : game.world.blocks){
            if(distanceToThisObject(object)<(30*game.tileSize) && object instanceof Obtainables){
                nearbyObjects.add(object);
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
