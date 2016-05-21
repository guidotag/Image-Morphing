JC = javac
JFLAGS = -g

.PHONY: all clean

all: Main.class

%.class: %.java
	$(JC) $(JFLAGS) $<

clean:
	rm -f *.class