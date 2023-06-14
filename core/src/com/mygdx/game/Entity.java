package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public abstract class Entity {

    private Sprite sprite;

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
    private float moveSpeed;
    private Texture texture;

    public Entity(Texture texture) {
        this.sprite = new Sprite(texture);
        sprite.setSize(24,24);
        this.texture = texture;
        jumpable = false;
        healthPoints = 1;
        attackPoints = 1;
        defensePoints = 0;
        moveSpeed = 200.0f;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
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

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(int speedPoints) {
        this.moveSpeed = speedPoints;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
        this.sprite = new Sprite(texture);
    }

    public abstract void update();

    public abstract void render();
}