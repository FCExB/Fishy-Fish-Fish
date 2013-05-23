package tomSprings;
import java.awt.Color;
import java.awt.Graphics;

public class Spring {
	float sprConst;
	float size;
	float force;

	Node a;
	Node b;

	Spring(float sprConst, Node a, Node b) {
		this.sprConst = sprConst;
		this.a = a;
		this.b = b;
		size = (a.position).distance(b.position);
	}
	
	void draw(Graphics g) {
		float r = Math.abs(force) / 100F;

		int red = (int) (255F * r);
		red = red <= 255 ? red : 255;

		g.setColor(Color.decode((new StringBuilder()).append("0x")
				.append(Integer.toHexString(red))
				.append(Integer.toHexString(255 - (red * 8) / 10)).append("00")
				.toString()));

		g.drawLine((int) a.position.x, (int) a.position.y, (int) b.position.x,
				(int) b.position.y);
	}

	void reCalculate() {
		force = -sprConst * (a.position.distance(b.position) - size);
		a.addForceFrom(force, b.position);
		b.addForceFrom(force, a.position);

	}

}