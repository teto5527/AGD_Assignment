package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Snowfall extends ApplicationAdapter {
    private ShapeRenderer shapeRenderer;
    private float[] snowflakes;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        snowflakes = generateSnowflakes();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 1);

        for (int i = 0; i < snowflakes.length; i += 2) {
            float x = snowflakes[i];
            float y = snowflakes[i + 1];
            shapeRenderer.rect(x, y, 1, 1);
        }

        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    private float[] generateSnowflakes() {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        int flakesCount = (width * height) / 100; // Adjust the density of snowflakes

        float[] flakes = new float[flakesCount * 2];

        for (int i = 0; i < flakes.length; i += 2) {
            float x = (float) (Math.random() * width);
            float y = (float) (Math.random() * height);
            flakes[i] = x;
            flakes[i + 1] = y;
        }

        return flakes;
    }
}