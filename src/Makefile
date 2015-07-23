CC = gcc
CFLAGS = -std=c99 -Wall -pedantic -m64 -O2
CLIBS = -lm
OPENCVFLAGS = `pkg-config --cflags opencv`
OPENCVLIBS = `pkg-config --libs opencv`

BIN = morph

OBJS = main.o morph.o curve.o utils.o algebra.o

.PHONY: all clean

$(BIN): $(OBJS)
	$(CC) $(CFLAGS) -o $@ $^ $(OPENCVLIBS) $(CLIBS)

%.o: %.c
	$(CC) $(CFLAGS) -c $< $(OPENCVFLAGS)

all: $(BIN)

clean:
	rm -f $(OBJS)
	rm -f $(BIN)