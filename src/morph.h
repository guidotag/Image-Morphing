#ifndef __MORPH_H__
#define __MORPH_H__

#include "algebra.h"
#include "pixel.h"
#include "curve.h"

void morph_c(	IplImage *src_image, 
			IplImage *dst_image, 
			segment *src_segments, 
			segment *dst_segments, 
			int n_segments, 
			int n_frames, 
			CvVideoWriter *writer);

//void morph_asm();

//~ void compute_weighted_src_pixel_asm(point dst_point,
									//~ segment *src_segments,
									//~ curve *from_interpolations, 
									//~ curve *to_interpolations,
									//~ int n_segments,
									//~ float t,
									//~ int width,
									//~ int height);
									
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
