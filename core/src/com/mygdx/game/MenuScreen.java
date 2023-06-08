package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

public class MenuScreen extends ScreenAdapter {
    private Stage stage;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Image backgroundImage;
    private TextButton newGameButton;
    private TextButton continueGameButton;
    private TextButton exitButton;

    // Snowflake properties
    private Texture snowflakeTexture;
    private float[] snowflakes;
    private static final int SNOWFLAKE_COUNT = 200;

    public MenuScreen() {
        stage = new Stage();
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);

        // Load the background texture and create an image actor
        backgroundTexture = new Texture("background.jpg"); // Customize the background image path as per your needs
        backgroundImage = new Image(backgroundTexture);
        stage.addActor(backgroundImage);

        // Load the snowflake texture
        snowflakeTexture = new Texture("snowflake.png"); // Customize the snowflake image path as per your needs

        // Create and position the buttons
        newGameButton = createButton("New Game", Align.center, 100);
        continueGameButton = createButton("Continue Game", Align.center, 50);
        exitButton = createButton("Exit", Align.center, 0);

        // Add button listeners
        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // TODO: Handle "New Game" button click
            }
        });

        continueGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // TODO: Handle "Continue Game" button click
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // TODO: Handle "Exit" button click
                Gdx.app.exit(); // Close the application
            }
        });

        // Add buttons to the stage
        stage.addActor(newGameButton);
        stage.addActor(continueGameButton);
        stage.addActor(exitButton);

        // Generate random snowflake positions
        generateSnowflakes();
    }

    private TextButton createButton(String text, int align, float y) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        // Customize the button style (font, color, etc.) as per your needs

        TextButton button = new TextButton(text, style);
        button.setSize(200, 60); // Customize button size as per your needs
        button.setPosition((Gdx.graphics.getWidth() - button.getWidth()) / 2, y, align);
        return button;
    }

    private void generateSnowflakes() {
        snowflakes = new float[SNOWFLAKE_COUNT * 2];
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        for (int i = 0; i < snowflakes.length; i += 2) {
            float x = (float) (Math.random() * width);
            float y = (float) (Math.random() * height);
            snowflakes[i] = x;
            snowflakes[i + 1] = y;
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        // Draw the background image
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw the snowflakes
        for (int i = 0; i < snowflakes.length; i += 2) {
            float x = snowflakes[i];
            float y = snowflakes[i + 1];
            batch.draw(snowflakeTexture, x, y);
        }

        batch.end();

        // Draw the stage
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        // Dispose resources
        stage.dispose();
        batch.dispose();
        backgroundTexture.dispose();
        snowflakeTexture.dispose();
    }
}
