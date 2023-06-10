package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class BackgroundMusic {
    private Music music;

    public BackgroundMusic(String filePath) {
        music = Gdx.audio.newMusic(Gdx.files.internal(filePath));
        music.setLooping(true);
    }

    public void play() {
        music.play();
    }

    public void pause() {
        music.pause();
    }

    public void stop() {
        music.stop();
    }

    public void dispose() {
        music.dispose();
    }
}