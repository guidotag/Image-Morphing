#include "utils.h"

float min(float a, float b) {
	return a < b ? a : b;
}

float max(float a, float b) {
	return a > b ? a : b;
}

CvVideoWriter *create_video_writer (const char *output_file, int width, int height, int fps) {
	CvVideoWriter *writer = NULL;
	int is_color = 1;
	writer = cvCreateVideoWriter(output_file, 
								//CV_FOURCC('M','J','P','G'),
								CV_FOURCC('P','I','M','1'), // MPEG-1 codec
								fps, 
								cvSize(width, height), 
								is_color);

	return writer;
}
