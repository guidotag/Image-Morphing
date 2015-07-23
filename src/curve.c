#include "curve.h"

point evaluate(curve c, float t) {
	point res;
	res.x = c.a1 * t + c.b1;
	res.y = c.a2 * t + c.b2;
	return res;
}

curve interpolate(point p, point q) {
	curve res;
	float x1 = p.x;
	float y1 = p.y;
	float x2 = q.x;
	float y2 = q.y;

	if (x1 != x2) {
		// Classic linear interpolation
		res.a1 = x2 - x1;
		res.b1 = x1;
		float a = (y2 - y1) / (x2 - x1);
		float b = y1 - x1 * a;
		res.a2 = a * res.a1;
		res.b2 = a * res.b1 + b;
	} else {
		// Interpolation taking the y-axis as variable
		res.a1 = 0;
		res.b1 = x1;
		res.a2 = y2 - y1;
		res.b2 = y1;
	}

	return res;
}