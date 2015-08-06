/*
 * Author: Computer Organization II (University of Buenos Aires - 
 * Faculty of Exact and Natural Sciences) course teachers.
 */

#ifndef __TIMING_H__
#define __TIMING_H__

#define MEASURE(var)                                   		\
{                                                           \
    __asm__ __volatile__ (                                  \
        "xor %%rdx, %%rdx\n\t"                              \
        "xor %%rax, %%rax\n\t"                              \
        "lfence\n\t"                                        \
        "rdtsc\n\t"                                         \
        "sal $32, %%rdx\n\t"                                \
        "or %%rdx, %%rax\n\t"                               \
        "movq %%rax, %0\n\t"                                \
        : "=r" (var)                                        \
        : /* no input */                                    \
        : "%rax", "%rdx"                                    \
    );                                                      \
}


#define START_CLOCK(start)		                           \
{                                                           \
    /* warm up ... */                                       \
    MEASURE(start);                                    		\
    MEASURE(start);                                    		\
    MEASURE(start);                                    		\
}

#define STOP_CLOCK(end)		                               \
{                                                           \
    MEASURE(end);                                      		\
}

#endif
