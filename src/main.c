#include <stdio.h>
#include <cv.h>
#include <highgui.h>
#include "algebra.h"
#include "morph.h"
#include "utils.h"

typedef struct CvVideoWriter CvVideoWriter;

int main (int argc, char *argv[]) {

	if (argc != 4) {
		fprintf(stderr, "Wrong number of arguments\n");
		exit(EXIT_FAILURE);
	}

	char *src_file = argv[1];
	char *dst_file = argv[2];
	char *out_file = argv[3];

	IplImage *src_image, *dst_image;

	if ((src_image = cvLoadImage(src_file, CV_LOAD_IMAGE_COLOR)) == 0) {
		fprintf(stderr, "Couldn't load source image\n");
		exit(EXIT_FAILURE);
	}

	if ((dst_image = cvLoadImage(dst_file, CV_LOAD_IMAGE_COLOR)) == 0) {
		fprintf(stderr, "Couldn't load destination image\n");
		exit(EXIT_FAILURE);
	}

	CvVideoWriter *writer = create_video_writer(out_file, src_image->width, src_image->height, 25);

	// select segments

	int n_segments = 24;

	segment src_segments[n_segments];
	src_segments[0].from.x = 233;
	src_segments[0].from.y = 249;
	src_segments[0].to.x = 269;
	src_segments[0].to.y = 342;

	segment dst_segments[n_segments];
	dst_segments[0].from.x = 283;
	dst_segments[0].from.y = 223;
	dst_segments[0].to.x = 307;
	dst_segments[0].to.y = 316;

	// apply morphing
	int n_frames = 100;

	morph_c(src_image, dst_image, src_segments, dst_segments, n_segments, n_frames, writer);

	//cvReleaseImage(&src_image);
	//cvReleaseImage(&dst_image);
	cvReleaseVideoWriter(&writer);

	return EXIT_SUCCESS;
}
