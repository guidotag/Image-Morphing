#ifndef __UTILS_H__
#define __UTILS_H__

#include <cv.h>
#include <highgui.h>
#include <stdio.h>

typedef struct CvVideoWriter CvVideoWriter;

float min(float a, float b);

float max(float a, float b);

CvVideoWriter *create_video_writer (const char *output_file, int width, int height, int fps);

#endif