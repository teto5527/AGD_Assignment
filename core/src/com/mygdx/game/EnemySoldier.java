package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class EnemySoldier extends Entity{
    final private int xpReward;

    public EnemySoldier(Texture texture){
        super(texture);
        xpReward = 1;
    }

    public int getXpReward() {
        return xpReward;
    }


    @Override
    public void update() {
        if(this.getHealthPoints() == 0){
        }

    }

    @Override
    public void render() {

    }
}
