package com.demolitionwars.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.demolitionwars.DemolitionWarsGame;
import com.demolitionwars.entities.*;
import com.demolitionwars.world.GameWorld;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Main game screen where gameplay takes place.
 * Complete implementation with Box2D physics.
 */
public class GameScreen implements Screen, ContactListener {
    
    private final DemolitionWarsGame game;
    
    // Rendering
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private BitmapFont font;
    
    // Physics
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private static final float TIME_STEP = 1/60f;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;
    private float accumulator = 0;
    
    // Game entities
    private Player player;
    private GameWorld gameWorld;
    private List<GameEntity> entities;
    private List<GameEntity> entitiesToAdd;
    private List<GameEntity> entitiesToRemove;
    
    // Game state
    private boolean gameOver = false;
    private boolean playerWon = false;
    private String gameOverMessage = "";
    
    public GameScreen(DemolitionWarsGame game) {
        this.game = game;
        this.entities = new ArrayList<>();
        this.entitiesToAdd = new ArrayList<>();
        this.entitiesToRemove = new ArrayList<>();
    }
    
    @Override
    public void show() {
        Gdx.app.log("GameScreen", "Initializing game screen");
        
        // Initialize Box2D
        Box2D.init();
        
        // Create physics world with gravity (stronger gravity for better feel)
        world = new World(new Vector2(0, -50f), true);
        world.setContactListener(this);
        
        // Set up rendering with proper viewport size
        // Using larger viewport to see more of the world (match original game's feel)
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(2400, 1600, camera);
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(3f);
        
        // Debug renderer for physics
        debugRenderer = new Box2DDebugRenderer();
        
        // Initialize game world
        gameWorld = new GameWorld(world);
        
        // Create player at spawn position (matching original)
        // Original spawns at (2974, 2848) in world coordinates
        player = new Player(world, 2974, 2848);
        entities.add(player);
        
        // Generate world terrain and enemies
        gameWorld.generate(entities);
        
        Gdx.app.log("GameScreen", "Game screen ready with " + entities.size() + " entities");
    }
    
    @Override
    public void render(float delta) {
        if (gameOver) {
            renderGameOver(delta);
            return;
        }
        
        // Update physics
        updatePhysics(delta);
        
        // Update game logic
        update(delta);
        
        // Update camera to follow player
        if (player != null && player.getBody() != null) {
            Vector2 playerPos = player.getBody().getPosition();
            camera.position.set(playerPos.x, playerPos.y, 0);
            
            // Clamp camera to world bounds
            float halfViewportWidth = viewport.getWorldWidth() / 2;
            float halfViewportHeight = viewport.getWorldHeight() / 2;
            camera.position.x = Math.max(halfViewportWidth, Math.min(DemolitionWarsGame.WORLD_WIDTH - halfViewportWidth, camera.position.x));
            camera.position.y = Math.max(halfViewportHeight, Math.min(DemolitionWarsGame.WORLD_HEIGHT - halfViewportHeight, camera.position.y));
        }
        
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        
        // Render game entities
        batch.begin();
        for (GameEntity entity : entities) {
            if (entity.isActive()) {
                entity.render(batch);
            }
        }
        batch.end();
        
        // Render UI
        renderUI();
        
        // Debug rendering
        if (Gdx.input.isKeyPressed(Input.Keys.F1)) {
            debugRenderer.render(world, camera.combined);
        }
    }
    
