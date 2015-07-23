#ifndef __ALGEBRA_H__
#define __ALGEBRA_H__

#include <stdlib.h>
#include <math.h>

typedef struct point {
	float x, y;
} point;

typedef struct segment {
	point from;
	point to;
} segment;

point subtract(point a, point b);

point add(point a, point b);

point perpendicular(point p);

float inner_product(point a, point b);

point scalar_product(float k, point p);

float norm(point p);

float projection_coefficient(point x, segment s);

float unit_perpendicular_coeffient(point x, segment s);

float distance(point x, segment s);

#endif