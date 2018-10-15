package engineTester;


import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import org.lwjglx.util.vector.Vector3f;
import renderEngine.*;
import models.RawModel;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class MainGameLoop {
    private static DisplayManager displayManager;
    public static void main(String[] args) {
        displayManager = new DisplayManager();
        displayManager.create();

        Loader loader = new Loader();

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassflowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture,rTexture,gTexture,bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendmap"));


        float[] vertices = {
                -0.5f,0.5f,0f,
                -0.5f,-0.5f,0f,
                0.5f,-0.5f,0f,
                0.5f,0.5f,0f,
        };

        float[] verticesCube = {
                -0.5f,0.5f,-0.5f,
                -0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,0.5f,-0.5f,

                -0.5f,0.5f,0.5f,
                -0.5f,-0.5f,0.5f,
                0.5f,-0.5f,0.5f,
                0.5f,0.5f,0.5f,

                0.5f,0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,0.5f,
                0.5f,0.5f,0.5f,

                -0.5f,0.5f,-0.5f,
                -0.5f,-0.5f,-0.5f,
                -0.5f,-0.5f,0.5f,
                -0.5f,0.5f,0.5f,

                -0.5f,0.5f,0.5f,
                -0.5f,0.5f,-0.5f,
                0.5f,0.5f,-0.5f,
                0.5f,0.5f,0.5f,

                -0.5f,-0.5f,0.5f,
                -0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,0.5f

        };
        int[] indices ={
            0,1,3,
            3,1,2
        };
        int[] indicesCube = {
                0,1,3,
                3,1,2,
                4,5,7,
                7,5,6,
                8,9,11,
                11,9,10,
                12,13,15,
                15,13,14,
                16,17,19,
                19,17,18,
                20,21,23,
                23,21,22

        };

        float[] textureCoords = {
                0,0,
                0,1,
                1,1,
                1,0
        };
        float[] textureCoordsCube = {

                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0


        };
        ModelData data = OBJFileLoader.loadOBJ("dragon");
        RawModel dragonModel = loader.loadToVAO(data.getVertices(),data.getTextureCoords(),data.getNormals(),data.getIndices());
        //RawModel model2 = loader.loadToVAO(verticesCube, textureCoordsCube,indicesCube);
        //RawModel model = OBJLoader.loadObjModel("dragon",loader);
        //ModelTexture texture = new ModelTexture(loader.loadTexture("white"));
        //TexturedModel texturedModel2 = new TexturedModel(model, texture);
        TexturedModel texturedModel = new TexturedModel(dragonModel,new ModelTexture(loader.loadTexture("blue")));
        ModelTexture texture = texturedModel.getTexture();


        data = OBJFileLoader.loadOBJ("fern");
        RawModel fernModel = loader.loadToVAO(data.getVertices(),data.getTextureCoords(),data.getNormals(),data.getIndices());
        //RawModel fernModel = OBJLoader.loadObjModel("fern",loader);
        TexturedModel texturedFern = new TexturedModel(fernModel,new ModelTexture(loader.loadTexture("fern")));
        ModelTexture fernTexture = texturedFern.getTexture();
        fernTexture.setTransparancy(true);
        fernTexture.setTransparancy(true);

        texture.setShineDamper(10);
        texture.setReflectivity(1);
        Entity fern = new Entity(texturedFern, new Vector3f(25,0,25),0,0,0,5);
        Entity entity = new Entity(texturedModel,new Vector3f(0,0,-25),0,0,0,1f);
        Light light = new Light(new Vector3f(0,20,-20),new Vector3f(1,1,1));
        Terrain terrain = new Terrain(0,0,loader, texturePack,blendMap);
        Terrain terrain2 = new Terrain(1,0,loader,texturePack,blendMap);


        RawModel bunnyModel = OBJLoader.loadObjModel("bunny",loader);
        TexturedModel bunnyTextured = new TexturedModel(bunnyModel,new ModelTexture(loader.loadTexture("blue")));
        Player player = new Player(bunnyTextured, new Vector3f(27,1,27),0,0,0,1);
        Camera camera = new Camera(displayManager.getWindow(),player);
        MasterRenderer renderer = new MasterRenderer();
        while(!displayManager.closed()){
            if (displayManager.isUpdating()) {
                displayManager.updateDisplay();
                player.move();
                entity.increaseRotation(0,0.2f,0);
                camera.move();
                //for lots of entities, processEntity.
                renderer.processTerrain(terrain);
                renderer.processTerrain(terrain2);
                renderer.processEntity(player);
                renderer.processEntity(fern);
                renderer.processEntity(entity);
                displayManager.update();
                renderer.render(light,camera);
                displayManager.swapBuffers();
            }
        }
        renderer.cleanUp();
        loader.cleanUp();
        displayManager.stop();
    }
}
