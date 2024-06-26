package gameboy.materials;

import gameboy.utilities.Color;
import gameboy.utilities.Material;
import gameboy.utilities.math.Vector3;

public class WaveMaterial extends Material {

	private long time;
	private Vector3 normal = new Vector3(1, 2, 0);

	public WaveMaterial() {
		super(Color.WHITE);
		time = System.currentTimeMillis();
	}

	@Override
	public Vector3 getNormal(Vector3 hitPoint) {
		if (System.currentTimeMillis() - time > 1) {
			normal = normal.rotate(0, Math.toRadians(1)); // TODO specular !!!!!!!!!!
			time = System.currentTimeMillis();
		}
		return normal;
	}
}
