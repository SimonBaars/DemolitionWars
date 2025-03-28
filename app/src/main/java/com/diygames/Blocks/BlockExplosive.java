package com.diygames.Blocks;

import android.gameengine.icadroids.alarms.Alarm;
import android.gameengine.icadroids.alarms.IAlarm;

import com.diygames.Humans.Bot;
import com.diygames.demolitionwars.MovingObject;
import com.diygames.demolitionwars.Obtainables;
import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public abstract class BlockExplosive extends Block implements IAlarm {

	public int timeTillExplosion;

    protected double blastSize;

    protected double blastStrength;

    boolean suckerBomb;

    double destroyPercentage;

    public Alarm explosionTime;

    /**
     * Set all of the explosive settings
     * @param game
     * @param timeTillExplosion
     * @param blastSize
     * @param blastStrength
     * @param suckerBomb
     * @param destroyPercentage
     */
    public BlockExplosive(DemolitionWars game, int timeTillExplosion, double blastSize, double blastStrength, boolean suckerBomb, double destroyPercentage){
        super(game);
        this.timeTillExplosion=timeTillExplosion;
        this.blastStrength=blastStrength;
        this.blastSize=blastSize;
        this.suckerBomb=suckerBomb;
        this.destroyPercentage=destroyPercentage;
    }

    /**
     * Set the explosive to explode in timeTillExplosion steps
     */
    public void setExplosionTimer(){
        explosionTime = new Alarm(3,timeTillExplosion,this);
        explosionTime.startAlarm();
        if(this instanceof BlockGrenade){
            BlockGrenade thisObject = (BlockGrenade)this;
            thisObject.throwIntoTheWorld(30);
        }
    }

    /**
     * trigger the alarm to explode the bomb
     * @param alarmId
     */
    public void triggerAlarm(int alarmId){
        if(alarmId==3){
            explode();
            explosionTime.restartAlarm();
        }
    }

    /**
     * Explode (lots of mathematical stuff here)
     */
	public void explode() {
        double realBlastSize = blastSize*(game.tileSize*3); // Make the blastsize grid based
        for(int i = 0; i<game.world.blocks.size(); ) { //First for-loop where stuff disappears
            if (invertInt((distanceToThisObject(game.world.blocks.get(i)) / (realBlastSize / 2)) * blastStrength, blastStrength / 2) > blastStrength*(invertInt(destroyPercentage, 50.0)/100) && !(game.world.blocks.get(i) instanceof BlockUnbreakableRock)) {
                if(this instanceof BlockMinerBomb && game.world.blocks.get(i) instanceof Obtainables && !(game.world.blocks.get(i) instanceof BlockMinerBomb)){
                    if(((BlockMinerBomb)this).getBlock((Obtainables)game.world.blocks.get(i))!=null){
                        game.world.humans.get(0).inventory.add(((BlockMinerBomb)this).getBlock((Obtainables)game.world.blocks.get(i)));
                    }
                }
                game.world.blocks.get(i).die();
            } else {
                i++;
            }
        }
        for(MovingObject object : game.world.blocks) { //Second for-loop where stuff gets momentum
            if(object.getX()>getX()-(realBlastSize/2) && object.getY()>getY()-(realBlastSize/2) && object.getX()<getX()+(realBlastSize/2) && object.getY()<getY()+(realBlastSize/2)  && !(object instanceof BlockUnbreakableRock)){ //Filter the objects that won't be affected by this explosion
                double degrees = degreesToBomb(object)+180;
                if(suckerBomb){
                    degrees-=180;
                }
                if(degrees>=360){
                    degrees-=360;
                }
                object.addXTimesToMomentum(10, degrees, invertInt((distanceToThisObject(object)/(realBlastSize/2))*blastStrength, blastStrength/2));
                object.inAir=3;
                object.optimizeCollisions();
            }else if (object instanceof Bot){
                ((Bot)object).checkGravityNextXSteps(50);
            }
        }
        if(game.world.humans.get(0).getX()>getX()-(realBlastSize/2) && game.world.humans.get(0).getY()>getY()-(realBlastSize/2) && game.world.humans.get(0).getX()<getX()+(realBlastSize/2) && game.world.humans.get(0).getY()<getY()+(realBlastSize/2)){ //Filter the objects that won't be affected by this explosion
            game.world.humans.get(0).addXTimesToMomentum(10, degreesToBomb(game.world.humans.get(0)), invertInt(distanceToThisObject(game.world.humans.get(0)), realBlastSize / 2)/3);
            game.world.humans.get(0).health-=(invertInt(distanceToThisObject(game.world.humans.get(0)), realBlastSize / 2)/5);
        }
        createExplosion();
        game.world.humans.get(0).optimizeCollisions();
        die();
    }

    /**
     * Subclasses are allowed to create their own explosioneffects here (For example: Scatterbomb scatters the world here, and Napalm creates it's Firebombs here)
     */
	public abstract void createExplosion();

    /**
     * Calculates the degrees that an object makes with the bomb
     * @param object
     * @return
     */
    public double degreesToBomb(MovingObject object){
        if(getX()>=object.getX() && getY()>=object.getY()){
            double differenceX = object.getX()-getX();
            double differenceY = object.getY()-getY();
            double corner=Math.toDegrees(Math.atan(differenceX/differenceY));
            return invertInt(corner+270, (90/2)+270);
        } else if(getX()>=object.getX() && getY()<=object.getY()){
            double differenceX = getX()-object.getX();
            double differenceY = getY()-object.getY();
            double corner=Math.toDegrees(Math.atan(differenceX/differenceY));
            return invertInt(corner+90, (90/2)+90);
        } else if(getX()<=object.getX() && getY()>=object.getY()){
            double differenceX = object.getX()-getX();
            double differenceY = object.getY()-getY();
            double corner=Math.toDegrees(Math.atan(differenceX/differenceY));
            return invertInt(corner-90,(90/2)-90);
        } else if(getX()<=object.getX() && getY()<=object.getY()){
            double differenceX = getX()-object.getX();
            double differenceY = getY()-object.getY();
            double corner=Math.toDegrees(Math.atan(differenceX/differenceY));
            return invertInt(corner+90, (90/2)+90);
        }
        return 0;
    }

    /**
     * Invert an integer. For example, if the mid is 0 and the number is 10 it becomes -10. Second example: if the mid is 50 and the number is 25 it becomes 75. Extremely useful with all the mathematical stuff included in this file :D.
     * @param number
     * @param mid
     * @return
     */
    public double invertInt(double number, double mid){
        if(number>mid){
            return number-(2*(number-mid));
        }
        return number+(2*(mid-number));
    }

}
