JC = javac
JFLAGS = -g

.PHONY: all clean

all: Main.class

%.class: %.java
	$(JC) $(JFLAGS) $<

clean:
	rm -f IMFrame.class
	rm -f ImagePanel.class
	rm -f Main.class
	rm -f Main\$$1.class