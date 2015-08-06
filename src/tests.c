#include <stdio.h>
#include <cv.h>
#include <highgui.h>
#include "algebra.h"
#include "morph.h"
#include "utils.h"
#include "timing.h"

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
	
	if(writer == NULL) {
        fprintf(stderr, "Cannot create video writer\n");
        exit(EXIT_FAILURE);
    }

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
	src_segments[16].to.x = 337;//227
	src_segments[16].to.y = 221;

	src_segments[17].from.x = 415;
	src_segments[17].from.y = 215;
	src_segments[17].to.x = 465;//415
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
	int n_frames = 100;

	//~ unsigned long long start, end;
	//~ START_CLOCK(start);
	morph(src_image, dst_image, src_segments, dst_segments, n_segments, n_frames, writer, ASM);
	//~ STOP_CLOCK(end);
	//~ printf("TSC: %llu\n", end - start);

	cvReleaseImage(&src_image);
	cvReleaseImage(&dst_image);
	cvReleaseVideoWriter(&writer);
}

void test_a_b() {
	char *src_file = "../input/A.bmp";
	char *dst_file = "../input/B.bmp";
	char *out_file = "../output/AB.avi";

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
	
	if(writer == NULL) {
        fprintf(stderr, "Cannot create video writer\n");
        exit(EXIT_FAILURE);
    }

	int n_segments = 8;

	segment src_segments[n_segments];
	src_segments[0].from.x = 336;
	src_segments[0].from.y = 105;
	src_segments[0].to.x = 166;
	src_segments[0].to.y = 490;

	src_segments[1].from.x = 352;
	src_segments[1].from.y = 105;
	src_segments[1].to.x = 507;
	src_segments[1].to.y = 490;

	src_segments[2].from.x = 327;
	src_segments[2].from.y = 172;
	src_segments[2].to.x = 249;
	src_segments[2].to.y = 355;

	src_segments[3].from.x = 327;
	src_segments[3].from.y = 172;
	src_segments[3].to.x = 408;
	src_segments[3].to.y = 357;

	src_segments[4].from.x = 249;
	src_segments[4].from.y = 355;
	src_segments[4].to.x = 408;
	src_segments[4].to.y = 357;

	src_segments[5].from.x = 249;
	src_segments[5].from.y = 355;
	src_segments[5].to.x = 194;
	src_segments[5].to.y = 485;

	src_segments[6].from.x = 408;
	src_segments[6].from.y = 357;
	src_segments[6].to.x = 477;
	src_segments[6].to.y = 485;

	src_segments[7].from.x = 194;
	src_segments[7].from.y = 485;
	src_segments[7].to.x = 477;
	src_segments[7].to.y = 485;

	segment dst_segments[n_segments];
	dst_segments[0].from.x = 172;
	dst_segments[0].from.y = 66;
	dst_segments[0].to.x = 164;
	dst_segments[0].to.y = 593;

	dst_segments[1].from.x = 502;
	dst_segments[1].from.y = 89;
	dst_segments[1].to.x = 502;
	dst_segments[1].to.y = 564;

	dst_segments[2].from.x = 216;
	dst_segments[2].from.y = 96;
	dst_segments[2].to.x = 216;
	dst_segments[2].to.y = 292;

	dst_segments[3].from.x = 479;
	dst_segments[3].from.y = 104;
	dst_segments[3].to.x = 479;
	dst_segments[3].to.y = 272;

	dst_segments[4].from.x = 216;
	dst_segments[4].from.y = 292;
	dst_segments[4].to.x = 479;
	dst_segments[4].to.y = 272;

	dst_segments[5].from.x = 216;
	dst_segments[5].from.y = 342;
	dst_segments[5].to.x = 216;
	dst_segments[5].to.y = 552;

	dst_segments[6].from.x = 479;
	dst_segments[6].from.y = 353;
	dst_segments[6].to.x = 479;
	dst_segments[6].to.y = 536;

	dst_segments[7].from.x = 164;
	dst_segments[7].from.y = 593;
	dst_segments[7].to.x = 409;
	dst_segments[7].to.y = 605;

	// apply morphing
	int n_frames = 25;

	unsigned long long start, end;
	START_CLOCK(start);
	morph(src_image, dst_image, src_segments, dst_segments, n_segments, n_frames, writer, C);
	STOP_CLOCK(end);
	printf("ASM: %llu\n", end - start);

	cvReleaseImage(&src_image);
	cvReleaseImage(&dst_image);
	cvReleaseVideoWriter(&writer);
}

