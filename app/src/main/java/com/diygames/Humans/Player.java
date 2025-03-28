package com.diygames.Humans;

import android.gameengine.icadroids.alarms.Alarm;
import android.gameengine.icadroids.input.OnScreenButtons;

import com.diygames.Items.Item;
import com.diygames.Items.ItemPistol;
import com.diygames.Items.ItemWing;
import com.diygames.demolitionwars.MovingObject;
import com.diygames.demolitionwars.DemolitionWars;

/**
 * Player character that responds to user input
 * 
 * @author Simon Baars
 */
public class Player extends Human {

	// Player state
	public int selectedItem;
	public int money = 1000;
	public int selectedShopItem = 0;
	public boolean inShop = false;
	public Salesman currentlyBuyingFrom;
	public int killCounter = 0;
	
	// Movement and input
	public Alarm jumpAlarm;
	private boolean buttonUsed = false;
	
	// Input cooldown to prevent rapid button presses
	private long lastButtonPressTime = 0;
	private static final long BUTTON_COOLDOWN = 200; // 200ms cooldown between button presses
	
	// Running speed factors
	private static final float NORMAL_SPEED = 10f;
	private static final float RUNNING_SPEED = 20f;

	/**
	 * Initialize the player
	 * @param game Reference to main game
	 * @param team Team affiliation
	 */
	public Player(DemolitionWars game, boolean team) {
		super(game, team);
		setSpriteId(0);
		jumpAlarm = new Alarm(1, 15, this);
		selectedItem = 0;
	}

	/**
	 * Update player state based on input
	 */
	@Override
	public void update() {
		super.update();
		
		if (game == null) return;
		
		// Check if player is in shop
		if (inShop) {
			handleShopInput();
		} else {
			handleMovementInput();
			handleActionInput();
		}
		
		// Reset button state if all buttons released
		if (buttonUsed && !anyActionButtonPressed()) {
			buttonUsed = false;
		}
		
		// Handle health regeneration and death
		updateHealthStatus();
	}
	
	/**
	 * Handle shop-specific input
	 */
	private void handleShopInput() {
		// Shop navigation
		if (OnScreenButtons.dPadRight) {
			if (canPressButton() && selectedShopItem < getCurrentShopInventorySize() - 1) {
				selectedShopItem++;
				buttonUsed = true;
				lastButtonPressTime = System.currentTimeMillis();
			}
		} else if (OnScreenButtons.dPadLeft) {
			if (canPressButton() && selectedShopItem > 0) {
				selectedShopItem--;
				buttonUsed = true;
				lastButtonPressTime = System.currentTimeMillis();
			}
		}
		
		// Buy item
		if (OnScreenButtons.buttonA && canPressButton()) {
			buySelectedItem();
		}
		
		// Exit shop
		if (OnScreenButtons.buttonY && canPressButton()) {
			exitShop();
		}
	}
	
	/**
	 * Handle player movement input
	 */
	private void handleMovementInput() {
		// Walking/running left and right
		float movementSpeed = OnScreenButtons.buttonB ? RUNNING_SPEED : NORMAL_SPEED;
		
		if (OnScreenButtons.dPadRight) {
			if (this.getX() <= game.getMapWidth() && !hasCollided) {
				addToMomentum(90, movementSpeed);
				if (animationFrame > 3) {
					animationFrame -= 4;
					setFrameNumber(animationFrame);
				}
			}
			isWalking = true;
		} else if (OnScreenButtons.dPadLeft) {
			if (this.getX() >= 0 && !hasCollided) {
				addToMomentum(270, movementSpeed);
				if (animationFrame < 4) {
					animationFrame += 4;
					setFrameNumber(animationFrame);
				}
			}
			isWalking = true;
		} else {
			isWalking = false;
		}
		
		// Jumping
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
	}
	
