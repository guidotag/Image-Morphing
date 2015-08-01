global compute_weighted_src_pixel_asm
global src_pixel_x
global src_pixel_y

%define CURVE_SIZE 		16
%define A1_OFFSET		0
%define B1_OFFSET		4
%define A2_OFFSET		8
%define B2_OFFSET		12

%define POINT_SIZE		8
%define X_OFFSET		0
%define Y_OFFSET		4

%define SEGMENT_SIZE 	16
%define FROM_OFFSET		0
%define TO_OFFSET		8

section .data
align 16

time: dq 0, 0
weight_sum: dq 0, 0
coefficient_v: dq 0, 0
condition_1: dq 0, 0
condition_2: dq 0, 0
condition_3: dq 0, 0
src_pixel_x: dq 0, 0
src_pixel_y: dq 0, 0

mask_copy_lowest_dword: db 0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3
; Copies the lowest dword in the other dwords

mask_turn_off_highest_bit_dwords: db 0xff, 0xff, 0xff, 0x7f, 0xff, 0xff, 0xff, 0x7f, 0xff, 0xff, 0xff, 0x7f, 0xff, 0xff, 0xff, 0x7f
; Turns off the highest bit of every dword

mask_float_one: db 0x00, 0x00, 0x80, 0x3f
; 32-bit floating point representation of 1 (0 01111111 00...0)

mask_float_eps: db 0x00, 0x00, 0x00, 0x3e
; 32-bit floating point representation of 0.125 (0 01111100 00...0)

mask_float_zero_one_two_three: db 0x00, 0x00, 0x40, 0x40, 0x00, 0x00, 0x00, 0x40, 0x00, 0x00, 0x80, 0x3f, 0x00, 0x00, 0x00, 0x00
; 32-bit floating point representation of 0 (0 00000000 0...0), 1 (0 01111111 0...0),
;	2 (0 1000000 0100...0) and 3 (0 10000000 100...0)

section .text

%macro norm 3
	; xmm point_x 		: %1
	; xmm point_y 		: %2
	; xmm result	 	: %3
	
	; modifies			: %3, xmm15
	
	movdqu %3, %1
	movdqu xmm15, %2
	mulps %3, %3
	mulps xmm15, xmm15
	addps %3, xmm15
	sqrtps %3, %3
%endmacro

%macro projection_coefficient 7
	; xmm point_x			: %1
	; xmm point_y			: %2
	; xmm segment_from_x	: %3
	; xmm segment_from_y	: %4
	; xmm segment_to_x		: %5
	; xmm segment_to_y		: %6
	; xmm result			: %7
	
	; modifies				: %7, xmm11, xmm12, xmm13, xmm14, xmm15
	
	; Let 	p = (segment_from_x, segment_from_y)
	;		q = (segment_to_x, segment_to_y)
	;		point = (point_x, point_y)
	movdqu xmm11, %5
	subps xmm11, %3														; xmm11 = q.x - p.x
	
	movdqu xmm12, %6
	subps xmm12, %4														; xmm12 = q.y - p.y
	
	norm xmm11, xmm12, xmm13													
	mulps xmm13, xmm13													; xmm13 = ||q - p||^2															
	
	movdqu xmm14, %1
	subps xmm14, %3														; xmm13 = point.x - p.x
	
	movdqu xmm15, %2
	subps xmm15, %4														; xmm14 = point.y - p.y
	
	mulps xmm11, xmm14
	mulps xmm12, xmm15
	
	movdqu %7, xmm11
	addps %7, xmm12														; %7 = <x - p, q - p>
	
	divps %7, xmm13														; %7 = <x - p, q - p> / ||q - p||^2
%endmacro

%macro unit_perpendicular_coefficient 7
	; xmm point_x			: %1
	; xmm point_y			: %2
	; xmm segment_from_x	: %3
	; xmm segment_from_y	: %4
	; xmm segment_to_x		: %5
	; xmm segment_to_y		: %6
	
	; xmm result			: %7
	
	; modifies				: %7, xmm11, xmm12, xmm13, xmm14, xmm15
	
	; Let 	p = (segment_from_x, segment_from_y)
	;		q = (segment_to_x, segment_to_y)
	;		point = (point_x, point_y)
	
	movdqu xmm11, %5
	subps xmm11, %3														; xmm11 = q.x - p.x
	
	movdqu xmm12, %6
	subps xmm12, %4														; xmm12 = q.y - p.y
	
	norm xmm11, xmm12, xmm13											; xmm15 = ||q - p||																						
	
	movdqu xmm14, xmm11
	movdqu xmm11, xmm12
	pxor xmm12, xmm12
	subps xmm12, xmm14													
	
	; perpendicular(q - p) = (xmm11, xmm12)
	
	movdqu xmm14, %1
	subps xmm14, %3														; xmm14 = point.x - p.x
	
	movdqu xmm15, %2
	subps xmm15, %4														; xmm15 = point.y - p.y
	
	mulps xmm11, xmm14
	mulps xmm12, xmm15
	
	movdqu %7, xmm11
	addps %7, xmm12														; %7 = <x - p, perpendicular(q - p)>
	
	divps %7, xmm13														; %7 = <x - p, perpendicular(q - p)> / ||q - p||
