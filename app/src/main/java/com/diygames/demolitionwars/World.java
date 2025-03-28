package com.diygames.demolitionwars;

import com.diygames.Blocks.BlockBrick;
import com.diygames.Blocks.BlockDirt;
import com.diygames.Blocks.BlockDoor;
import com.diygames.Blocks.BlockGrass;
import com.diygames.Blocks.BlockLadder;
import com.diygames.Blocks.BlockLandscape;
import com.diygames.Blocks.BlockPlanks;
import com.diygames.Blocks.BlockSteel;
import com.diygames.Blocks.BlockSteelDoor;
import com.diygames.Blocks.BlockStone;
import com.diygames.Blocks.BlockUnbreakableRock;
import com.diygames.Blocks.BlockWoodenDoor;
import com.diygames.Blocks.BlockWoodenPole;
import com.diygames.Blocks.BlockWool;
import com.diygames.Humans.Demolist;
import com.diygames.Humans.Human;
import com.diygames.Humans.King;
import com.diygames.Humans.Player;
import com.diygames.Humans.Blockseller;
import com.diygames.Humans.Weaponclerk;
import com.diygames.Humans.Guard;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public class World implements Serializable {

	private static final long serialVersionUID = 1L;

	public ArrayList<MovingObject> blocks = new ArrayList<MovingObject>();

	private ArrayList<Obtainables> obtainables = new ArrayList<Obtainables>();

    public transient DemolitionWars game;

    public ArrayList<Human> humans = new ArrayList<Human>();

    /**
    * Generate the world
     */
    public World(DemolitionWars game){
        this.game = game;
        humans.add(new Player(game, false));
        game.addGameObject(humans.get(0), 2974, 2848);
        game.setPlayer(humans.get(0));

        generateLayers();
        generateWorld();
    }

    /**
    * Generate both sides of the world
     */
    void generateWorld(){
        /*
        For the size of the array I use:
        game.getMapWidth()/(game.tileSize*3) which is 15000/(64*3)=78.12 (53 naar 78)
        game.getMapHeight()/(game.tileSize*3) which is (4000/(64*3))-6=14.83(15)
         */
        //Block[] blockTypes = {new BlockBrick(), new BlockSteelDoor(false, 0, 0), new BlockWoodenPole(), new BlockWool(0), new BlockWool(1), new BlockSteel(), new BlockBrick()};
        int[][] world = {
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,14,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,1,1,1,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,0,0,6,0,0,0,0,0,0,0,0,14,0,0,0,0,0,0,0,0,0,14,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,1,0,1,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,6,6,6,0,0,0,0,0,0,7,0,0,7,0,0,0,0,0,0,9,0,0,9,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,1,0,1,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,6,6,6,0,0,0,0,0,0,7,7,7,7,0,0,0,0,0,0,9,9,9,9,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,1,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,6,6,6,0,0,0,0,0,0,7,7,7,7,0,0,0,0,0,0,9,9,9,9,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,6,6,6,0,0,0,0,0,0,7,7,7,7,0,0,0,0,0,0,9,9,9,9,0,0,0,0,0,0,0,9,0,0,0,0,0,0,0,0,0,0,0},
                {0,1,13,0,0,15,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,6,6,6,0,0,0,0,0,0,7,7,7,7,0,0,0,0,0,0,9,9,9,9,0,0,0,0,0,0,0,9,0,0,0,0,0,0,0,0,0,0,0},
                {0,1,13,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,6,6,6,0,0,0,0,0,0,7,7,7,7,0,0,0,0,0,0,9,9,9,9,0,0,0,0,0,0,0,9,0,0,0,0,0,0,0,0,0,0,0},
                {0,1,13,0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,6,6,6,0,0,0,0,0,0,7,7,7,7,0,0,0,0,0,0,9,9,9,9,0,0,0,0,0,0,0,9,0,0,0,0,0,0,0,0,0,0,0},
                {0,1,13,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,6,6,6,0,0,0,0,0,0,7,7,7,7,0,0,0,0,0,0,9,9,9,9,0,0,0,0,0,0,0,9,0,0,0,0,0,0,0,0,0,0,0},
                {0,1,13,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,6,6,6,0,0,0,0,0,0,7,7,7,7,0,0,0,0,0,0,9,9,9,9,0,0,0,0,0,0,0,9,0,0,0,0,0,0,0,0,0,0,0},
                {0,1,13,0,0,0,0,1,0,0,0,4,5,4,5,4,0,0,4,5,4,5,4,0,0,4,5,4,5,4,0,0,0,0,0,6,6,6,6,0,0,0,0,0,0,7,7,7,7,0,0,0,0,0,0,9,9,9,9,0,0,0,0,0,0,0,9,0,0,0,0,0,0,9,0,0,0,0},
                {0,1,13,0,0,0,8,1,8,0,0,3,10,3,0,3,0,0,3,11,3,0,3,0,0,3,12,3,0,3,0,0,0,0,2,6,6,6,6,2,0,14,14,0,2,7,7,7,7,2,0,14,14,0,2,9,9,9,9,2,0,0,14,14,0,8,9,8,0,0,14,0,8,9,8,0,0,0},
                {0,1,13,0,0,0,0,1,0,0,0,3,0,3,0,3,0,0,3,0,3,0,3,0,0,3,0,3,0,3,0,0,0,0,0,6,6,6,6,0,0,0,0,0,0,7,7,7,7,0,0,0,0,0,0,9,9,9,9,0,0,0,0,0,0,0,9,0,0,0,0,0,0,9,0,0,0,0},
        };

        int doorId=0;
        for(int team = 0; team<2; team++) {
            int k = 0;
            for (int i = world.length - 1; i >= 0; i--) {
                int l = world[0].length-1;
                for (int j = 0; j < world[0].length; j++) {
                    if (world[i][j] != 0) {
                        int mirrorTeam = l;
                        if (team == 0) {
                            mirrorTeam = j;
                        }
                        if (getType(world[i][j], false, 0) instanceof Human) {
                            blocks.add(getType(world[i][j], (team != 0), (mirrorTeam * (game.tileSize * 3)) + (((game.tileSize * 3) * world[0].length) * team)));
                        } else {
                            blocks.add(getType(world[i][j], (team != 0), doorId));
                            if (getType(world[i][j], false, 0) instanceof BlockDoor) {
                                doorId++;
                            }
                        }
                        if (blocks.get(blocks.size() - 1) instanceof Human) {
                            game.addGameObject(blocks.get(blocks.size() - 1), (mirrorTeam * (game.tileSize * 3)) + (((game.tileSize * 3) * world[0].length) * team), game.getMapHeight() - ((5 + k) * (game.tileSize * 3)),1);
                        } else {
                            game.addGameObject(blocks.get(blocks.size() - 1), (mirrorTeam * (game.tileSize * 3)) + (((game.tileSize * 3) * world[0].length) * team), game.getMapHeight() - ((5 + k) * (game.tileSize * 3)));
                        }
                    }
                    l--;
                }
                k++;
            }
            doorId++; //Doors must take mirrorvalues too!
        }
    }

    /**
    * Get the type of block we're trying to add to the world
     */
    public MovingObject getType(int blockTypeId, boolean metadata, int moreMetadata){
        switch(blockTypeId){
            case 1:
                return new BlockBrick(game);
            case 2:
                return new BlockSteelDoor(game,metadata, moreMetadata);
            case 3:
                return new BlockWoodenPole(game);
            case 4:
                return new BlockWool(game,0);
            case 5:
                return new BlockWool(game,1);
            case 6:
                return new BlockSteel(game);
            case 7:
                return new BlockBrick(game);
            case 8:
                return new BlockWoodenDoor(game,metadata, moreMetadata);
            case 9:
                return new BlockPlanks(game);
            case 10:
                return new Demolist(game, metadata, moreMetadata);
            case 11:
                return new Blockseller(game, metadata, moreMetadata);
            case 12:
                return new Weaponclerk(game, metadata, moreMetadata);
            case 13:
                return new BlockLadder(game, metadata);
            case 14:
                return new Guard(game, metadata, moreMetadata);
            case 15:
                return new King(game, metadata, moreMetadata);
        }
        return null;
    }

    /**
    * Generate layers of the world
     */
	public void generateLayers() {
        for(int i = 0; i<game.getMapWidth(); i+=game.tileSize*3) {
            createLayer(new BlockUnbreakableRock(game), -1, i);
            createLayer(new BlockStone(game), 0, i);
            createLayer(new BlockStone(game), 1, i);
            createLayer(new BlockDirt(game), 2, i);
            createLayer(new BlockDirt(game), 3, i);
            createLayer(new BlockGrass(game), 4, i);
        }
    }

    /**
    * Create the layer itself
     */
    public void createLayer(BlockLandscape layerType, int layer, int i){
        blocks.add(layerType);
        game.addGameObject(blocks.get(blocks.size()-1), i, game.getMapHeight() - (layer*(game.tileSize * 3)));
    }

    /**
    * Check if a given integer is even or not
     */
    public boolean isEven(int number){
        if ((number & 1) == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
    * Optimize the collisions for all Humans
     */
    public void optimizeCollisionsForAllHumans(){
        humans.get(0).optimizeCollisions();
        for(MovingObject object : blocks){
            if(object instanceof Human){
                Human human = (Human)object;
                human.optimizeCollisions();
            }
        }
    }
}
