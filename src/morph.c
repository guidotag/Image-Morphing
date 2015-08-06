#include <stdio.h>
#include <cv.h>
#include <highgui.h>
#include "morph.h"
#include "utils.h"

static void compute_interpolations(	segment *src_segments, 
									segment *dst_segments, 
									curve *from_interpolations,
									curve *to_interpolations, 
									int n_segments) {

	int i;

	for (i = 0; i < n_segments; i++) {
		point p, q;

		p.x = src_segments[i].from.x;
		p.y = src_segments[i].from.y;
		q.x = dst_segments[i].from.x;
		q.y = dst_segments[i].from.y;

		from_interpolations[i] = interpolate(p, q);	

		p.x = src_segments[i].to.x;
		p.y = src_segments[i].to.y;
		q.x = dst_segments[i].to.x;
		q.y = dst_segments[i].to.y;
		
		to_interpolations[i] = interpolate(p, q);
	}
}

static point compute_src_point(float u, float v, segment src_segment) {
	point p = src_segment.from;
	point q = src_segment.to;
	point sub = subtract(q, p);
	return add(p, add(scalar_product(u, sub), scalar_product(v / norm(sub), perpendicular(sub))));
}

static float compute_weight(point x, segment s) {
	float dist = distance(x, s);
	float length = norm(subtract(s.to, s.from));
	return pow(pow(length, P) / (A + dist), B);
}

static void blend_pixels(	IplImage *dst_image, 
							IplImage *src_image_1, 
							IplImage *src_image_2,
							pixel dst_pixel,  
							pixel src_pixel_1, 
							pixel src_pixel_2, 
							float t) {
	int width_step = dst_image->widthStep;					
	int i;
	for (i = 0; i < N_CHANNELS; i++) {
		dst_image->imageData[dst_pixel.y * width_step + dst_pixel.x * N_CHANNELS + i] = 
			(1 - t) * (unsigned char) src_image_1->imageData[src_pixel_1.y * width_step + src_pixel_1.x * N_CHANNELS + i] +
			t * (unsigned char) src_image_2->imageData[src_pixel_2.y * width_step + src_pixel_2.x * N_CHANNELS + i];
	}
}

static pixel compute_weighted_src_pixel(point dst_point,
										segment *src_segments,
										curve *from_interpolations, 
										curve *to_interpolations,
										int n_segments,
										float t,
										int width,
										int height) {

	point disp_sum;
	disp_sum.x = disp_sum.y = 0;

	float weight_sum = 0;

	int i;
	for (i = 0; i < n_segments; i++) {
		segment dst_segment;
		dst_segment.from = evaluate(from_interpolations[i], t);
		dst_segment.to = evaluate(to_interpolations[i], t);
		
		float u = projection_coefficient(dst_point, dst_segment);
		float v = unit_perpendicular_coeffient(dst_point, dst_segment);
		float weight = compute_weight(dst_point, dst_segment);
		
		point src_point = compute_src_point(u, v, src_segments[i]);
		
		point disp = subtract(src_point, dst_point);
		
		disp_sum = add(disp_sum, scalar_product(weight, disp));
		
		weight_sum += weight;
	}

	point src_point;
	if (n_segments > 0) {
		src_point = add(dst_point, scalar_product(1 / weight_sum, disp_sum));
	} else {
		src_point.x = dst_point.x;
		src_point.y = dst_point.y;
	}

	src_point.x = max(0, src_point.x);
	src_point.x = min(src_point.x, width - 1);
	src_point.y = max(0, src_point.y);
	src_point.y = min(src_point.y, height - 1);

	pixel src_pixel;
	src_pixel.x = src_point.x;
	src_pixel.y = src_point.y;

	return src_pixel;
}

