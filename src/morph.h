#ifndef __MORPH_H__
#define __MORPH_H__

#include "algebra.h"
#include "pixel.h"
#include "curve.h"

#define N_CHANNELS 3
#define A 0.125
#define B 2
#define P 0.5

typedef enum version{C, ASM} version;

void morph(	IplImage *src_image, 
			IplImage *dst_image, 
			segment *src_segments, 
			segment *dst_segments, 
			int n_segments, 
			int n_frames, 
			CvVideoWriter *writer,
			version ver);
									
void compute_weighted_src_pixel_asm(float dst_point_x, float dst_point_y,
									segment *src_segments,
									curve *from_interpolations, 
									curve *to_interpolations,
									int n_segments,
									float t,
									int width,
									int height);
					
unsigned int src_pixel_x;
unsigned int src_pixel_y;

#endif
