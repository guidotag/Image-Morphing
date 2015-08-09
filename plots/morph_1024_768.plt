input_file_1 = "morph_1024_768_C.txt"
input_file_2 = "morph_1024_768_ASM.txt"
output_file = "morph_1024_768.tex"

reset

set terminal epslatex size 12cm, 7cm

set style line 1 linecolor rgb "red" linetype 1 pointtype 5 pointsize 1
set style line 2 lc rgb "blue" lt 13 pt 2 ps 1

set ylabel "TSC"
set xlabel "Cantidad de segmentos ($s$)"

set output output_file

plot input_file_1 using 1:2 with linespoints ls 1 title "C", \
	input_file_2 using 1:2 with linespoints ls 2 title "SIMD"
		
unset output
