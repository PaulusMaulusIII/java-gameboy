package gameboy.geometries;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import gameboy.materials.Material;
import gameboy.utilities.Shape;
import gameboy.utilities.math.Ray;
import gameboy.utilities.math.RayHit;
import gameboy.utilities.math.Vector3;

public class Line extends Shape {

    private final Vector3[] points = new Vector3[2];
    private Vector3 anchor2;
    private Vector3 direction;

    public Line(Vector3 anchor, Vector3 anchor2) {
        super(anchor);
        setMaterial(new LineMaterial());
        points[0] = anchor;
        points[1] = anchor2;
        this.anchor2 = anchor2;
        this.direction = anchor2.subtract(anchor).normalize();
    }

    public Line(Vector3 anchor, Vector3 anchor2, Color color) {
        super(anchor);
        setMaterial(new LineMaterial(color));
        points[0] = anchor;
        points[1] = anchor2;
        this.anchor2 = anchor2;
        this.direction = anchor2.subtract(anchor).normalize();
    }

    @Override
    public List<Vector3> getPoints() {
        return new LinkedList<>(List.of(points));
    }

    public Vector3 getDirection() {
        return direction;
    }

    public Vector3 getAnchor2() {
        return anchor2;
    }

    @Override
    public Vector3 getIntersectionPoint(Ray ray) {
        return ray.intersection(toRay());
    }

    public Ray toRay() {
        return new Ray(getAnchor(), direction);
    }

    public class LineMaterial extends Material {

        public LineMaterial() {
            super();
        }

        public LineMaterial(Color color) {
            super(color);
        }

        @Override
        public Vector3 getNormal(RayHit rayHit) {
            return rayHit.getHitPoint().invert();
        }
    }
}
