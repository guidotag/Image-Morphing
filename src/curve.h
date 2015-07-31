#ifndef __CURVE_H__
#define __CURVE_H__

#include "algebra.h"

// Represents a parametrized curve (x(t), y(t)), where
// x(t) = a1 t + b1
// y(t) = a2 t + b2

typedef struct __attribute__((__packed__)) curve {
	float a1;
	float b1;
	float a2;
	float b2;
} curve;

point evaluate(curve c, float t);

curve interpolate(point p, point q);

#endif
