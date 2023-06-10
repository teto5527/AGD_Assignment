package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class MenuScreen implements Screen {

    MyGdxGame game; // Note it’s "MyGdxGame" not "Game"
    private SpriteBatch batch;
    private Skin skin;
    private Stage stage;
    private int screenWidth;
    private int screenHeight;
    private Texture backgroundTexture;
    private Image backgroundImage;
    private TextButton newGameButton;
    private TextButton continueGameButton;
    private TextButton exitButton;

    // constructor to keep a reference to the main Game class
    public MenuScreen(MyGdxGame game) {
        this.game = game;
    }



    public void create() {
        Gdx.app.log("MenuScreen: ","menuScreen create");
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("gui/uiskin.json"));
        stage = new Stage();

        backgroundTexture = new Texture("PNG/Back Ground/background.png");
        backgroundImage = new Image(backgroundTexture);
        stage.addActor(backgroundImage);

        newGameButton = createButton("New Game", Align.center, 750);
        continueGameButton = createButton("Continue Game", Align.center, 500);
        exitButton = createButton("Exit", Align.center, 250);

        stage.addActor(newGameButton);
        stage.addActor(continueGameButton);
        stage.addActor(exitButton);
        Gdx.input.setInputProcessor(stage);

        newGameButton.addListener(new ClickListener()
        {
            @Override
            public void clicked (InputEvent event, float x, float y)
            {
                Gdx.app.exit();
            }
        });
        continueGameButton.addListener(new ClickListener()
        {
            @Override
            public void clicked (InputEvent event, float x, float y)
            {
                Gdx.app.exit();
            }
        });
        exitButton.addListener(new ClickListener()
        {
            @Override
            public void clicked (InputEvent event, float x, float y)
            {
                Gdx.app.exit();
            }
        });
    }

    public void render(float f) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        stage.draw();
        batch.end();
    }

    public TextButton createButton(String text, int align, float y) {
        TextButton button = new TextButton(text, skin, "default");
        button.setSize(600, 180);
        float x = (stage.getWidth() - button.getWidth()) / 2 + button.getWidth() / 2;
        button.setPosition(x, y, align);
        return button;
    }

    @Override
    public void dispose() { }

    @Override
    public void resize(int width, int height) { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void show() {
        Gdx.app.log("MenuScreen: ","menuScreen show called");
        create();
    }

    @Override
    public void hide() {
        Gdx.app.log("MenuScreen: ","menuScreen hide called");
    }
}