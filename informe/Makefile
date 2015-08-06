TEX = pdflatex
BIB = bibtex
main = informe

.PHONY = all clean

all: $(main).tex $(main).bbl $(main).blg
	$(TEX) $(main).tex
	
$(main).bbl $(main).blg: $(main).bib $(main).aux
	$(BIB) $(main)

$(main).aux: $(main).aux
	$(TEX) $(main).tex

$(main).bib: $(main).tex
	$(TEX) $(main).tex
	
clean:
	rm -f $(main).aux
	rm -f $(main).log
	rm -f $(main).toc
	rm -f $(main).out
	rm -f $(main).pdf
	rm -f $(main).synctex.gz
	rm -f $(main).bbl
	rm -f $(main).blg
