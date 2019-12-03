package sample;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.settings.GameSettings;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

import java.util.List;
import java.util.Random;

public class Main extends GameApplication {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static Random random = new Random();
    private static List<Entity> spawners;
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
        spawners = getGameWorld().getEntitiesByType(GameTypes.SPAWN);
        player = getGameWorld().spawn("player", shuffle());
        getGameScene().getViewport().bindToEntity(player, getWidth()/2, getHeight()/2);

    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).left();
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).right();
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onActionBegin() {
                player.getComponent(PlayerComponent.class).jump();
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
                player.getComponent(PlayerComponent.class).playerFell = true;
            }
        });

    }

    @Override
    protected void onUpdate(double tpf) {
        PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);
        if(playerComponent.playerFell) {
            playerComponent.damage();
            playerComponent.playerFell = false;
            getMasterTimer().runOnceAfter(() -> {
                player.getComponent(PhysicsComponent.class).reposition(shuffle());
                playerComponent.playerHurt = false;
            }, Duration.seconds(1));

        }
    }
    /*@Override
    protected void initUI() {
    }*/

    private static Point2D shuffle() {
        Entity spawn = spawners.get(random.nextInt(spawners.size()));
        return new Point2D(spawn.getX(), spawn.getY());
    }


    public static void main(String args[]) {
        launch(args);
    }
}
