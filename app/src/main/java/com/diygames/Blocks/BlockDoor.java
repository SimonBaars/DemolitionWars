package com.diygames.Blocks;

import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public class BlockDoor extends BlockLandscape {

    public boolean team;
    public int doorId;
    /**
     * Create the door, set the team and set the id
     * @param game
     */
    public BlockDoor(DemolitionWars game, boolean team, int doorId){
        super(game);
        this.team = team;
        this.doorId=doorId;
    }


    /**
     * Open the door (teleport the player)
     */
	public void open() {
            int modifier = -((64 * 3) + 32);
            if (game.world.isEven(doorId)) {
                modifier = -modifier;
            }
            if (this instanceof BlockWoodenDoor) {
                game.world.humans.get(0).xlocation += 3 * modifier;
            } else {
                game.world.humans.get(0).xlocation += 6 * modifier;
            }
    }

}
