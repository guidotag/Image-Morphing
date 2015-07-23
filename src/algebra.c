#include "algebra.h"

point subtract(point a, point b) {
	point res;
	res.x = a.x - b.x;
	res.y = a.y - b.y;
	return res;
}

point add(point a, point b) {
	point res;
	res.x = a.x + b.x;
	res.y = a.y + b.y;
	return res;
}

point perpendicular(point p) {
	point res;
	res.x = p.y;
	res.y = -p.x;
	return res;
}

float inner_product(point a, point b) {
	return a.x * b.x + a.y * b.y;
}

point scalar_product(float k, point p) {
	p.x = k * p.x;
	p.y = k * p.y;
	return p;
}

float norm(point p) {
	return sqrtf(inner_product(p, p));
}

float projection_coefficient(point x, segment s) {
	point p = s.from;
	point q = s.to;
	point sub = subtract(q, p);
	float ns = norm(sub);
	return inner_product(subtract(x, p), sub) / (ns * ns);
}

float unit_perpendicular_coeffient(point x, segment s) {
	point p = s.from;
	point q = s.to;
	point sub = subtract(q, p);
	return inner_product(subtract(x, p), perpendicular(sub)) / norm(sub);
}

float distance(point x, segment s) {
	float k = projection_coefficient(x, s);
	point p = s.from;
	point q = s.to;

	if (k >= 0 && k <= 1) {
		// Distance to the subspace
		return abs(unit_perpendicular_coeffient(x, s));
	} else {
		if (k < 0) {
			return norm(subtract(x, p));
		} else {
			return norm(subtract(x, q));
		}
	}
}