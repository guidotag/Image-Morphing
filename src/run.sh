bin=morph
src=../input/bill.bmp
dst=../input/laurie.bmp
out=../output/billaurie.avi

clear
clear
make clean
make
./${bin} ${src} ${dst} ${out}