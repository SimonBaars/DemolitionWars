package com.diygames.demolitionwars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public abstract class Obtainables extends MovingObject implements Comparable {

    private int sizex;

    private int sizey;

    public String name;

    public int price;

    /**
    * Set the size of obtainables
     */
    public Obtainables(DemolitionWars game){
        super(game);
        sizex=64;
        sizey=64;
    }

    /**
    * Use an item or block (place the block)
     */
    public abstract void use();

    /**
    * Check if an x and y collide with this object
     */
    public boolean collidesWith(int doelx, int doely) {
        if (doelx>getX()-(1.5*sizex) && doely>getY() && doelx<getX()+sizex+(1.5*sizex) && doely<getY()+sizey+(1.5*sizey)) {
            return true;
        }
        return false;
    }

    /**
    * To be comparable in price. Add Collections.order to any salesman and his sales will be ordered by price (not added to the game as it still gives some issues.)
     */
    public int compareTo(Object o){
        return(price-((Obtainables)o).price);
    }
}
