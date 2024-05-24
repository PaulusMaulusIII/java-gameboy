package gameboy.utilities;

import java.util.LinkedList;
import java.util.List;

import gameboy.lights.Light;

public class Scene {
    private int currentCamera = 0;
    private List<Camera> cameras = new LinkedList<>();
    private List<Shape> children = new LinkedList<>();
    private List<Light> lights = new LinkedList<>();

    public Scene(Camera camera, List<Shape> children) {
        cameras.add(camera);
        this.children = children;
    }

    public Scene(Camera camera, List<Shape> children, List<Light> lights) {
        cameras.add(camera);
        this.children = children;
        this.lights = lights;
    }

    public Scene(List<Camera> cameras, List<Shape> children, List<Light> lights) {
        this.cameras = cameras;
        this.children = children;
        this.lights = lights;
    }

    public Scene(Camera camera) {
        cameras.add(camera);
    }

    public List<Shape> getChildren() {
        return children;
    }

    public Camera getCurrentCamera() {
        return cameras.get(currentCamera);
    }

    public void nextCamera() {
        currentCamera++;
    }

    public List<Light> getLights() {
        return lights;
    }

}