	/**
	 * Handle inventory selection and item usage
	 */
	private void handleActionInput() {
		// Use selected item
		if (OnScreenButtons.buttonX && canPressButton()) {
			useSelectedItem();
		}
		
		// Select previous item
		if (OnScreenButtons.dPadDown && canPressButton()) {
			if (selectedItem > 0) {
				selectedItem--;
				buttonUsed = true;
				lastButtonPressTime = System.currentTimeMillis();
				game.showMessage("Selected: " + getSelectedItemName());
			}
		}
		
		// Select next item
		if (OnScreenButtons.dPadUp && canPressButton()) {
			if (selectedItem < inventory.size() - 1) {
				selectedItem++;
				buttonUsed = true;
				lastButtonPressTime = System.currentTimeMillis();
				game.showMessage("Selected: " + getSelectedItemName());
			}
		}
		
		// Interact with NPCs
		if (OnScreenButtons.buttonY && canPressButton()) {
			interactWithNearbyNPC();
		}
	}
	
	/**
	 * Use the currently selected item
	 */
	private void useSelectedItem() {
		if (inventory.size() > 0) {
			buttonUsed = true;
			lastButtonPressTime = System.currentTimeMillis();
			
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
				game.showMessage("Item used");
			}
		}
	}
	
	/**
	 * Exit the shop
	 */
	private void exitShop() {
		inShop = false;
		buttonUsed = true;
		lastButtonPressTime = System.currentTimeMillis();
	}
	
	/**
	 * Buy the currently selected item from shop
	 */
	private void buySelectedItem() {
		if (currentlyBuyingFrom == null || selectedShopItem >= getCurrentShopInventorySize()) {
			return;
		}
		
		int itemPrice = currentlyBuyingFrom.inventory.get(selectedShopItem).price;
		
		if (money >= itemPrice) {
			money -= itemPrice;
			
			// Add weapons to front of inventory for quick access
			if (currentlyBuyingFrom.inventory.get(selectedShopItem) instanceof ItemPistol) {
				inventory.add(0, currentlyBuyingFrom.getSale(selectedShopItem));
			} else {
				inventory.add(currentlyBuyingFrom.getSale(selectedShopItem));
			}
			
			// Show feedback and exit shop
			game.showMessage("Purchased: " + currentlyBuyingFrom.inventory.get(selectedShopItem).name);
			inShop = false;
			buttonUsed = true;
			lastButtonPressTime = System.currentTimeMillis();
		} else {
			game.showMessage("Not enough money!");
		}
	}
	
	/**
	 * Interact with nearby NPCs
	 */
	private void interactWithNearbyNPC() {
		for (MovingObject object : game.world.blocks) {
			if (object instanceof Bot) {
				if (object.distanceToPlayer() < 6 * game.tileSize) {
					if (((Human)object).team == team) {
						((Bot) object).interact();
						buttonUsed = true;
						lastButtonPressTime = System.currentTimeMillis();
						break;
					}
				}
			}
		}
	}
	
	/**
	 * Update player health status
	 */
	private void updateHealthStatus() {
		if (health <= 0) {
			game.setGameOver();
		} else if (health < 100) {
			// Slowly regenerate health
			health++;
		}
	}
	
	/**
	 * Check if any action button is pressed
	 */
	private boolean anyActionButtonPressed() {
		return OnScreenButtons.buttonX || OnScreenButtons.dPadUp || 
			   OnScreenButtons.dPadDown || OnScreenButtons.buttonY || 
			   OnScreenButtons.dPadLeft || OnScreenButtons.dPadRight;
	}
	
	/**
	 * Check if enough time has passed to register a new button press
	 */
	private boolean canPressButton() {
		return !buttonUsed && 
			   (System.currentTimeMillis() - lastButtonPressTime > BUTTON_COOLDOWN);
	}
	
	/**
	 * Get the size of the current shop's inventory
	 */
	private int getCurrentShopInventorySize() {
		return (currentlyBuyingFrom != null && currentlyBuyingFrom.inventory != null) ? 
				currentlyBuyingFrom.inventory.size() : 0;
	}
	
	/**
	 * Get the name of the currently selected item
	 */
	private String getSelectedItemName() {
		return (inventory.size() > 0 && selectedItem < inventory.size()) ? 
				inventory.get(selectedItem).name : "none";
	}
}
