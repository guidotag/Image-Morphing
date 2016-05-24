Image-Morphing
==============

Final project for the course Computer Organization II, at the University of Buenos Aires, Faculty of Exact and Natural Sciences.

## Hardware prerequisites

An x86-64 processor with SSE3 instructions or better.

## Software prerequisites

- Intel C compiler, which is included in the Intel® Parallel Studio XE packages. You can download a free trial, or a full version for academic usage only, both from Intel's webpage. Alternatively, you can use the GCC compiler, by changing the CC variable in `src/Makefile`.

- OpenCV 2.4 or greater. Super easy downloading script at https://github.com/jayrambhia/Install-OpenCV.

- JDK 7 or greater.

## Building

Run `make` in the root folder. This will create a build folder with a `gui.jar` file and the morph binary.

## Usage

In the build folder, run `java -jar gui.jar`.

## How does it work?

Information about the morphing process can be found in `informe/informe.pdf`. This document is in Spanish.
