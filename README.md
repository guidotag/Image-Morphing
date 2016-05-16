Image-Morphing
==============

Final project for the Computer Organization II course, at the University of Buenos Aires, Faculty of Exact and Natural Sciences.

## Hardware prerequisites

An x86-64 processor with SSE3 instructions or better.

## Software prerequisites

- Intel C compiler, which is included in the Intel® Parallel Studio XE packages. You can download a free trial, or a full version for academic usage only, both from Intel's webpage. Alternatively, you can use the GCC compiler, and change the CC variable in the `Makefile`.

- OpenCV 2.4 or better.

## Configuration

Run `config.sh`, which just creates an output folder.

## Usage

To compile and execute the program, run

```
make
./morph
```

This will process all the pair of images defined in the `main.c` file. All the input images are located in the `input` folder. The output will be in the previously created `output` folder. To morph your own pair of images, you'll need to manually define the segments of each one in the main file.

## How does it work?

Information about the morphing process can be found in `informe/informe.pdf`. This document is in Spanish.
