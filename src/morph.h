#ifndef __MORPH_H__
#define __MORPH_H__

#include "algebra.h"
#include "pixel.h"

void morph(	IplImage *src_image, 
			IplImage *dst_image, 
			segment *src_segments, 
			segment *dst_segments, 
			int n_segments, 
			int n_frames, 
			CvVideoWriter *writer);

#endif