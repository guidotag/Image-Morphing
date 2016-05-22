JC = javac
JFLAGS = -g
JAVA_FILES = $(wildcard *.java)
CLASS_FILES = $(JAVA_FILES:.java=.class)

.PHONY: all clean

all: $(CLASS_FILES)

%.class: %.java
	$(JC) $(JFLAGS) $<

clean:
	rm -f *.class