%endmacro

%macro distance 7
	; xmm point_x			: %1
	; xmm point_y			: %2
	; xmm segment_from_x	: %3
	; xmm segment_from_y	: %4
	; xmm segment_to_x		: %5
	; xmm segment_to_y		: %6
	
	; xmm result			: %7
	; modifies				: %7, xmm10, xmm11, xmm12, xmm13, xmm14, xmm15
	
	projection_coefficient %1, %2, %3, %4, %5, %6, xmm10
	
	; Let 	p = (segment_from_x, segment_from_y)
	;		q = (segment_to_x, segment_to_y)
	;		point = (point_x, point_y)
	
	movdqu xmm11, xmm10
	movd xmm14, [mask_float_one]
	pshufb xmm14, [mask_copy_lowest_dword]								; xmm14 = 1 | 1 | 1 | 1
	cmpps xmm11, xmm14, 2												; xmm11 = condition xmm10 <= 1
	
	movdqu xmm12, xmm10
	pxor xmm14, xmm14													; xmm14 = 0 | 0 | 0 | 0
	cmpps xmm12, xmm14, 5												; xmm12 = condition xmm10 >= 0
	
	movdqu xmm13, xmm11
	pand xmm13, xmm12													; xmm13 = condition xmm10 >= 0 and xmm10 <= 1
	
	movdqu xmm11, xmm10
	pxor xmm14, xmm14
	cmpps xmm11, xmm14, 1												; xmm11 = condition xmm10 < 0
	
	movdqu xmm12, xmm10
	movd xmm14, [mask_float_one]
	pshufb xmm14, [mask_copy_lowest_dword]								; xmm14 = 1 | 1 | 1 | 1
	cmpps xmm12, xmm14, 6												; xmm12 = condition xmm10 > 1
	
	movdqu [condition_1], xmm11
	movdqu [condition_2], xmm12
	movdqu [condition_3], xmm13
	unit_perpendicular_coefficient xmm0, xmm1, xmm4, xmm5, xmm6, xmm7, xmm10

	pand xmm10, [mask_turn_off_highest_bit_dwords]						; xmm10 = abs(unit_perpendicular_coeffient x, p, q)
	pand xmm10, [condition_3]
	movdqu %7, xmm10
	
	movdqu xmm12, %1
	movdqu xmm13, %2
	movdqu xmm14, %3
	movdqu xmm15, %4
	subps xmm12, xmm14
	subps xmm13, xmm15
	norm xmm12, xmm13, xmm14											; xmm14 = ||point - p||

	pand xmm14, [condition_1]
	por %7, xmm14
	
	movdqu xmm12, %1
	movdqu xmm13, %2
	movdqu xmm14, %5
	movdqu xmm15, %6
	subps xmm12, xmm14
	subps xmm13, xmm15
	norm xmm12, xmm13, xmm14											; xmm14 = ||point - q||
	pand xmm14, [condition_2]
	por %7, xmm14
%endmacro

%macro compute_weight 7
	; xmm point_x			: %1
	; xmm point_y			: %2
	; xmm segment_from_x	: %3
	; xmm segment_from_y	: %4
	; xmm segment_to_x		: %5
	; xmm segment_to_y		: %6
	
	; xmm result			: %7
	; modifies				: %7, xmm10, xmm11, xmm12, xmm13, xmm14, xmm15
	
	; Let 	p = (segment_from_x, segment_from_y)
	;		q = (segment_to_x, segment_to_y)
	;		point = (point_x, point_y)
	
	distance %1, %2, %3, %4, %5, %6, %7

	movdqu xmm12, %5
	movdqu xmm13, %6
	subps xmm12, %3
	subps xmm13, %4
	norm xmm12, xmm13, xmm14											; xmm14 = length = ||q - p||
		
	; We use p = 0.5, b = 2, a = 0.125
	sqrtps xmm14, xmm14
	
	movd xmm12, [mask_float_eps]
	pshufb xmm12, [mask_copy_lowest_dword]
	addps %7, xmm12
	
	divps xmm14, %7
	
	mulps xmm14, xmm14
	
	movdqu %7, xmm14
