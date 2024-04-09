package gameboy.rendering;

import gameboy.math.Vector3;

public class Camera3D {
    private Vector3 position;
    private double pitch;
    private double yaw = 0;
    private double fov = 0;

    public Camera3D(Vector3 position, double fov) {
        this.position = position;
        this.fov = (double) fov;
    }

    public double getYaw() {
        return yaw;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    public double getPitch() {
        return pitch;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public void setFOV(double fov) {
        this.fov = fov;
    }

    public double getFOV() {
        return fov;
    }

    public void translate(Vector3 vec) {
        this.position = this.position.add(vec);
    }
}
