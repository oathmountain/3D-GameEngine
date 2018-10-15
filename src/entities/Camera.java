package entities;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjglx.input.Keyboard;
import org.lwjglx.input.Mouse;
import org.lwjglx.util.vector.Vector3f;

import java.nio.DoubleBuffer;

public class Camera {
    private float distanceFromPlayer = 50;
    private float angleAroundPlayer = 0;
    private boolean mouse1Down;
    private boolean mouse2Down;


    private Vector3f position = new Vector3f(25, 5, 50);
    private float pitch=20;
    private float yaw;
    private float roll;
    private long window;
    private Player player;
    private double lastMouseXPos;
    private double lastMouseZPos;

    public Camera(long window, Player player) {
        this.window = window;
        this.player = player;
    }

    public void move() {
        calculateZoom();
        calculatePitch();
        calculateAngleAroundPlayer();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);
        this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    private void calculateZoom() {
        float zoomLevel = 0;//does not work Mouse.getDWheel() * 0.1f;
        distanceFromPlayer -= zoomLevel;
    }

    private void calculateCameraPosition(float horizDistance, float verticDistance) {
        float theta = player.getRotY() + angleAroundPlayer;
        float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + verticDistance;
    }

    private float calculateHorizontalDistance() {
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance() {
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }

    private void calculatePitch() {
        if (GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_2) == 1) {
            //System.out.println("Mouse left pressed");
            if(!mouse2Down){
                DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
                DoubleBuffer z = BufferUtils.createDoubleBuffer(1);
                GLFW.glfwGetCursorPos(window,x,z);
                x.rewind();
                z.rewind();
                lastMouseZPos =z.get();
            }
            mouse2Down = true;
        }else{
            mouse2Down = false;
        }
        if(mouse2Down){
            DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer z = BufferUtils.createDoubleBuffer(1);
            GLFW.glfwGetCursorPos(window,x,z);
            x.rewind();
            z.rewind();
            double newZ = z.get();
            double DZ = lastMouseZPos - newZ;
            lastMouseZPos = newZ;
            float pitchChange = (float)DZ * 0.1f;
            if(pitch - pitchChange > 20 && pitch - pitchChange < 90) {
                pitch -= pitchChange;
            }
        }
    }

    private void calculateAngleAroundPlayer() {
        if (GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_1) == 1) {
            if(!mouse1Down){
                DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
                DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
                GLFW.glfwGetCursorPos(window,x,y);
                x.rewind();
                y.rewind();
                lastMouseXPos= x.get();
            }
            mouse1Down = true;
        } else {
            mouse1Down = false;
        }
        if (mouse1Down) {
            DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
            GLFW.glfwGetCursorPos(window,x,y);
            x.rewind();
            y.rewind();
            double newX = x.get();
            double DX = lastMouseXPos - newX;
            lastMouseXPos = newX;
            float angleChange = (float)DX * 0.3f;
            angleAroundPlayer -= angleChange;
        }
    }
}

