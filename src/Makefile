CC = gcc
ASM = nasm
CFLAGS = -std=c99 -Wall -pedantic -m64 -O3
ASMFLAGS = -felf64 -g -F dwarf
CLIBS = -lm
OPENCVFLAGS = `pkg-config --cflags opencv`
OPENCVLIBS = `pkg-config --libs opencv`

OBJS = morph.o magic.o curve.o utils.o algebra.o

BIN = morph
MAIN = main.o
TESTBIN = test
TESTMAIN = tests.o

.PHONY: all test clean

$(BIN): $(OBJS) $(MAIN)
	$(CC) $(CFLAGS) -o $@ $^ $(OPENCVLIBS) $(CLIBS)

$(TESTBIN): $(OBJS) $(TESTMAIN)
	$(CC) $(CFLAGS) -o $@ $^ $(OPENCVLIBS) $(CLIBS)

%.o: %.c
	$(CC) $(CFLAGS) -c $< $(OPENCVFLAGS)
	
%.o: %.asm
	$(ASM) $(ASMFLAGS) -o $@ $^

all: $(BIN)

test: $(TESTBIN)

clean:
	rm -f $(OBJS)
	rm -f $(MAIN)
	rm -f $(TESTMAIN)
	rm -f $(BIN)
	rm -f $(TESTBIN)
