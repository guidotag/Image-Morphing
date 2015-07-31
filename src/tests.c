#include <stdio.h>
#include <cv.h>
#include <highgui.h>
#include "algebra.h"
#include "morph.h"
#include "utils.h"

typedef struct CvVideoWriter CvVideoWriter;

void test_bill_house() {
	char *src_file = "../input/bill.bmp";
	char *dst_file = "../input/house.bmp";
	char *out_file = "../output/billhouse.avi";

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

	int n_segments = 24;

	segment src_segments[n_segments];
	src_segments[0].from.x = 233;
	src_segments[0].from.y = 249;
	src_segments[0].to.x = 269;
	src_segments[0].to.y = 342;

	src_segments[1].from.x = 275;
	src_segments[1].from.y = 359;
	src_segments[1].to.x = 324;
	src_segments[1].to.y = 415;

	src_segments[2].from.x = 344;
	src_segments[2].from.y = 428;
	src_segments[2].to.x = 394;
	src_segments[2].to.y = 444;

	src_segments[3].from.x = 397;
	src_segments[3].from.y = 445;
	src_segments[3].to.x = 439;
	src_segments[3].to.y = 424;

	src_segments[4].from.x = 449;
	src_segments[4].from.y = 406;
	src_segments[4].to.x = 513;
	src_segments[4].to.y = 327;

	src_segments[5].from.x = 520;
	src_segments[5].from.y = 311;
	src_segments[5].to.x = 542;
	src_segments[5].to.y = 221;

	src_segments[6].from.x = 227;
	src_segments[6].from.y = 212;
	src_segments[6].to.x = 242;
	src_segments[6].to.y = 107;

	src_segments[7].from.x = 251;
	src_segments[7].from.y = 84;
	src_segments[7].to.x = 351;
	src_segments[7].to.y = 30;

	src_segments[8].from.x = 373;
	src_segments[8].from.y = 28;
	src_segments[8].to.x = 487;
	src_segments[8].to.y = 70;

	src_segments[9].from.x = 497;
	src_segments[9].from.y = 92;
	src_segments[9].to.x = 543;
	src_segments[9].to.y = 192;

	src_segments[10].from.x = 306;
	src_segments[10].from.y = 432;
	src_segments[10].to.x = 277;
	src_segments[10].to.y = 477;

	src_segments[11].from.x = 263;
	src_segments[11].from.y = 482;
	src_segments[11].to.x = 60;
	src_segments[11].to.y = 575;

	src_segments[12].from.x = 518;
	src_segments[12].from.y = 396;
	src_segments[12].to.x = 561;
	src_segments[12].to.y = 440;

	src_segments[13].from.x = 580;
	src_segments[13].from.y = 441;
	src_segments[13].to.x = 793;
	src_segments[13].to.y = 493;

	src_segments[14].from.x = 283;
	src_segments[14].from.y = 201;
	src_segments[14].to.x = 344;
	src_segments[14].to.y = 193;

	src_segments[15].from.x = 401;
	src_segments[15].from.y = 188;
	src_segments[15].to.x = 458;
	src_segments[15].to.y = 186;

	src_segments[16].from.x = 299;
	src_segments[16].from.y = 220;
	src_segments[16].to.x = 237;
	src_segments[16].to.y = 221;

	src_segments[17].from.x = 415;
	src_segments[17].from.y = 215;
	src_segments[17].to.x = 415;
	src_segments[17].to.y = 210;

	src_segments[18].from.x = 378;
	src_segments[18].from.y = 223;
	src_segments[18].to.x = 381;
	src_segments[18].to.y = 293;

	src_segments[19].from.x = 332;
	src_segments[19].from.y = 285;
	src_segments[19].to.x = 298;
	src_segments[19].to.y = 326;

	src_segments[20].from.x = 434;
	src_segments[20].from.y = 273;
	src_segments[20].to.x = 475;
	src_segments[20].to.y = 307;

	src_segments[21].from.x = 322;
	src_segments[21].from.y = 332;
	src_segments[21].to.x = 453;
	src_segments[21].to.y = 326;

	src_segments[22].from.x = 321;
	src_segments[22].from.y = 341;
	src_segments[22].to.x = 388;
	src_segments[22].to.y = 364;

	src_segments[23].from.x = 389;
	src_segments[23].from.y = 363;
	src_segments[23].to.x = 449;
	src_segments[23].to.y = 335;


	segment dst_segments[n_segments];
	dst_segments[0].from.x = 283;
	dst_segments[0].from.y = 223;
	dst_segments[0].to.x = 307;
	dst_segments[0].to.y = 316;

	dst_segments[1].from.x = 317;
	dst_segments[1].from.y = 349;
	dst_segments[1].to.x = 356;
	dst_segments[1].to.y = 428;

	dst_segments[2].from.x = 366;
	dst_segments[2].from.y = 447;
	dst_segments[2].to.x = 412;
	dst_segments[2].to.y = 464;

	dst_segments[3].from.x = 420;
	dst_segments[3].from.y = 462;
	dst_segments[3].to.x = 465;
	dst_segments[3].to.y = 445;

	dst_segments[4].from.x = 482;
	dst_segments[4].from.y = 428;
	dst_segments[4].to.x = 530;
	dst_segments[4].to.y = 343;

	dst_segments[5].from.x = 536;
	dst_segments[5].from.y = 319;
	dst_segments[5].to.x = 573;
	dst_segments[5].to.y = 231;

	dst_segments[6].from.x = 286;
	dst_segments[6].from.y = 198;
	dst_segments[6].to.x = 302;
	dst_segments[6].to.y = 90;

	dst_segments[7].from.x = 314;
	dst_segments[7].from.y = 69;
	dst_segments[7].to.x = 414;
	dst_segments[7].to.y = 13;

	dst_segments[8].from.x = 434;
	dst_segments[8].from.y = 19;
	dst_segments[8].to.x = 527;
	dst_segments[8].to.y = 63;

	dst_segments[9].from.x = 542;
	dst_segments[9].from.y = 82;
	dst_segments[9].to.x = 571;
	dst_segments[9].to.y = 200;

	dst_segments[10].from.x = 320;
	dst_segments[10].from.y = 404;
	dst_segments[10].to.x = 281;
	dst_segments[10].to.y = 437;

	dst_segments[11].from.x = 265;
	dst_segments[11].from.y = 443;
	dst_segments[11].to.x = 92;
	dst_segments[11].to.y = 513;

	dst_segments[12].from.x = 524;
	dst_segments[12].from.y = 417;
	dst_segments[12].to.x = 562;
	dst_segments[12].to.y = 453;

	dst_segments[13].from.x = 572;
	dst_segments[13].from.y = 457;
	dst_segments[13].to.x = 725;
	dst_segments[13].to.y = 520;

	dst_segments[14].from.x = 323;
	dst_segments[14].from.y = 211;
	dst_segments[14].to.x = 395;
	dst_segments[14].to.y = 213;

	dst_segments[15].from.x = 445;
	dst_segments[15].from.y = 213;
	dst_segments[15].to.x = 506;
	dst_segments[15].to.y = 208;

	dst_segments[16].from.x = 336;
	dst_segments[16].from.y = 228;
	dst_segments[16].to.x = 383;
	dst_segments[16].to.y = 229;

	dst_segments[17].from.x = 456;
	dst_segments[17].from.y = 228;
	dst_segments[17].to.x = 500;
	dst_segments[17].to.y = 230;

	dst_segments[18].from.x = 417;
	dst_segments[18].from.y = 235;
	dst_segments[18].to.x = 417;
	dst_segments[18].to.y = 301;

	dst_segments[19].from.x = 378;
	dst_segments[19].from.y = 319;
	dst_segments[19].to.x = 349;
	dst_segments[19].to.y = 354;

	dst_segments[20].from.x = 460;
	dst_segments[20].from.y = 311;
	dst_segments[20].to.x = 489;
	dst_segments[20].to.y = 339;

	dst_segments[21].from.x = 374;
	dst_segments[21].from.y = 364;
	dst_segments[21].to.x = 461;
	dst_segments[21].to.y = 363;

	dst_segments[22].from.x = 372;
	dst_segments[22].from.y = 370;
	dst_segments[22].to.x = 421;
	dst_segments[22].to.y = 381;

	dst_segments[23].from.x = 422;
	dst_segments[23].from.y = 381;
	dst_segments[23].to.x = 460;
	dst_segments[23].to.y = 372;

	// apply morphing
	int n_frames = 150;

	morph_c(src_image, dst_image, src_segments, dst_segments, n_segments, n_frames, writer);

	//cvReleaseImage(&src_image);
	//cvReleaseImage(&dst_image);
	cvReleaseVideoWriter(&writer);
}

int main (int argc, char *argv[]) {
	test_bill_house();

	return EXIT_SUCCESS;
}