static void generate_sequence_c(IplImage *src_image, 
								IplImage *dst_image, 
								segment *src_segments, 
								segment *dst_segments,
								curve *from_interpolations,
								curve *to_interpolations,
								int n_segments, 
								int n_frames, 
								CvVideoWriter *writer) {
					
	int width = src_image->width;
	int height = src_image->height;

	IplImage *img = cvCreateImage(cvSize(width, height), IPL_DEPTH_8U, N_CHANNELS);
	int i;

	for (i = 0; i < n_frames; i++) {
		float t = i / (float)(n_frames - 1);
		
		int x, y;
		for (y = 0; y < height; y++) {
			for (x = 0; x < width; x++) {
				point dst_point;
				dst_point.x = x;
				dst_point.y = y;

				pixel src_src_pixel = compute_weighted_src_pixel(dst_point, src_segments, from_interpolations, to_interpolations, n_segments, t, width, height);
				pixel dst_src_pixel = compute_weighted_src_pixel(dst_point, dst_segments, from_interpolations, to_interpolations, n_segments, t, width, height);

				pixel dst_pixel;
				dst_pixel.x = x;
				dst_pixel.y = y;

				blend_pixels(img, src_image, dst_image, dst_pixel, src_src_pixel, dst_src_pixel, t);
			}
		}

		cvWriteFrame(writer, img);
	}
	
	cvReleaseImage(&img);
}

// Pre: images' width is >= 4
static void generate_sequence_asm(	IplImage *src_image, 
									IplImage *dst_image, 
									segment *src_segments, 
									segment *dst_segments, 
									curve *from_interpolations,
									curve *to_interpolations,
									int n_segments, 
									int n_frames, 
									CvVideoWriter *writer) {

	int width = src_image->width;
	int height = src_image->height;

	IplImage *img = cvCreateImage(cvSize(width, height), IPL_DEPTH_8U, N_CHANNELS);
	int i;
	
	for (i = 0; i < n_frames; i++) {
		float t = i / (float)(n_frames - 1);
		
		int x, y;
		for (y = 0; y < height; y++) {
			for (x = 0; x < width; x += 4) {
				if (x + 4 > width) {
					x = width - 4;
				}
				
				point dst_point;
				dst_point.x = x;
				dst_point.y = y;

				compute_weighted_src_pixel_asm(dst_point.x, dst_point.y, src_segments, from_interpolations, to_interpolations, n_segments, t, width, height);
				
				pixel src_src_pixel[4];
				unsigned int *ptr_x = &src_pixel_x;
				unsigned int *ptr_y = &src_pixel_y;
				
				int p;
				for (p = 0; p < 4; p++) {
					src_src_pixel[p].x = ptr_x[3 - p];
					src_src_pixel[p].y = ptr_y[3 - p];
				}
				
				compute_weighted_src_pixel_asm(dst_point.x, dst_point.y, dst_segments, from_interpolations, to_interpolations, n_segments, t, width, height);
				
				pixel dst_src_pixel[4];
				ptr_x = &src_pixel_x;
				ptr_y = &src_pixel_y;
				
				for (p = 0; p < 4; p++) {
					dst_src_pixel[p].x = ptr_x[3 - p];
					dst_src_pixel[p].y = ptr_y[3 - p];
				}
				
				pixel dst_pixel;
				for (p = 0; p < 4; p++) {
					dst_pixel.x = x + p;
					dst_pixel.y = y;
					blend_pixels(img, src_image, dst_image, dst_pixel, src_src_pixel[p], dst_src_pixel[p], t);
				}
			}
		}

		cvWriteFrame(writer, img);
	}
	
	cvReleaseImage(&img);
}

// Pre: nframes >= 2
void morph(	IplImage *src_image, 
			IplImage *dst_image, 
			segment *src_segments, 
			segment *dst_segments, 
			int n_segments, 
			int n_frames, 
			CvVideoWriter *writer,
			version v) {

	curve from_interpolations[n_segments];
	curve to_interpolations[n_segments];
	compute_interpolations(src_segments, dst_segments, from_interpolations, to_interpolations, n_segments);
	
	if (v == C) {
		generate_sequence_c(src_image, dst_image, src_segments, dst_segments, from_interpolations, to_interpolations, n_segments, n_frames, writer);
	} else {
		generate_sequence_asm(src_image, dst_image, src_segments, dst_segments, from_interpolations, to_interpolations, n_segments, n_frames, writer);
	}
}
