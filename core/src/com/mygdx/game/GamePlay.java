package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

public class GamePlay extends ScreenAdapter {

    public enum GameState { PLAYING, COMPLETE }
    GameState gameState = GameState.PLAYING;

    //Game clock
    float dt;
    public static final float GOAL_BOB_HEIGHT = 5.0f;

    //Map and rendering
    SpriteBatch spriteBatch;
    SpriteBatch uiBatch; //Second SpriteBatch without camera transforms, for drawing UI
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    OrthographicCamera camera;

    //Player Character
    Texture playerTexture;
    Player player;
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
    Button attckButton;
    Button restartButton;
    //Just use this to only restart when the restart button is released instead of immediately as it's pressed
    boolean restartActive;
    private MusicManager backgroundMusic;


    @Override
    public void show() {
        //LibGDX Settings
        Gdx.app.log("GameScreen: ","gameScreen show called");
        Gdx.app.setLogLevel(Application.LOG_DEBUG); //Allows sending messages to Logcat

        //Music
        backgroundMusic = new MusicManager("music/Eight_Bit_Adventure.wav");
        backgroundMusic.play();

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
        player = new Player(playerTexture);
        playerSprite = player.getSprite();
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
        attckButton = new Button(w - buttonSize*2, 0.0f, buttonSize*2, buttonSize, buttonLongTexture, buttonLongDownTexture);
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
    public void render (float delta) {
        dt = delta;
        update();

        //Clear the screen every frame before drawing.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA); //Allows transparent sprites/tiles
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        tiledMapRenderer.setView(camera);

        TiledMapTileLayer overhead = (TiledMapTileLayer) tiledMap.getLayers().get("Overhead");
        for (MapLayer l: tiledMap.getLayers()) {
            if (! l.isVisible() || l == overhead || ! (l instanceof TiledMapTileLayer)) {
                continue;
            }
            tiledMapRenderer.renderTileLayer((TiledMapTileLayer) l);
        }


        //Apply camera to spritebatch and draw player
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        goalSprite.draw(spriteBatch);
        playerSprite.draw(spriteBatch);
        spriteBatch.end();

        overhead.setOpacity(MathUtils.lerp(0.8f, 1.0f, overheadOpacity));

        tiledMapRenderer.renderTileLayer(overhead);

        renderUI();
    }


    /** Render the UI. Called from render(). */
    private void renderUI() {
        uiBatch.begin();
        switch(gameState) {
            //if gameState is Running: Draw Controls
            case PLAYING: {
                moveLeftButton.draw(uiBatch);
                moveRightButton.draw(uiBatch);
                moveDownButton.draw(uiBatch);
                moveUpButton.draw(uiBatch);
                attckButton.draw(uiBatch);

                // This text will display once the entire exercise is completed
                bmfont.setColor(1f, 1f, 1f, 1f - overheadOpacity);
                bmfont.draw(uiBatch, "HIDDEN", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() * 5 / 6, 0f, 1, false);
            } break;
            //if gameState is Complete: Draw Restart button
            case COMPLETE: {
                restartButton.draw(uiBatch);
            } break;
        }
        uiBatch.end();
    }

    private void update() {
        //Touch Input Info
        boolean checkTouch = Gdx.input.isTouched();
        int touchX = Gdx.input.getX();
        int touchY = Gdx.input.getY();

        //Update Game State based on input
        switch (gameState) {

            case PLAYING:
                //Poll user for input
                moveLeftButton.update(checkTouch, touchX, touchY);
                moveRightButton.update(checkTouch, touchX, touchY);
                moveDownButton.update(checkTouch, touchX, touchY);
                moveUpButton.update(checkTouch, touchX, touchY);
                attckButton.update(checkTouch, touchX, touchY);

                int moveX = 0;
                int moveY = 0;
                if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT) || moveLeftButton.isDown) {
                    moveLeftButton.isDown = true;
                    player.setDirection(Entity.Direction.LEFT);
                    moveX -= 1;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT) || moveRightButton.isDown) {
                    moveRightButton.isDown = true;
                    player.setDirection(Entity.Direction.RIGHT);
                    moveX += 1;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN) || moveDownButton.isDown) {
                    moveDownButton.isDown = true;
                    player.setDirection(Entity.Direction.DOWN);
                    moveY -= 1;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP) || moveUpButton.isDown) {
                    moveUpButton.isDown = true;
                    player.setDirection(Entity.Direction.UP);
                    moveY += 1;
                }
                if (attckButton.isDown) {
                    attckButton.isDown = true;
                    //TODO:attck()
                }

                //TODO Determine Character Movement Distance
                playerDelta.x = moveX * player.getMoveSpeed() * dt;
                playerDelta.y = moveY * player.getMoveSpeed() * dt;

                //TODO Check movement against grid
                if (playerDelta.len2() > 0) { //Don't do anything if we're not moving
                    //Retrieve Collision layer
                    MapLayer collisionLayer = tiledMap.getLayers().get("Collision");
                    TiledMapTileLayer tileLayer = (TiledMapTileLayer) collisionLayer;

                    //TODO Determine bounds to check within
                    // Find top-right corner tile
                    int right = (int) Math.ceil(Math.max(playerSprite.getX() + playerSprite.getWidth(),playerSprite.getX() + playerSprite.getWidth() + playerDelta.x));
                    int top = (int) Math.ceil(Math.max(playerSprite.getY() + playerSprite.getHeight(),playerSprite.getY() + playerSprite.getHeight() + playerDelta.y));

                    // Find bottom-left corner tile
                    int left = (int) Math.floor(Math.min(playerSprite.getX(),playerSprite.getX() + playerDelta.x));
                    int bottom = (int) Math.floor(Math.min(playerSprite.getY(),playerSprite.getY() + playerDelta.y));

                    // Divide bounds by tile sizes to retrieve tile indices
                    right /= tileLayer.getTileWidth();
                    top /= tileLayer.getTileHeight();
                    left /= tileLayer.getTileWidth();
                    bottom /= tileLayer.getTileHeight();
                    //TODO Loop through selected tiles and correct by each axis
                    //EXTRA: Try counting down if moving left or down instead of counting up
                    for (int y = bottom; y <= top; y++) {
                        for (int x = left; x <= right; x++) {
                            TiledMapTileLayer.Cell targetCell = tileLayer.getCell(x, y);
                            // If the cell is empty, ignore it
                            if (targetCell == null) continue;
                            // Otherwise correct against tested squares
                            tileRectangle.x = x * tileLayer.getTileWidth();
                            tileRectangle.y = y * tileLayer.getTileHeight();

                            playerDeltaRectangle.x = playerSprite.getX() + playerDelta.x;
                            playerDeltaRectangle.y = playerSprite.getY();
                            if (tileRectangle.overlaps(playerDeltaRectangle)) playerDelta.x = 0;

                            playerDeltaRectangle.x = playerSprite.getX();
                            playerDeltaRectangle.y = playerSprite.getY() + playerDelta.y;
                            if (tileRectangle.overlaps(playerDeltaRectangle)) playerDelta.y = 0;
                        }
                    }

                    //TODO Move player and camera
                    playerSprite.translate(playerDelta.x, playerDelta.y);
                    camera.translate(playerDelta);
                }

                //TODO Check if player has met the winning condition
                if (playerSprite.getBoundingRectangle().overlaps(goalSprite.getBoundingRectangle())) {
                    //Player has won!
                    gameState = GameState.COMPLETE;
                }

                //TODO Calculate overhead layer opacity
                if (playerSprite.getBoundingRectangle().overlaps(opacityTrigger)) {
                    overheadOpacity -= dt * 5.0f;
                } else {
                    overheadOpacity += dt * 5.0f;
                }
                overheadOpacity = MathUtils.clamp(overheadOpacity, 0.0f, 1.0f);

                break;

            case COMPLETE:
                //Poll for input
                restartButton.update(checkTouch, touchX, touchY);

                if (Gdx.input.isKeyPressed(Input.Keys.DPAD_CENTER) || restartButton.isDown) {
                    restartButton.isDown = true;
                    restartActive = true;
                } else if (restartActive) {
                    newGame();
                }
                break;
        }

        goalBobSine += dt;
        goalBobSine %= Math.PI;
        goalSprite.setPosition(goalPosition.x, goalPosition.y + (GOAL_BOB_HEIGHT / 2.0f) -
                (GOAL_BOB_HEIGHT * (float) Math.sin(goalBobSine)));
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }


    @Override
    public void dispose () {
        tiledMap.dispose();

        playerTexture.dispose();
        buttonSquareTexture.dispose();
        buttonSquareDownTexture.dispose();
        buttonLongTexture.dispose();
        buttonLongDownTexture.dispose();
    }
}
