package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class EnemyBoss extends Entity{
    final private int xpReward;

    public EnemyBoss(Texture texture){
        super(texture);
        this.updateHealthPoints(2);
        this.setDefensePoints(1);

        xpReward = 5;
    }

    public int getXpReward() {
        return xpReward;
    }

    @Override
    public void update() {

    }

    @Override
    public void render() {

    }
}
