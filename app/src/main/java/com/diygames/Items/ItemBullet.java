package com.diygames.Items;

import com.diygames.demolitionwars.DemolitionWars;

public class ItemBullet extends Item {

    public boolean direction;

    public boolean shotByPlayer;
    /**
     * Set the item's price 'n name
     * @param game
     */
    public ItemBullet(DemolitionWars game){
        super(game);
        name="Bullet";
        price=200;
    }

    /**
    * A bullet has no use as it's useless if you don't put it in a pistol
     */
    @Override
    public void use() {}

    /**
     * Get shot by a pistol. BOOM.
     * @param shooterX
     * @param shooterY
     * @param usageDirection
     * @param shotByPlayer
     */
    public void getShot(int shooterX, int shooterY, boolean usageDirection, boolean shotByPlayer){
        this.shotByPlayer=shotByPlayer;
        setSpriteId(29+((usageDirection)? 1 : 0));
        game.addGameObject(this, shooterX, shooterY);
        direction=usageDirection;
    }

    /**
     * Move the bullet
     */
    public void update(){
        super.update();
        int directionDegrees=90;
        if(direction){
            directionDegrees=270;
        }
        addToMomentum(directionDegrees, 40);
    }

}
