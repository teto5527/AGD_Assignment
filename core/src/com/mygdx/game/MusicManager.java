package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class MusicManager {
    private Music music;

    public MusicManager(String filePath) {
        music = Gdx.audio.newMusic(Gdx.files.internal(filePath));
        music.setLooping(true);
    }

    public void play() {
        Gdx.app.log("MusicManager: ","Music palyed");
        music.play();
    }

    public void pause() {
        Gdx.app.log("MusicManager: ","Music paused");
        music.pause();
    }

    public void stop() {
        Gdx.app.log("MusicManager: ","Music stopped");
        music.stop();
    }

    public void dispose() {
        music.dispose();
    }
}