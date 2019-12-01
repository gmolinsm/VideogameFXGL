package sample;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.settings.GameSettings;
import javafx.scene.input.KeyCode;

public class Main extends GameApplication {

    private static final int WIDTH = 1540;
    private static final int HEIGHT = 840;
    private Entity player;

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(WIDTH);
        gameSettings.setHeight(HEIGHT);
        gameSettings.setTitle("Game");
        gameSettings.setVersion("0.5.4");
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new GameEntityFactory());
        getGameWorld().setLevelFromMap("level0.json");
        player = getGameWorld().spawn("player", getWidth()/2, getHeight()/2);
    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerController.class).left();
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerController.class).right();
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onActionBegin() {
                player.getComponent(PlayerController.class).jump();
            }
        }, KeyCode.W);
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(GameTypes.PLAYER, GameTypes.CRATE) {

            @Override
            protected void onCollisionBegin(Entity player, Entity crate) {
                crate.removeFromWorld();
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(GameTypes.PLAYER, GameTypes.DEATHZONE) {

            @Override
            protected void onCollisionBegin(Entity player, Entity deathzone) {
                player.removeFromWorld();
                player = getGameWorld().spawn("player", getWidth()/2, getHeight()/2);
            }
        });

    }

    /*@Override
    protected void initUI() {
    }*/


    public static void main(String args[]) {
        launch(args);
    }
}
