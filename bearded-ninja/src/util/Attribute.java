package util;

public enum Attribute {
	FOOD(0), SEX(1), HEALTH(2), SPEED(3), STRENGTH(4), ANGER(5);

	private int index;

	private Attribute(int i) {
		index = i;
	}

	public int getValue() {
		return index;
	}
}
