package entities;

import models.TexturedModel;
import org.lwjgl.glfw.GLFW;
import org.lwjglx.util.vector.Vector3f;
import renderEngine.DisplayManager;


public class Player extends Entity {
    private static final float RUN_SPEED = 20;
    private static final float TURN_SPEED = 160;
    private static final float GRAVITY = -50;
    private static final float JUMP_POWER = 30;
    private static final float TERRAIN_HEIGHT = 0;
    private float currentSpeed = 0;
    private float currentTurnSpeed= 0;
    private float upwardSpeed = 0;
    private boolean isInAir = false;
    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public void move(){
        checkInputs(DisplayManager.getWindow());
        super.increaseRotation(0,currentTurnSpeed*DisplayManager.getFrameTimeSeconds(),0);
        float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
        float dx = (float)(distance * Math.sin(Math.toRadians(super.getRotY())));
        float dz = (float)(distance * Math.cos(Math.toRadians(super.getRotY())));
        super.increasePosition(dx,0,dz);
        upwardSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
        super.increasePosition(0,upwardSpeed*DisplayManager.getFrameTimeSeconds(),0);
        if(super.getPosition().y < TERRAIN_HEIGHT){
            isInAir = false;
            upwardSpeed = 0;
            super.getPosition().y = TERRAIN_HEIGHT;
        }
    }
    private void jump(){
        if(!isInAir) {
            this.upwardSpeed = JUMP_POWER;
            isInAir = true;
        }
    }

    private void checkInputs(Long window){
        if(GLFW.glfwGetKey(window,(GLFW.GLFW_KEY_W))==1){
            this.currentSpeed = RUN_SPEED;
        } else if(GLFW.glfwGetKey(window,GLFW.GLFW_KEY_S)==1){
            this.currentSpeed = -RUN_SPEED;
        }else{
            this.currentSpeed = 0;
        }
        if(GLFW.glfwGetKey(window,GLFW.GLFW_KEY_D)==1){
            this.currentTurnSpeed = -TURN_SPEED;
        }else if(GLFW.glfwGetKey(window,GLFW.GLFW_KEY_A)==1){
           this.currentTurnSpeed = TURN_SPEED;
        }else{
            this.currentTurnSpeed = 0;
        }
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_SPACE)==1){
            jump();
        }
    }
}
