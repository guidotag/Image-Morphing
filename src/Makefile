CC = gcc
ASM = nasm
CFLAGS = -std=c99 -Wall -pedantic -m64 -O3
ASMFLAGS = -felf64 -g -F dwarf
CLIBS = -lm
OPENCVFLAGS = `pkg-config --cflags opencv`
OPENCVLIBS = `pkg-config --libs opencv`

OBJS = main.o morph.o magic.o curve.o utils.o algebra.o

BIN = morph

.PHONY: all test clean

$(BIN): $(OBJS)
	$(CC) $(CFLAGS) -o $@ $^ $(OPENCVLIBS) $(CLIBS)

%.o: %.c
	$(CC) $(CFLAGS) -c $< $(OPENCVFLAGS)
	
%.o: %.asm
	$(ASM) $(ASMFLAGS) -o $@ $^

all: $(BIN)

clean:
	rm -f $(OBJS)
	rm -f $(BIN)
