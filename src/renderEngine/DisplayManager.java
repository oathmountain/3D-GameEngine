package renderEngine;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjglx.Sys;
import org.lwjglx.opengl.Display;

import java.nio.DoubleBuffer;

public class DisplayManager {
    public static final int WIDTH = 1280, HEIGHT = 720;
    private static double fpsCap = 1/120.0, time,processedTime=0;
    private static long window;
    private static long lastFrameTime;
    private static float delta;
    private Vector3f backgroundColor = new Vector3f(0.5f,0.5f,0.5f);
    private boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
    private boolean[] mouseButtons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];

    public void create(){
        if(!GLFW.glfwInit()){
            System.err.println("Error: Could not initialize GLFW");
            System.exit(-1);
        }
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
        window = GLFW.glfwCreateWindow(WIDTH,HEIGHT,"GameWindow",0,0);
        if(window == 0){
            System.err.println("Error: Window could not be created");
            System.exit(-1);
        }

        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        GLFW.glfwSetWindowPos(window,(videoMode.width()-WIDTH)/2, (videoMode.height()-HEIGHT)/2);
        GLFW.glfwShowWindow(window);

        time = getTime();
    }

    public boolean closed(){
        return GLFW.glfwWindowShouldClose(window);
    }

    public void update(){
        for (int i = 0; i < GLFW.GLFW_KEY_LAST;i++){
            keys[i] = isKeyDown(i);
        }
        for (int i = 0; i < GLFW.GLFW_MOUSE_BUTTON_LAST;i++){
            mouseButtons[i] = isMouseDown(i);
        }
        GL11.glClearColor(backgroundColor.x,backgroundColor.y,backgroundColor.z,1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
        GLFW.glfwPollEvents();
    }

    public void stop(){
        GLFW.glfwTerminate();
    }
    public void swapBuffers(){
        GLFW.glfwSwapBuffers(window);
    }

    public Double getTime(){
        return (double)System.nanoTime() / (double)1000000000;
    }

    public boolean isKeyDown(int keyCode){
        return GLFW.glfwGetKey(window, keyCode)==1;

    }
    public boolean isMouseDown(int mouseButton){
        return GLFW.glfwGetMouseButton(window,mouseButton) == 1;
    }
    public boolean isKeyPressed(int keyCode){
        return isKeyDown(keyCode) && !keys[keyCode];
    }
    public boolean isKeyReleased(int keyCode){
        return !isKeyDown(keyCode) && keys[keyCode];
    }
    public boolean isMousePressed(int mouseButton){
        return isMouseDown(mouseButton) && !mouseButtons[mouseButton];
    }
    public boolean isMouseReleased(int mouseButton){
        return !isMouseDown(mouseButton) && mouseButtons[mouseButton];
    }
    public double getMouseX(){
        DoubleBuffer buffer = BufferUtils.createDoubleBuffer(1);
        GLFW.glfwGetCursorPos(window,buffer,null);
        return buffer.get(0);
    }
    public double getMouseY(){
        DoubleBuffer buffer = BufferUtils.createDoubleBuffer(1);
        GLFW.glfwGetCursorPos(window,null,buffer);
        return buffer.get(0);
    }

    public boolean isUpdating(){
        double nextTime = getTime();
        double passedTime = nextTime - time;
        processedTime += passedTime;
        time = nextTime;
        while (processedTime > fpsCap) {
            processedTime -= fpsCap;
            return true;
        }
        return false;
    }
    public static void updateDisplay(){
        //Display.sync((int)(1/fpsCap));
        //Display.update();
        long currentFrameTime = getCurrentTime();
        delta = (currentFrameTime - lastFrameTime)/1000f;
        lastFrameTime = currentFrameTime;
    }

    public static float getFrameTimeSeconds(){
        return delta;
    }
    public static long getCurrentTime(){
        return (System.nanoTime()/ (long)1000000);// *1000);/// 1000000000;
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public double getFps() {
        return 1/fpsCap;
    }

    public static long getWindow() {
        return window;
    }
    public void setBackgroundColor(float r, float g, float b){
        backgroundColor = new Vector3f(r,g,b);
    }
}
