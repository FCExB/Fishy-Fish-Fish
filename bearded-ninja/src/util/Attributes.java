package util;

import java.util.Random;

public class Attributes {
	private final static double CHANCE_OF_MUTATION = 0.05;
	private final static double CHANCE_OF_NEW = 0.01;

	private final float[] stats = new float[Attribute.values().length];

	private final static Random generator = new Random();

	public Attributes() {

		for (Attribute a : Attribute.values()) {
			stats[a.getValue()] = Math.round((Math.random() * 10000) + 5000);
		}

		stats[Attribute.SPEED.getValue()] = (float) ((Math.random() * 10) + 5);
		stats[Attribute.HEALTH.getValue()] = (float) (Math.random() * 10) + 1;
		stats[Attribute.STRENGTH.getValue()] = (float) (Math.random() * 5) + 1;

		stats[Attribute.ANGER.getValue()] = generator.nextFloat() / 2;
	}

	public int getLength() {
		return stats.length;
	}

	public float getAttribute(Attribute a) {
		return stats[a.getValue()];
	}

	public static Attributes breed(Attributes a1, Attributes a2) {

		if (generator.nextDouble() < CHANCE_OF_MUTATION) {
			if (generator.nextBoolean()) {
				return mutate(a1);
			} else {
				return mutate(a2);
			}
		}

		if (generator.nextDouble() < CHANCE_OF_NEW) {
			return new Attributes();
		}

		int pivot = generator.nextInt(Attribute.values().length - 1);

		Attributes result = new Attributes();

		for (Attribute a : Attribute.values()) {
			if (a.getValue() < pivot) {
				result.stats[a.getValue()] = a1.stats[a.getValue()];
			} else {
				result.stats[a.getValue()] = a2.stats[a.getValue()];
			}
		}

		return result;
	}

	private static Attributes mutate(Attributes as) {
		Attributes result = new Attributes();

		for (Attribute a : Attribute.values()) {

			float change = (generator.nextFloat() * 2 - 1)
					* (as.stats[a.getValue()] / 5);
			result.stats[a.getValue()] = as.stats[a.getValue()] + change;
		}

		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (Attribute a : Attribute.values()) {

			builder.append(a.toString());
			builder.append(": ");
			builder.append(stats[a.getValue()]);
			builder.append("\n");
		}

		return builder.toString();
	}
}