void test_scarlett_keanu() {
	char *src_file = "../input/scarlett.bmp";
	char *dst_file = "../input/keanu.bmp";
	char *out_file = "../output/scarlettkeanu.avi";

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
	
	if(writer == NULL) {
        fprintf(stderr, "Cannot create video writer\n");
        exit(EXIT_FAILURE);
    }
    
    int n_segments = 44;

	segment src_segments[n_segments];
	src_segments[0].from.x = 315;
	src_segments[0].from.y = 304;
	src_segments[0].to.x = 323;
	src_segments[0].to.y = 346;

	src_segments[1].from.x = 326;
	src_segments[1].from.y = 357;
	src_segments[1].to.x = 343;
	src_segments[1].to.y = 404;

	src_segments[2].from.x = 353;
	src_segments[2].from.y = 409;
	src_segments[2].to.x = 363;
	src_segments[2].to.y = 408;

	src_segments[3].from.x = 365;
	src_segments[3].from.y = 415;
	src_segments[3].to.x = 385;
	src_segments[3].to.y = 477;

	src_segments[4].from.x = 385;
	src_segments[4].from.y = 480;
	src_segments[4].to.x = 429;
	src_segments[4].to.y = 537;

	src_segments[5].from.x = 435;
	src_segments[5].from.y = 544;
	src_segments[5].to.x = 472;
	src_segments[5].to.y = 570;

	src_segments[6].from.x = 475;
	src_segments[6].from.y = 574;
	src_segments[6].to.x = 517;
	src_segments[6].to.y = 587;

	src_segments[7].from.x = 522;
	src_segments[7].from.y = 587;
	src_segments[7].to.x = 564;
	src_segments[7].to.y = 570;

	src_segments[8].from.x = 570;
	src_segments[8].from.y = 565;
	src_segments[8].to.x = 601;
	src_segments[8].to.y = 536;

	src_segments[9].from.x = 604;
	src_segments[9].from.y = 535;
	src_segments[9].to.x = 640;
	src_segments[9].to.y = 481;

	src_segments[10].from.x = 644;
	src_segments[10].from.y = 469;
	src_segments[10].to.x = 665;
	src_segments[10].to.y = 407;

	src_segments[11].from.x = 667;
	src_segments[11].from.y = 406;
	src_segments[11].to.x = 680;
	src_segments[11].to.y = 402;

	src_segments[12].from.x = 684;
	src_segments[12].from.y = 400;
	src_segments[12].to.x = 697;
	src_segments[12].to.y = 345;

	src_segments[13].from.x = 696;
	src_segments[13].from.y = 340;
	src_segments[13].to.x = 703;
	src_segments[13].to.y = 282;

	src_segments[14].from.x = 703;
	src_segments[14].from.y = 281;
	src_segments[14].to.x = 681;
	src_segments[14].to.y = 286;

	src_segments[15].from.x = 680;
	src_segments[15].from.y = 289;
	src_segments[15].to.x = 673;
	src_segments[15].to.y = 222;

	src_segments[16].from.x = 671;
	src_segments[16].from.y = 218;
	src_segments[16].to.x = 655;
	src_segments[16].to.y = 162;

	src_segments[17].from.x = 644;
	src_segments[17].from.y = 159;
	src_segments[17].to.x = 578;
	src_segments[17].to.y = 146;

	src_segments[18].from.x = 569;
	src_segments[18].from.y = 141;
	src_segments[18].to.x = 472;
	src_segments[18].to.y = 141;

	src_segments[19].from.x = 456;
	src_segments[19].from.y = 141;
	src_segments[19].to.x = 375;
	src_segments[19].to.y = 162;

	src_segments[20].from.x = 370;
	src_segments[20].from.y = 170;
	src_segments[20].to.x = 355;
	src_segments[20].to.y = 221;

	src_segments[21].from.x = 354;
	src_segments[21].from.y = 230;
	src_segments[21].to.x = 349;
	src_segments[21].to.y = 298;

	src_segments[22].from.x = 338;
	src_segments[22].from.y = 289;
	src_segments[22].to.x = 320;
	src_segments[22].to.y = 286;

	src_segments[23].from.x = 406;
	src_segments[23].from.y = 527;
	src_segments[23].to.x = 395;
	src_segments[23].to.y = 498;
	
	src_segments[24].from.x = 379;
	src_segments[24].from.y = 613;
	src_segments[24].to.x = 265;
	src_segments[24].to.y = 658;

	src_segments[25].from.x = 253;
	src_segments[25].from.y = 664;
	src_segments[25].to.x = 151;
	src_segments[25].to.y = 707;

	src_segments[26].from.x = 143;
	src_segments[26].from.y = 716;
	src_segments[26].to.x = 109;
	src_segments[26].to.y = 763;

	src_segments[27].from.x = 634;
	src_segments[27].from.y = 500;
	src_segments[27].to.x = 631;
	src_segments[27].to.y = 586;

	src_segments[28].from.x = 638;
	src_segments[28].from.y = 589;
	src_segments[28].to.x = 775;
	src_segments[28].to.y = 622;

	src_segments[29].from.x = 786;
	src_segments[29].from.y = 625;
	src_segments[29].to.x = 909;
	src_segments[29].to.y = 659;

	src_segments[30].from.x = 918;
	src_segments[30].from.y = 671;
	src_segments[30].to.x = 970;
	src_segments[30].to.y = 762;

	src_segments[31].from.x = 386;
	src_segments[31].from.y = 280;
	src_segments[31].to.x = 424;
	src_segments[31].to.y = 279;

	src_segments[32].from.x = 431;
	src_segments[32].from.y = 281;
	src_segments[32].to.x = 474;
	src_segments[32].to.y = 297;

	src_segments[33].from.x = 554;
	src_segments[33].from.y = 292;
	src_segments[33].to.x = 612;
	src_segments[33].to.y = 272;

	src_segments[34].from.x = 622;
	src_segments[34].from.y = 274;
	src_segments[34].to.x = 651;
	src_segments[34].to.y = 276;

	src_segments[35].from.x = 404;
	src_segments[35].from.y = 316;
	src_segments[35].to.x = 472;
	src_segments[35].to.y = 326;

	src_segments[36].from.x = 564;
	src_segments[36].from.y = 325;
	src_segments[36].to.x = 637;
	src_segments[36].to.y = 319;

	src_segments[37].from.x = 520;
	src_segments[37].from.y = 323;
	src_segments[37].to.x = 520;
	src_segments[37].to.y = 413;

	src_segments[38].from.x = 514;
	src_segments[38].from.y = 435;
	src_segments[38].to.x = 489;
	src_segments[38].to.y = 425;

	src_segments[39].from.x = 526;
	src_segments[39].from.y = 435;
	src_segments[39].to.x = 556;
	src_segments[39].to.y = 424;

	src_segments[40].from.x = 463;
	src_segments[40].from.y = 490;
	src_segments[40].to.x = 523;
	src_segments[40].to.y = 525;

	src_segments[41].from.x = 525;
	src_segments[41].from.y = 527;
	src_segments[41].to.x = 577;
	src_segments[41].to.y = 488;

	src_segments[42].from.x = 575;
	src_segments[42].from.y = 483;
	src_segments[42].to.x = 520;
	src_segments[42].to.y = 466;

	src_segments[43].from.x = 515;
	src_segments[43].from.y = 465;
	src_segments[43].to.x = 464;
	src_segments[43].to.y = 483;


	segment dst_segments[n_segments];
	dst_segments[0].from.x = 311;
	dst_segments[0].from.y = 281;
	dst_segments[0].to.x = 313;
	dst_segments[0].to.y = 322;

	dst_segments[1].from.x = 316;
	dst_segments[1].from.y = 333;
	dst_segments[1].to.x = 329;
	dst_segments[1].to.y = 376;

	dst_segments[2].from.x = 333;
	dst_segments[2].from.y = 375;
	dst_segments[2].to.x = 345;
	dst_segments[2].to.y = 375;

	dst_segments[3].from.x = 347;
	dst_segments[3].from.y = 383;
	dst_segments[3].to.x = 351;
	dst_segments[3].to.y = 440;

	dst_segments[4].from.x = 353;
	dst_segments[4].from.y = 449;
	dst_segments[4].to.x = 399;
	dst_segments[4].to.y = 522;

	dst_segments[5].from.x = 404;
	dst_segments[5].from.y = 529;
	dst_segments[5].to.x = 439;
	dst_segments[5].to.y = 570;

	dst_segments[6].from.x = 444;
	dst_segments[6].from.y = 572;
	dst_segments[6].to.x = 483;
	dst_segments[6].to.y = 584;

	dst_segments[7].from.x = 489;
	dst_segments[7].from.y = 582;
	dst_segments[7].to.x = 525;
	dst_segments[7].to.y = 571;

	dst_segments[8].from.x = 533;
	dst_segments[8].from.y = 564;
	dst_segments[8].to.x = 557;
	dst_segments[8].to.y = 532;

	dst_segments[9].from.x = 561;
	dst_segments[9].from.y = 526;
	dst_segments[9].to.x = 611;
	dst_segments[9].to.y = 458;

	dst_segments[10].from.x = 612;
	dst_segments[10].from.y = 447;
	dst_segments[10].to.x = 623;
	dst_segments[10].to.y = 409;

	dst_segments[11].from.x = 626;
	dst_segments[11].from.y = 408;
	dst_segments[11].to.x = 640;
	dst_segments[11].to.y = 410;

	dst_segments[12].from.x = 644;
	dst_segments[12].from.y = 403;
	dst_segments[12].to.x = 651;
	dst_segments[12].to.y = 361;

	dst_segments[13].from.x = 651;
	dst_segments[13].from.y = 361;
	dst_segments[13].to.x = 658;
	dst_segments[13].to.y = 305;

	dst_segments[14].from.x = 653;
	dst_segments[14].from.y = 303;
	dst_segments[14].to.x = 636;
	dst_segments[14].to.y = 310;

	dst_segments[15].from.x = 638;
	dst_segments[15].from.y = 306;
	dst_segments[15].to.x = 637;
	dst_segments[15].to.y = 240;

	dst_segments[16].from.x = 635;
	dst_segments[16].from.y = 235;
	dst_segments[16].to.x = 629;
	dst_segments[16].to.y = 178;

	dst_segments[17].from.x = 616;
	dst_segments[17].from.y = 170;
	dst_segments[17].to.x = 571;
	dst_segments[17].to.y = 142;

	dst_segments[18].from.x = 564;
	dst_segments[18].from.y = 144;
	dst_segments[18].to.x = 488;
	dst_segments[18].to.y = 139;

	dst_segments[19].from.x = 475;
	dst_segments[19].from.y = 138;
	dst_segments[19].to.x = 412;
	dst_segments[19].to.y = 156;

	dst_segments[20].from.x = 402;
	dst_segments[20].from.y = 166;
	dst_segments[20].to.x = 380;
	dst_segments[20].to.y = 207;

	dst_segments[21].from.x = 373;
	dst_segments[21].from.y = 222;
	dst_segments[21].to.x = 356;
	dst_segments[21].to.y = 285;

	dst_segments[22].from.x = 344;
	dst_segments[22].from.y = 283;
	dst_segments[22].to.x = 320;
	dst_segments[22].to.y = 275;

	dst_segments[23].from.x = 356;
	dst_segments[23].from.y = 471;
	dst_segments[23].to.x = 358;
	dst_segments[23].to.y = 547;
	
	dst_segments[24].from.x = 340;
	dst_segments[24].from.y = 553;
	dst_segments[24].to.x = 222;
	dst_segments[24].to.y = 589;

	dst_segments[25].from.x = 206;
	dst_segments[25].from.y = 593;
	dst_segments[25].to.x = 90;
	dst_segments[25].to.y = 650;

	dst_segments[26].from.x = 81;
	dst_segments[26].from.y = 658;
	dst_segments[26].to.x = 39;
	dst_segments[26].to.y = 710;

	dst_segments[27].from.x = 588;
	dst_segments[27].from.y = 503;
	dst_segments[27].to.x = 586;
	dst_segments[27].to.y = 556;

	dst_segments[28].from.x = 596;
	dst_segments[28].from.y = 557;
	dst_segments[28].to.x = 727;
	dst_segments[28].to.y = 576;

	dst_segments[29].from.x = 739;
	dst_segments[29].from.y = 577;
	dst_segments[29].to.x = 848;
	dst_segments[29].to.y = 584;

	dst_segments[30].from.x = 857;
	dst_segments[30].from.y = 592;
	dst_segments[30].to.x = 919;
	dst_segments[30].to.y = 635;

	dst_segments[31].from.x = 392;
	dst_segments[31].from.y = 286;
	dst_segments[31].to.x = 429;
	dst_segments[31].to.y = 284;

	dst_segments[32].from.x = 441;
	dst_segments[32].from.y = 284;
	dst_segments[32].to.x = 481;
	dst_segments[32].to.y = 296;

	dst_segments[33].from.x = 554;
	dst_segments[33].from.y = 302;
	dst_segments[33].to.x = 601;
	dst_segments[33].to.y = 297;

	dst_segments[34].from.x = 604;
	dst_segments[34].from.y = 298;
	dst_segments[34].to.x = 625;
	dst_segments[34].to.y = 308;

	dst_segments[35].from.x = 415;
	dst_segments[35].from.y = 311;
	dst_segments[35].to.x = 473;
	dst_segments[35].to.y = 323;

	dst_segments[36].from.x = 544;
	dst_segments[36].from.y = 332;
	dst_segments[36].to.x = 600;
	dst_segments[36].to.y = 335;

	dst_segments[37].from.x = 518;
	dst_segments[37].from.y = 318;
	dst_segments[37].to.x = 511;
	dst_segments[37].to.y = 411;

	dst_segments[38].from.x = 500;
	dst_segments[38].from.y = 432;
	dst_segments[38].to.x = 462;
	dst_segments[38].to.y = 414;

	dst_segments[39].from.x = 515;
	dst_segments[39].from.y = 434;
	dst_segments[39].to.x = 548;
	dst_segments[39].to.y = 417;

	dst_segments[40].from.x = 431;
	dst_segments[40].from.y = 450;
	dst_segments[40].to.x = 489;
	dst_segments[40].to.y = 508;

	dst_segments[41].from.x = 497;
	dst_segments[41].from.y = 511;
	dst_segments[41].to.x = 560;
	dst_segments[41].to.y = 464;

	dst_segments[42].from.x = 559;
	dst_segments[42].from.y = 456;
	dst_segments[42].to.x = 504;
	dst_segments[42].to.y = 460;

	dst_segments[43].from.x = 496;
	dst_segments[43].from.y = 458;
	dst_segments[43].to.x = 433;
	dst_segments[43].to.y = 446;
    
    // apply morphing
	int n_frames = 100;
	
	n_segments = 44;
	unsigned long long start, end;
	START_CLOCK(start);
	morph(dst_image, src_image, dst_segments, src_segments, n_segments, n_frames, writer, ASM);
	STOP_CLOCK(end);
	printf("TSC: %llu\n", end - start);
	
	cvReleaseImage(&src_image);
	cvReleaseImage(&dst_image);
	cvReleaseVideoWriter(&writer);
}

int main (int argc, char *argv[]) {
	//~ test_bill_house();
	//~ test_a_b();
	test_scarlett_keanu();

	return EXIT_SUCCESS;
}

