package com.diygames.Humans;

import android.gameengine.icadroids.alarms.Alarm;
import android.gameengine.icadroids.input.OnScreenButtons;

import com.diygames.Items.Item;
import com.diygames.Items.ItemPistol;
import com.diygames.Items.ItemWing;
import com.diygames.demolitionwars.MovingObject;
import com.diygames.demolitionwars.DemolitionWars;
/**
 * Created by Simon Baars on 3/24/2015.
 */
public class Player extends Human {

	public int selectedItem;

	public int money=1000;

    public Alarm jumpAlarm;

    private boolean buttonUsed=false;

    public int selectedShopItem = 0;

    public boolean inShop=false;

    public Salesman currentlyBuyingFrom;

    public int killCounter=0;

    /**
     * Initialize the player
     * @param game
     * @param team
     */
    public Player(DemolitionWars game, boolean team) {
        super(game, team);
        setSpriteId(0);
        jumpAlarm = new Alarm(1, 15, this);
        selectedItem=0;
    }

    /**
     * Check if the player pushes buttons and react. Also his health is managed here.
     */
    public void update()
    {
        super.update();
        // Handle input. Both on screen buttons and tilting are supported.
        // Buttons take precedence.
        if (OnScreenButtons.dPadRight) {
            if(inShop){
                if(selectedShopItem<currentlyBuyingFrom.inventory.size()-1 && !buttonUsed) {
                    selectedShopItem++;
                    buttonUsed=true;
                }
            }else {
                if (this.getX() <= game.getMapWidth() && !hasCollided) {
                    addToMomentum(90, 10 + (10 * ((OnScreenButtons.buttonB) ? 1 : 0)));
                    if (animationFrame > 3) {
                        animationFrame -= 4;
                        setFrameNumber(animationFrame);
                    }
                }
                isWalking = true;
            }
        } else if (OnScreenButtons.dPadLeft){
            if(inShop){
                if(selectedShopItem>0 && !buttonUsed) {
                    selectedShopItem--;
                    buttonUsed=true;
                }
            } else {
                if (this.getX() >= 0 && !hasCollided) {
                    addToMomentum(270, 10 + (10 * ((OnScreenButtons.buttonB) ? 1 : 0)));
                    if (animationFrame < 4) {
                        animationFrame += 4;
                        setFrameNumber(animationFrame);
                    }
                }
                isWalking = true;
            }
        } else {
            isWalking=false;
        }

        if(!inShop) {
            if (OnScreenButtons.buttonA) {
                if (inAir < 3 && !hasCollided) {
                    addToMomentum(0, 20);
                    if (inAir == 0) {
                        jumpAlarm.restartAlarm();
                        inAir++;
                    }
                }
            } else {
                inAir = 0;
            }

            if (OnScreenButtons.buttonX && !buttonUsed) {
                if (inventory.size() > 0) {
                    buttonUsed = true;
                    inventory.get(selectedItem).use();
                    boolean removeItem = true;
                    if (inventory.get(selectedItem) instanceof Item) {
                        Item inventoryItem = (Item) inventory.get(selectedItem);
                        if (inventoryItem.usesLeft > 0) {
                            removeItem = false;
                            if (inventoryItem instanceof ItemWing) {
                                buttonUsed = false;
                            }
                        }
                    }
                    if (removeItem) {
                        inventory.remove(selectedItem);
                        if (selectedItem > 0) {
                            selectedItem--;
                        }
                    }
                }
            }
        } else if (OnScreenButtons.buttonA && !buttonUsed){
            if(money>=currentlyBuyingFrom.inventory.get(selectedShopItem).price){
                money-=currentlyBuyingFrom.inventory.get(selectedShopItem).price;
                if(currentlyBuyingFrom.inventory.get(selectedShopItem) instanceof ItemPistol || currentlyBuyingFrom.inventory.get(selectedShopItem) instanceof ItemPistol){
                    inventory.add(0, currentlyBuyingFrom.getSale(selectedShopItem));
                } else {
                    inventory.add(currentlyBuyingFrom.getSale(selectedShopItem));
                }
                inShop=false;
                buttonUsed=true;
            }
        }

        if (OnScreenButtons.dPadDown && !buttonUsed)
        {
            if(selectedItem>0){
                selectedItem--;
                buttonUsed=true;
            }
        }

        if (OnScreenButtons.dPadUp && !buttonUsed)
        {
            if(selectedItem<inventory.size()-1){
                selectedItem++;
                buttonUsed=true;
            }
        }

        if(OnScreenButtons.buttonY && !buttonUsed){
            if(!inShop) {
                for (MovingObject object : game.world.blocks) {
                    if (object instanceof Bot) {
                        if (object.distanceToPlayer() < 6 * game.tileSize) {
                            if(((Human)object).team==team) {
                                ((Bot) object).interact();
                                break;
                            }
                        }
                    }
                }
            } else {
                inShop=false;
            }
            buttonUsed=true;
        }

        if(buttonUsed && !OnScreenButtons.buttonX & !OnScreenButtons.dPadUp && !OnScreenButtons.dPadDown && !OnScreenButtons.buttonY){
            if(!inShop || (!OnScreenButtons.dPadLeft && !OnScreenButtons.dPadRight)) {
                buttonUsed = false;
            }
        }

        if(health<=0) {
            game.setGameOver();
        } else if(health<100){
                health++;
        }
    }
}
