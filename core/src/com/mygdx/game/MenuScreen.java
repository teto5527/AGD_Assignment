package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

public class MenuScreen implements Screen {

    MyGdxGame game; // Note it’s "MyGdxGame" not "Game"
    private SpriteBatch batch;
    private Skin skin;
    private Stage stage;
    private Music backgroundMusic;

    // constructor to keep a reference to the main Game class
    public MenuScreen(MyGdxGame game) {
        this.game = game;
    }



    public void create() {
        Gdx.app.log("MenuScreen: ","menuScreen create");
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/Eight_Bit_Adventure.wav"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("gui/uiskin.json"));
        stage = new Stage();
        TextureRegion buttonRegion = new TextureRegion(new Texture("PNG/UI/OrangeBtn1.png"));
        TextureRegionDrawable buttonDrawable = new TextureRegionDrawable(buttonRegion);
        final TextButton exitButton = new TextButton("EXIT", skin, "default");
        final TextButton playButton = new TextButton("PLAY", skin, "default");
        exitButton.getLabel().setFontScale(6);
        playButton.getLabel().setFontScale(6);
        //set the size of buttons
        exitButton.setWidth(800f);
        exitButton.setHeight(300f);
        playButton.setWidth(800f);
        playButton.setHeight(300f);
        //set the position of buttons, the position we set here is the upper left corner of the buttons.
        exitButton.setPosition(Gdx.graphics.getWidth() /2 - 400, Gdx.graphics.getHeight()/2 - 350f);
        playButton.setPosition(Gdx.graphics.getWidth() /2 - 400, Gdx.graphics.getHeight()/2 + 50f);
        exitButton.getStyle().up = buttonDrawable;
        stage.addActor(exitButton);
        stage.addActor(playButton);
        Gdx.input.setInputProcessor(stage);
        exitButton.addListener(new ClickListener()
        {
            @Override
            public void clicked (InputEvent event, float x, float y)
            {
                //exit -> return to the OS
                Gdx.app.exit();
            }
        });
        playButton.addListener(new ClickListener()
        {
            @Override
            public void clicked (InputEvent event, float x, float y)
            {
                //play -> go to GameScreen
                game.setScreen(MyGdxGame.gameScreen);
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

    @Override
    public void dispose() {
        backgroundMusic.stop();
        backgroundMusic.dispose();
    }

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