%endmacro

%macro compute_src_point 5
	; xmm u				: %1
	; xmm v				: %2
	; r64 src_segment	: %3
	
	; xmm, xmm result	: %4, %5
	; modifies			: xmm10, xmm11, xmm12, xmm13, xmm14, xmm15
	
	movd %4, [%3 + FROM_OFFSET + X_OFFSET]
	pshufb %4, [mask_copy_lowest_dword]
	
	movd %5, [%3 + FROM_OFFSET + Y_OFFSET]
	pshufb %5, [mask_copy_lowest_dword]
	
	movd xmm10, [%3 + TO_OFFSET + X_OFFSET]
	pshufb xmm10, [mask_copy_lowest_dword]
	
	movd xmm11, [%3 + TO_OFFSET + Y_OFFSET]
	pshufb xmm11, [mask_copy_lowest_dword]
	
	subps xmm10, %4
	subps xmm11, %5
	
	norm xmm10, xmm11, xmm13
	
	movdqu xmm12, %2
	divps xmm12, xmm13													; xmm12 = v / norm(sub)
	
	movdqu xmm13, xmm11
	pxor xmm14, xmm14
	subps xmm14, xmm10													; Perpendicular(sub) = (xmm13, xmm14)
	
	mulps xmm13, xmm12
	mulps xmm14, xmm12													; (v / norm(sub)) Perpendicular(sub) = (xmm13, xmm14)
	
	mulps xmm10, %1
	mulps xmm11, %1														; u sub = (xmm10, xmm11)
	
	addps %4, xmm10
	addps %5, xmm11
	addps %4, xmm13
	addps %5, xmm14
%endmacro

%macro evaluate 4
	; r64 curve 		: %1
	; xmm t 			: %2
	
	; xmm, xmm result	: %3, %4
	; modifies			: %3, %4, xmm15

	movd %3, [%1 + A1_OFFSET]											; %3 = x | x | x | curve.a1
	pshufb %3, [mask_copy_lowest_dword]									; %3 = curve.a1 | ... | curve.a1
	
	movd xmm15, [%1 + B1_OFFSET]										; xmm15 = x | x | x | curve.b1
	pshufb xmm15, [mask_copy_lowest_dword]								; xmm15 = curve.b1 | ... | curve.b1
	
	mulps %3, %2
	addps %3, xmm15														; %3 = curve.a1 * t + curve.b1

	movd %4, [%1 + A2_OFFSET]											; %4 = x | x | x | curve.a2 
	pshufb %4, [mask_copy_lowest_dword]									; %4 = curve.a2 | ... | curve.a2
	
	movd xmm15, [%1 + B2_OFFSET]										; xmm15 = x | x | x | curve.b2
	pshufb xmm15, [mask_copy_lowest_dword]								; xmm15 = curve.b2 | ... | curve.b2
	
	mulps %4, %2
	addps %4, xmm15														; %4 = curve.a2 * t + curve.b2
%endmacro

