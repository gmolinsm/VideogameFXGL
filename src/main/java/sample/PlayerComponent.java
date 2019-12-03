package sample;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

public class PlayerComponent extends Component {
    private int speedRate = 0;
    private int score = 0;
    private int lives = 3;
    private PhysicsComponent physics;
    private AnimatedTexture texture;
    private AnimationChannel animationWalk;
    private AnimationChannel animationIdle;
    private AnimationChannel animationJump;
    private AnimationChannel animationHurt;
    boolean playerFell = false;
    boolean playerHurt = false;

    public PlayerComponent() {
        animationWalk = new AnimationChannel("p1_walking.png", 4, 72, 95, Duration.seconds(1), 0, 3);
        animationIdle = new AnimationChannel("p1_stand.png", 1, 66, 92, Duration.seconds(1), 0, 0);
        animationJump = new AnimationChannel("p1_jump.png", 1, 66, 92, Duration.seconds(1), 0, 0);
        animationHurt = new AnimationChannel("p1_hurt.png", 1, 66, 92, Duration.seconds(1), 0, 0);
        texture = new AnimatedTexture(animationIdle);
    }

    @Override
    public void onAdded() {
        entity.setView(texture);
    }

    @Override
    public void onUpdate(double tpf) {
        entity.translateX(speedRate * tpf);
        if (playerHurt) {
            texture.loopAnimationChannel(animationHurt);
        } else {
            if (physics.isOnGround()) {
                if (speedRate != 0) {
                    if (texture.getAnimationChannel() == animationIdle || texture.getAnimationChannel() == animationJump) {
                        texture.loopAnimationChannel(animationWalk);
                    }

                    speedRate = (int) (speedRate * 0.1);

                    if(FXGLMath.abs(speedRate) < 1) {
                        speedRate = 0;
                        texture.loopAnimationChannel(animationIdle);
                    }
                } else {
                    if (texture.getAnimationChannel() == animationWalk || texture.getAnimationChannel() == animationJump) {
                        texture.loopAnimationChannel(animationIdle);
                    }
                }
            } else {
                texture.loopAnimationChannel(animationJump);
            }
        }
    }

    public void left() {
        speedRate = -150;
        getEntity().setScaleX(-1);
        physics.setVelocityX(speedRate);
    }

    public void right() {
        speedRate = 150;
        getEntity().setScaleX(1);
        physics.setVelocityX(speedRate);
    }

    public void jump() {
        if (physics.isOnGround()) {
            physics.setVelocityY(-500);
        }
    }

    public void damage() {
        physics.setVelocityY(-300);
        lives -= 1;
        score -= 100;
        playerHurt = true;

    }
}