    private void updatePhysics(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        
        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            accumulator -= TIME_STEP;
        }
    }
    
    private void update(float delta) {
        // Handle input
        handleInput(delta);
        
        // Update all entities
        for (GameEntity entity : entities) {
            if (entity.isActive()) {
                entity.update(delta);
            }
        }
        
        // Add/remove entities
        entities.addAll(entitiesToAdd);
        entitiesToAdd.clear();
        
        for (GameEntity entity : entitiesToRemove) {
            entities.remove(entity);
            if (entity.getBody() != null) {
                world.destroyBody(entity.getBody());
            }
        }
        entitiesToRemove.clear();
        
        // Check game over conditions
        checkGameOverConditions();
    }
    
    private void handleInput(float delta) {
        if (player == null || !player.isActive()) return;
        
        // Keyboard controls
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.moveLeft();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.moveRight();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.jump();
        }
        
        // Use item
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            player.useCurrentItem(this);
        }
        
        // Cycle items
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            player.previousItem();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            player.nextItem();
        }
        
        // Place explosive
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            placeExplosive();
        }
        
        // Touch controls for mobile
        handleTouchInput();
    }
    
    private void handleTouchInput() {
        if (!Gdx.input.isTouched()) return;
        
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        
        for (int i = 0; i < 5; i++) { // Support up to 5 touches
            if (!Gdx.input.isTouched(i)) continue;
            
            int touchX = Gdx.input.getX(i);
            int touchY = Gdx.input.getY(i);
            
            // Left side = move left
            if (touchX < screenWidth / 3) {
                player.moveLeft();
            }
            // Right side = move right
            else if (touchX > screenWidth * 2 / 3) {
                player.moveRight();
            }
            
            // Top of screen = jump
            if (touchY < screenHeight / 3) {
                player.jump();
            }
            // Bottom right corner = place explosive
            else if (touchX > screenWidth * 2 / 3 && touchY > screenHeight * 2 / 3) {
                placeExplosive();
            }
        }
    }
    
    private void placeExplosive() {
        if (player == null || player.getMoney() < 200) return;
        
        Vector2 pos = player.getBody().getPosition();
        Explosive explosive = new Explosive(world, Explosive.ExplosiveType.TNT, 
            pos.x + 100, pos.y, 200, 0);
        entitiesToAdd.add(explosive);
        player.addMoney(-200);
    }
    
    private void checkGameOverConditions() {
        // Check if player is dead
        if (player == null || !player.isActive()) {
            gameOver = true;
            playerWon = false;
            gameOverMessage = "Game Over!\n\nPress R to restart";
            return;
        }
        
        // Check if king is dead (win condition)
        for (GameEntity entity : entities) {
            if (entity instanceof King && !entity.isActive()) {
                gameOver = true;
                playerWon = true;
                gameOverMessage = "Victory!\n\nYou defeated the king!\nPress R to restart";
                return;
            }
        }
    }
    
    private void renderUI() {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        
        // Draw player stats in top-left corner (in screen space)
        float screenX = camera.position.x - viewport.getWorldWidth() / 2 + 20;
        float screenY = camera.position.y + viewport.getWorldHeight() / 2 - 40;
        
        if (player != null && player.isActive()) {
            font.draw(batch, "Health: " + player.getHealth() + "%", screenX, screenY);
            font.draw(batch, "Money: $" + player.getMoney(), screenX, screenY - 30);
            font.draw(batch, "Press F to place TNT ($200)", screenX, screenY - 60);
            font.draw(batch, "Use Arrow Keys/WASD to move, SPACE to jump", screenX, screenY - 90);
        }
        
        batch.end();
    }
    
    private void renderGameOver(float delta) {
        // Handle restart input
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            game.setScreen(new GameScreen(game));
            return;
        }
        
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        
        float centerX = camera.position.x - 200;
        float centerY = camera.position.y;
        
        if (playerWon) {
            font.setColor(Color.GREEN);
        } else {
            font.setColor(Color.RED);
        }
        
        // Draw game over message
        String[] lines = gameOverMessage.split("\n");
        for (int i = 0; i < lines.length; i++) {
            font.draw(batch, lines[i], centerX, centerY + (lines.length - i) * 40);
        }
        
        font.setColor(Color.WHITE);
        batch.end();
    }
    
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
    
    @Override
    public void pause() {
        // Handle Android pause
    }
    
    @Override
    public void resume() {
        // Handle Android resume
    }
    
    @Override
    public void hide() {
        // Screen is no longer active
    }
    
    @Override
    public void dispose() {
        Gdx.app.log("GameScreen", "Disposing resources");
        batch.dispose();
        world.dispose();
        debugRenderer.dispose();
        font.dispose();
    }
    
    // ContactListener implementation
    @Override
    public void beginContact(Contact contact) {
        Object userDataA = contact.getFixtureA().getBody().getUserData();
        Object userDataB = contact.getFixtureB().getBody().getUserData();
        
        // Player ground contact for jumping
        if (userDataA instanceof Player) {
            ((Player) userDataA).setCanJump(true);
        }
        if (userDataB instanceof Player) {
            ((Player) userDataB).setCanJump(true);
        }
        
        // Explosive collisions
        if (userDataA instanceof Explosive && userDataB instanceof Block) {
            handleExplosiveBlockCollision((Explosive) userDataA, (Block) userDataB);
        } else if (userDataB instanceof Explosive && userDataA instanceof Block) {
            handleExplosiveBlockCollision((Explosive) userDataB, (Block) userDataA);
        }
    }
    
    @Override
    public void endContact(Contact contact) {
        // Handle contact end
    }
    
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // Before physics resolution
    }
    
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // After physics resolution
    }
    
    private void handleExplosiveBlockCollision(Explosive explosive, Block block) {
        if (explosive.hasExploded()) {
            int money = block.takeDamage(explosive.getDamage());
            if (player != null) {
                player.addMoney(money);
            }
            if (!block.isActive()) {
                entitiesToRemove.add(block);
            }
        }
    }
    
    public void addEntity(GameEntity entity) {
        entitiesToAdd.add(entity);
    }
    
    public void removeEntity(GameEntity entity) {
        entitiesToRemove.add(entity);
    }
}
