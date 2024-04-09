package gameboy.math.shapes;

import java.util.LinkedList;
import java.util.List;

import gameboy.math.Ray;
import gameboy.math.RayHit;
import gameboy.math.Vector3;
import gameboy.rendering.Light;
import gameboy.rendering.Material;
import gameboy.rendering.Shape3D;
import javafx.scene.paint.Color;

public class Cube extends Shape3D {

    private final Vector3[] points = new Vector3[8];

    double sideLength;

    public Cube(Vector3 anchor, double sideLength) {
        super(anchor);
        setMaterial(new CubeMaterial());
        this.sideLength = sideLength;

        double radius = sideLength / 2.0;

        for (int i = 0; i < 8; i++) {
            double xOffset = (i & 1) == 0 ? -radius : radius;
            double yOffset = (i & 2) == 0 ? -radius : radius;
            double zOffset = (i & 4) == 0 ? -radius : radius;

            points[i] = new Vector3(anchor.x + xOffset, anchor.y + yOffset, anchor.z + zOffset);
        }
    }

    @Override
    public List<Vector3> getPoints() {
        return new LinkedList<>(List.of(points));
    }

    @Override
    public Vector3 getIntersectionPoint(Ray ray) {
        double tMin = Double.NEGATIVE_INFINITY;
        double tMax = Double.POSITIVE_INFINITY;
        double halfSideLength = sideLength / 2.0;

        boolean hit = false;

        for (int i = 0; i < 3; i++) {
            if (Math.abs(ray.getDirection().toArray()[i]) < 1e-6) {
                if (ray.getOrigin().toArray()[i] < getAnchor().toArray()[i] - halfSideLength
                        || ray.getOrigin().toArray()[i] > getAnchor().toArray()[i] + halfSideLength)
                    return null;
            } else {
                double t1 = (getAnchor().toArray()[i] - halfSideLength - ray.getOrigin().toArray()[i])
                        / ray.getDirection().toArray()[i];
                double t2 = (getAnchor().toArray()[i] + halfSideLength - ray.getOrigin().toArray()[i])
                        / ray.getDirection().toArray()[i];

                if (t1 > t2) {
                    double temp = t1;
                    t1 = t2;
                    t2 = temp;
                }

                tMin = Math.max(tMin, t1);
                tMax = Math.min(tMax, t2);

                if (tMin > tMax)
                    return null;

            }

            if (tMax >= 0 && tMin <= tMax)
                hit = true;
        }

        if (hit)
            return ray.getOrigin().add(ray.getDirection().scale(tMin));
        return null;
    }

    private int determineCubeSide(Vector3 point) {
        double[] pointArray = point.toArray();
        double[] anchorArray = getAnchor().toArray();

        double maxDistance = 0;
        int maxAxis = -1;
        for (int i = 0; i < 3; i++) {
            double distance = Math.abs(pointArray[i] - anchorArray[i]);
            if (distance > maxDistance) {
                maxDistance = distance;
                maxAxis = i;
            }
        }

        if (maxAxis == 0) {
            return pointArray[0] > anchorArray[0] ? 0 : 1;
        } else if (maxAxis == 1) {
            return pointArray[1] > anchorArray[1] ? 2 : 3;
        } else {
            return pointArray[2] > anchorArray[2] ? 4 : 5;
        }
    }

    @Override
    public Vector3 getNormal(Vector3 point) {
        switch (determineCubeSide(point)) {
        case 0:
            return new Vector3(1, 0, 0);
        case 1:
            return new Vector3(-1, 0, 0);
        case 2:
            return new Vector3(0, 1, 0);
        case 3:
            return new Vector3(0, -1, 0);
        case 4:
            return new Vector3(0, 0, 1);
        case 5:
            return new Vector3(0, 0, -1);

        default:
            return null;
        }
    }

    public class CubeMaterial extends Material {

        public CubeMaterial() {
            super();
        }

        @Override
        public Color getColor(Vector3 point) {
            int side = determineCubeSide(point);

            Color[] colors = { Color.RED, Color.LIME, Color.BLUE, Color.YELLOW, Color.PINK, Color.PURPLE };
            // { x+, x-, y+, y-, z+, z- }

            if (side >= 0 && side < colors.length) {
                return colors[side];
            }

            return null;
        }

        @Override
        public Color shade(RayHit rayHit, List<Light> lights) {
            Vector3 hitPoint = rayHit.getHitPoint();
            Color baseColor = getColor(hitPoint);
            Vector3 normal = getNormal(hitPoint);
            Color finalColor = Color.BLACK;
            for (Light light : lights) {
                Color shadedColor = baseColor.deriveColor(0, 1,
                        normal.dot(light.getAnchor().subtract(hitPoint).normalize()), 1);
                finalColor = finalColor.interpolate(shadedColor, 1);
            }
            return finalColor;
        }
    }
}
