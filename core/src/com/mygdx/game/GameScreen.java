package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class GameScreen extends ScreenAdapter {

    public enum GameState { PLAYING, COMPLETE }

    public static final float MOVEMENT_SPEED = 200.0f;
    public static final float GOAL_BOB_HEIGHT = 5.0f;

    GameState gameState = GameState.PLAYING;

    //Map and rendering
    SpriteBatch spriteBatch;
    SpriteBatch uiBatch; //Second SpriteBatch without camera transforms, for drawing UI
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    OrthographicCamera camera;

    //Game clock
    float dt;

    //Player Character
    Texture playerTexture;
    Sprite playerSprite;
    Vector2 playerDelta;
    Rectangle playerDeltaRectangle;

    //Goal
    Texture goalTexture;
    Sprite goalSprite;
    Vector2 goalPosition;
    float goalBobSine;

    //BMFont Text
    BitmapFont bmfont;

    //Overhead Layer Opacity
    Rectangle opacityTrigger = new Rectangle();
    float overheadOpacity = 1f;

    //Storage class for collision
    Rectangle tileRectangle;

    //UI textures
    Texture buttonSquareTexture;
    Texture buttonSquareDownTexture;
    Texture buttonLongTexture;
    Texture buttonLongDownTexture;

    //UI Buttons
    Button moveLeftButton;
    Button moveRightButton;
    Button moveDownButton;
    Button moveUpButton;
    Button restartButton;
    //Just use this to only restart when the restart button is released instead of immediately as it's pressed
    boolean restartActive;

    /**Setup when the game is opened. It is important to set default values in here instead of
     * during instantiation, as those values aren't always reset after closing and reopening the
     * application. */

    @Override
    public void show () {

        //LibGDX Settings
        Gdx.app.setLogLevel(Application.LOG_DEBUG); //Allows sending messages to Logcat

        //Rendering
        spriteBatch = new SpriteBatch();
        uiBatch = new SpriteBatch();
        camera = new OrthographicCamera();

        tiledMap = new TmxMapLoader().load("other/MageCity.tmx");
        tiledMapRenderer = new LayerRenderer(tiledMap);
        //TODO:tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, spriteBatch);

        //Camera
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, w/h * 250, 250);

        //Textures
        playerTexture = new Texture("PNG/Other/player.png");
        goalTexture = new Texture("PNG/Other/goal.png");
        buttonSquareTexture = new Texture("buttons/buttonSquare_blue.png");
        buttonSquareDownTexture = new Texture("buttons/buttonSquare_beige_pressed.png");
        buttonLongTexture = new Texture("buttons/buttonLong_blue.png");
        buttonLongDownTexture = new Texture("buttons/buttonLong_beige_pressed.png");

        //Player
        playerSprite = new Sprite(playerTexture);
        playerSprite.setSize(24, 24);
        playerDelta = new Vector2();
        playerDeltaRectangle = new Rectangle(0, 0, playerSprite.getWidth(), playerSprite.getHeight());

        //Goal
        goalSprite = new Sprite(goalTexture);
        goalPosition = new Vector2(0,0);
        goalBobSine = 0.0f;

        //Collision
        tileRectangle = new Rectangle();
        MapLayer collisionLayer = tiledMap.getLayers().get("Collision");
        TiledMapTileLayer tileLayer = (TiledMapTileLayer) collisionLayer;
        tileRectangle.width = tileLayer.getTileWidth();
        tileRectangle.height = tileLayer.getTileHeight();

        //Buttons
        float buttonSize = h * 0.2f;
        moveLeftButton = new Button(0.0f, buttonSize, buttonSize, buttonSize, buttonSquareTexture, buttonSquareDownTexture);
        moveRightButton = new Button(buttonSize*2, buttonSize, buttonSize, buttonSize, buttonSquareTexture, buttonSquareDownTexture);
        moveDownButton = new Button(buttonSize, 0.0f, buttonSize, buttonSize, buttonSquareTexture, buttonSquareDownTexture);
        moveUpButton = new Button(buttonSize, buttonSize*2, buttonSize, buttonSize, buttonSquareTexture, buttonSquareDownTexture);
        restartButton = new Button(w/2 - buttonSize*2, h * 0.2f, buttonSize*4, buttonSize, buttonLongTexture, buttonLongDownTexture);

        //BMFont
        bmfont = new BitmapFont(
                Gdx.files.internal("other/good_neighbors_unity.fnt"),
                Gdx.files.internal("other/good_neighbors_unity.png"),
                false);
        // Scale up the font slightly to make it more legible on larger screens
        bmfont.getData().setScale(2, 2);

        newGame();
    }

    private void newGame() {
        gameState = GameState.PLAYING;

        //Translate camera to center of screen
        camera.position.x = 16;
        camera.position.y = 16;

        dt = 0.0f;

        MapLayer objectLayer = tiledMap.getLayers().get("Objects");

        //Player start location, loaded from the tilemaze's object layer.
        RectangleMapObject playerObject = (RectangleMapObject) objectLayer.getObjects().get("Player");
        playerSprite.setCenter(playerObject.getRectangle().x, playerObject.getRectangle().y);
        camera.translate(playerSprite.getX(), playerSprite.getY());

        //Goal Location
        RectangleMapObject goalObject = (RectangleMapObject) objectLayer.getObjects().get("Goal");
        goalPosition.x = goalObject.getRectangle().x - 16;
        goalPosition.y = goalObject.getRectangle().y - 16;

        //TODO Load Opacity Trigger Volume
        RectangleMapObject opacityObject = (RectangleMapObject)objectLayer.getObjects().get("Fadeout");
        opacityTrigger.set(opacityObject.getRectangle());

        restartActive = false;
    }

    @Override
    public void render(float delta) {
        dt = delta;

        // Clear the screen
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleInput();
        update();
        renderGame();
        renderUI();
    }

    /** Handle user input. Called from render(). */
    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerDelta.x = -MOVEMENT_SPEED * dt;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerDelta.x = MOVEMENT_SPEED * dt;
        } else {
            playerDelta.x = 0;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            playerDelta.y = MOVEMENT_SPEED * dt;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            playerDelta.y = -MOVEMENT_SPEED * dt;
        } else {
            playerDelta.y = 0;
        }
    }

    /** Update the game world. Called from render(). */
    private void update() {
        // Update player position
        playerSprite.setX(playerSprite.getX() + playerDelta.x);
        playerSprite.setY(playerSprite.getY() + playerDelta.y);

        // Update goal bobbing
        goalBobSine += dt * 2.0f * MathUtils.PI;
        goalSprite.setY(goalPosition.y + MathUtils.sin(goalBobSine) * GOAL_BOB_HEIGHT);

        // Check collision with goal
        if (playerSprite.getBoundingRectangle().overlaps(goalSprite.getBoundingRectangle())) {
            gameState = GameState.COMPLETE;
        }
    }

    /** Render the game world. Called from render(). */
    private void renderGame() {
        // Set the camera matrix
        spriteBatch.setProjectionMatrix(camera.combined);

        // Begin rendering
        spriteBatch.begin();

        // Render the tiled map
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        // Render the player sprite
        playerSprite.draw(spriteBatch);

        // Render the goal sprite
        goalSprite.draw(spriteBatch);

        // End rendering
        spriteBatch.end();
    }

    /** Render the UI. Called from render(). */
    private void renderUI() {
        // Begin rendering UI
        uiBatch.begin();

        // Render UI buttons
        moveLeftButton.draw(uiBatch);
        moveRightButton.draw(uiBatch);
        moveDownButton.draw(uiBatch);
        moveUpButton.draw(uiBatch);
        restartButton.draw(uiBatch);

        // End rendering UI
        uiBatch.end();
    }

    /** Called when the application is resized.
     *  @param width The new width of the application window.
     *  @param height The new height of the application window. */
    @Override
    public void resize(int width, int height) {
        // Update camera viewport
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    /** Called when the application is paused. */
    @Override
    public void pause() {
        // Add code here if needed
    }

    /** Called when the application is resumed. */
    @Override
    public void resume() {
        // Add code here if needed
    }

    /** Called when the screen is hidden. */
    @Override
    public void hide() {
        // Add code here if needed
    }

    /** Called when the screen is disposed. */
    @Override
    public void dispose() {
        // Dispose resources
        spriteBatch.dispose();
        uiBatch.dispose();
        tiledMap.dispose();
        playerTexture.dispose();
        goalTexture.dispose();
        bmfont.dispose();
        buttonSquareTexture.dispose();
        buttonSquareDownTexture.dispose();
        buttonLongTexture.dispose();
        buttonLongDownTexture.dispose();
    }
}