; void compute_weighted_src_pixel(	float dst_point_x,
;									float dst_point_y,
;									segment *src_segments,
;									curve *from_interpolations, 
;									curve *to_interpolations,
;									int n_segments,
;									float t,
;									int width,
;									int height)
compute_weighted_src_pixel_asm:
	; dst_point_x 			: xmm0
	; dst_point_y 			: xmm1
	; src_segments 			: rdi
	; from_interpolations 	: rsi
	; to_interpolations 	: rdx
	; n_segments 			: rcx
	; t 					: xmm2
	; width 				: r8
	; height 				: r9
	
	; Stores the result in src_pixel_x and src_pixel_y

	push rbp
	mov rbp, rsp
	push rbx
	push r12
	push r13
	push r14
	push r15
	sub rsp, 8
	
	pshufb xmm0, [mask_copy_lowest_dword]															; xmm0 = x | x + 1 | x + 2 | x + 3
	movdqu xmm15, [mask_float_zero_one_two_three]
	addps xmm0, xmm15
	
	pshufb xmm1, [mask_copy_lowest_dword]
	
	pshufb xmm2, [mask_copy_lowest_dword]																		
	movdqu [time], xmm2
	
	; clear upper bits
	shl rcx, 4
	shr rcx, 4
	shl r8, 4
	shr r8, 4
	shl r9, 4
	shr r9, 4
	
	mov r12, rdi																					; r12 = src_segments
	mov r13, rsi																					; r13 = from_interpolations
	mov r14, rdx																					; r14 = to_interpolations
	mov r15, rcx																					; r15 = n_segments
	
	pxor xmm2, xmm2
	pxor xmm3, xmm3
	
	movdqu [weight_sum], xmm2
	
	xor rbx, rbx
	.loop:
		movdqu xmm8, [time]																			; xmm8 = t
		
		; Compute dst_segment.from = evaluate(from_interpolations[i], t)
		mov rax, rbx
		xor rdx, rdx
		mov rdx, CURVE_SIZE
		imul rdx
		add rax, r13																				; rax = from_interpolations + CURVE_SIZE * i
		
		evaluate rax, xmm8, xmm4, xmm5																; xmm4 = x coordinates of dst_segment.from
																									; xmm5 = y coordinates of dst_segment.from
		
		; Compute dst_segment.to = evaluate(to_interpolations[i], t)
		mov rax, rbx
		xor rdx, rdx
		mov rdx, CURVE_SIZE
		imul rdx
		add rax, r14																				; rax = to_interpolations + CURVE_SIZE * i
		
		evaluate rax, xmm8, xmm6, xmm7																; xmm6 = x coordinates of dst_segment.to
																									; xmm7 = y coordinates of dst_segment.to
		
		; Compute u = projection_coefficient(dst_point, dst_segment)
		projection_coefficient xmm0, xmm1, xmm4, xmm5, xmm6, xmm7, xmm8								; xmm8 = projection_coefficient(dst_point, dst_segment)
		
		; Compute v = unit_perpendicular_coeffient(dst_point, dst_segment)
		unit_perpendicular_coefficient xmm0, xmm1, xmm4, xmm5, xmm6, xmm7, xmm9						; xmm9 = unit_perpendicular_coeffient(dst_point, dst_segment)
		
		; We need to free one xmm
		movdqu [coefficient_v], xmm9
		
		; weight = compute_weight(dst_point, dst_segment)
		compute_weight xmm0, xmm1, xmm4, xmm5, xmm6, xmm7, xmm9
		
		movdqu xmm4, xmm9
		movdqu xmm5, xmm8
		movdqu xmm6, [coefficient_v]
		
		; xmm4 = weight
		; xmm5 = u
		; xmm6 = v
		
		mov rax, rbx
		mov rdx, SEGMENT_SIZE
		imul rdx
		add rax, r12
		
		compute_src_point xmm5, xmm6, rax, xmm7, xmm8												; src_point = (xmm7, xmm8)

		subps xmm7, xmm0
		subps xmm8, xmm1																			; disp = (xmm7, xmm8)

		mulps xmm7, xmm4
		mulps xmm8, xmm4
		
		addps xmm2, xmm7
		addps xmm3, xmm8
		
		movdqu xmm5, [weight_sum]
		addps xmm5, xmm4
		movdqu [weight_sum], xmm5																	; weight_sum += weight
		
		inc rbx
		cmp rbx, r15
		jne .loop
	.end_loop:
	
	movdqu xmm4, [weight_sum]
	
	; dst_point = (xmm0, xmm1)
	; disp_sum = (xmm2, xmm3)
	; weight_sum = xmm4
	
	movd xmm5, r15d
	pshufb xmm5, [mask_copy_lowest_dword]
	pxor xmm7, xmm7
	pcmpgtd xmm5, xmm7																				; xmm5 = condition n_segments > 0
	
	movd xmm6, r15d
	pshufb xmm6, [mask_copy_lowest_dword]
	pxor xmm7, xmm7
	pcmpeqd xmm6, xmm7																				; xmm6 = condition n_segments = 0
	
	movdqu xmm8, xmm0
	movdqu xmm9, xmm1
	
	pand xmm8, xmm6
	pand xmm9, xmm6
	
	divps xmm2, xmm4
	divps xmm3, xmm4
	addps xmm0, xmm2
	addps xmm1, xmm3
	
	pand xmm0, xmm5
	pand xmm1, xmm5
	
	por xmm0, xmm8
	por xmm1, xmm9
	
	; src_point = (xmm0, xmm1)
	
	pxor xmm2, xmm2
	maxps xmm0, xmm2
	
	dec r8d
	movd xmm2, r8d
	pshufb xmm2, [mask_copy_lowest_dword]
	cvtdq2ps xmm2, xmm2
	minps xmm0, xmm2
	
	pxor xmm2, xmm2
	maxps xmm1, xmm2
	
	dec r9d
	movd xmm2, r9d
	pshufb xmm2, [mask_copy_lowest_dword]
	cvtdq2ps xmm2, xmm2
	minps xmm1, xmm2

	cvtps2dq xmm0, xmm0
	cvtps2dq xmm1, xmm1
	
	movdqu [src_pixel_x], xmm0
	movdqu [src_pixel_y], xmm1
	
	add rsp, 8
	pop r15
	pop r14
	pop r13
	pop r12
	pop rbx
	pop rbp
	
	ret
