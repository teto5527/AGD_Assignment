package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public abstract class Entity {

    private Sprite entitySprite;

    public enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT;


        public Direction getOpposite() {
            if (this == LEFT) {
                return RIGHT;
            } else if (this == RIGHT) {
                return LEFT;
            } else if (this == UP) {
                return DOWN;
            } else {
                return UP;
            }
        }
    }

    private boolean jumpable;
    private int healthPoints;
    private int attackPoints;
    private int defensePoints;
    private int speedPoints;
    private Texture texture;
    private Sprite sprite;

    public Entity(Texture texture) {
        this.entitySprite = entitySprite;

        this.texture = texture;
        this.sprite = new Sprite(texture);
        jumpable = false;
        healthPoints = 1;
        attackPoints = 1;
        defensePoints = 0;
        speedPoints = 1;
    }

    public Sprite getEntitySprite() {
        return entitySprite;
    }

    public float getX() {
        return entitySprite.getX();
    }

    public float getY() {
        return entitySprite.getY();
    }

    public boolean isJumpable() {
        return jumpable;
    }

    public void setJumpable(boolean jumpable) {
        this.jumpable = jumpable;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void updateHealthPoints(int healthPoints) {
        this.healthPoints += healthPoints;
    }

    public int getAttackPoints() {
        return attackPoints;
    }

    public void setAttackPoints(int attackPoints) {
        this.attackPoints = attackPoints;
    }

    public int getDefensePoints() {
        return defensePoints;
    }

    public void setDefensePoints(int defensePoints) {
        this.defensePoints = defensePoints;
    }

    public int getSpeedPoints() {
        return speedPoints;
    }

    public void setSpeedPoints(int speedPoints) {
        this.speedPoints = speedPoints;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
        this.sprite = new Sprite(texture);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public abstract void update();

    public abstract void render();
}