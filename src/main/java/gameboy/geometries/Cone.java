package gameboy.geometries;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import gameboy.materials.Material;
import gameboy.utilities.Shape;
import gameboy.utilities.math.Ray;
import gameboy.utilities.math.RayHit;
import gameboy.utilities.math.Vector3;

public class Cone extends Shape {

	Vector3 axis;
	double angle;
	double height;

	public Cone(Vector3 anchor, Vector3 axis, double angle, double height, Color color) {
		super(anchor);
		setMaterial(new ConeMaterial(color));
		this.axis = axis.normalize();
		this.angle = angle;
		this.height = height;
	}

	@Override
	public List<Vector3> getPoints() {
		return new LinkedList<>(List.of(getAnchor(), getAnchor().add(axis)));
	}

	@Override
	public Vector3 getIntersectionPoint(Ray ray) {
		Vector3 rayOrigin = ray.getOrigin();
		Vector3 rayDirection = ray.getDirection();
		Vector3 co = rayOrigin.subtract(getAnchor());

		double a = rayDirection.dot(axis) * rayDirection.dot(axis) - Math.cos(angle) * Math.cos(angle);
		double b = 2
				* (rayDirection.dot(axis) * co.dot(axis) - rayDirection.dot(co) * Math.cos(angle) * Math.cos(angle));
		double c = co.dot(axis) * co.dot(axis) - co.dot(co) * Math.cos(angle) * Math.cos(angle);

		double det = b * b - 4 * a * c;

		if (det < 0)
			return null;
		det = Math.sqrt(det);

		double tMin = (-b - det) / (2. * a);
		double tMax = (-b + det) / (2. * a);

		double t = tMin;
		if (t < 0 || tMax > 0 && tMax < t)
			t = tMax;
		if (t < 0)
			return null;

		Vector3 hitPoint = rayOrigin.add(rayDirection.scale(t)).subtract(getAnchor());
		double heightAtPoint = hitPoint.dot(axis);
		if (heightAtPoint < 0 || heightAtPoint > height)
			return null;

		return hitPoint;
	}

	public class ConeMaterial extends Material {

		public ConeMaterial() {
			super();
		}

		public ConeMaterial(Color color) {
			super(color);
		}

		@Override
		public Vector3 getNormal(RayHit hit) {
			Vector3 cp = hit.getHitPoint();
			Vector3 n = cp.scale(axis.dot(cp) / cp.dot(cp)).subtract(axis).normalize();
			return n;
		}
	}

	@Override
	public String toString() {
		return super.toString() + " | " + getMaterial().getColor(getAnchor()).toString();
	}
}
