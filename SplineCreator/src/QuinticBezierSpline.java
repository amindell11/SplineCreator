public class QuinticBezierSpline {
	static Function B0 = (float t) -> {
		return Function.pow(1 - t, 5);
	};
	static Function B1 = (float t) -> {
		return 5 * Function.pow(1 - t, 4) * t;
	};
	static Function B2 = (float t) -> {
		return 10 * Function.pow(1 - t, 3) * Function.pow(t, 2);
	};
	static Function B3 = (float t) -> {
		return 10 * Function.pow(1 - t, 2) * Function.pow(t, 3);
	};
	static Function B4 = (float t) -> {
		return 5 * (1 - t) * Function.pow(t, 4);
	};
	static Function B5 = (float t) -> {
		return Function.pow(t, 5);
	};
	static Function[] B = new Function[] { B0, B1, B2, B3, B4, B5 };
	Function splineX;
	Function splineY;

	public QuinticBezierSpline(Point[] p) {
		splineX = (float t) -> {
			float val = 0;
			for (int x = 0; x < 6; x++) {
				val = val + B[x].f(t) * p[x].x;
			}
			return val;
		};
		splineY=(float t) -> {
			float val = 0;
			for (int x = 0; x < 6; x++) {
				val = val + B[x].f(t) * p[x].y;
			}
			return val;
		};
	}

	public Point get(float t) {
		return new Point(splineX.f(t),splineY.f(t));
	}

}
