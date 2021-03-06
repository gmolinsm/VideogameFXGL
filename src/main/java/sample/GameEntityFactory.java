package sample;

import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameEntityFactory  implements EntityFactory {

    @Spawns("platform")
    public Entity newPlatform(SpawnData data) {
        HitBox hitBox = new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height")));
        PhysicsComponent physics = new PhysicsComponent();
        physics.addGroundSensor(hitBox);

        return Entities.builder()
                .type(GameTypes.PLATFORM)
                .from(data)
                .bbox(hitBox)
                .with(physics)
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("crate")
    public Entity newAmmoCrate(SpawnData data) {
        return Entities.builder()
                .type(GameTypes.CRATE)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .viewFromTexture("box.png")
                .with(new PhysicsComponent())
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.addGroundSensor(new HitBox(BoundingShape.box(66, 92)));

        return Entities.builder()
                .type(GameTypes.PLAYER)
                .from(data)
                .viewFromNodeWithBBox(new Rectangle(66, 92, Color.BLUE))
                .with(physics)
                .with(new PlayerComponent())
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("deathzone")
    public Entity newDeathZone(SpawnData data) {
        return Entities.builder()
                .type(GameTypes.DEATHZONE)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("spawn")
    public Entity newSpawner(SpawnData data) {

        return Entities.builder()
                .type(GameTypes.SPAWN)
                .from(data)
                .build();
    }
}
