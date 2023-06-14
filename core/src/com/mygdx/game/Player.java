package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player extends Entity{

    //Player Character
    Texture playerTexture;
    Sprite playerSprite;


    private Direction direction;
    private float attackCooldown;
    private final float attackCooldownTime = 1.0f;
    private boolean canAttack;

    public Player(Texture texture){
        super(texture);
        direction = Direction.DOWN;
        attackCooldown = 0;
        canAttack = true;
    }
    @Override
    public void update() {
        attackCooldown -= Gdx.graphics.getDeltaTime();
        if (attackCooldown < 0) {
            attackCooldown = 0;
            canAttack = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && canAttack) {
            attack();
        }
        updateDirection();
    }

    private void attack() {
        canAttack = false;
        attackCooldown = attackCooldownTime;

        float targetX = getX();
        float targetY = getY();
        switch (direction) {
            case UP:
                targetY += 24;
                break;
            case RIGHT:
                targetX += 24;
                break;
            case DOWN:
                targetY -= 24;
                break;
            case LEFT:
                targetX -= 24;
                break;
        }

        Entity target = getEntityAt(targetX, targetY);
        if (target != null) {
            target.updateHealthPoints(-1);
        }
    }

    public void updateDirection() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            direction = Direction.UP;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            direction = Direction.RIGHT;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            direction = Direction.DOWN;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            direction = Direction.LEFT;
        }
    }
    public void setDirection(Direction d){
        direction = d;
    }

    private Entity getEntityAt(float x, float y) {
        return null;
    }

    @Override
    public void render() {

    }

    public void Draw(SpriteBatch batch){
        playerSprite.setPosition(0,0);
        playerSprite.draw(batch);
    }
}
