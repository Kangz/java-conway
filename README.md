Java-conway
===========
This is an implementation of Conway's Game of Life in java made with Cyprien Mangin. It is a school project I'd thought I'd share as it has this interesting "reverse time" feature.

Garbage Collector
-----------------
Has Java's default GC is not suited for Hashlife you might want to run the program with the `-verbose:gc -Xmx6G -Xms6G -XX:+UseParallelOldGC -XX:ParallelGCThreads=8` switches (depending on your computer's specs).

Features
--------
- Gosper's Hashlife algorithm
- A responsive GUI with drag&drop and zoom levels
- Editing
- Saving and loading to RLE files
- A reverse time switch

Commands
--------
- Drag&drop - move around
- Mouse wheel - zoom / unzoom
- Right click - (when paused) toggle the cell under the mouse
- Arrows - move around
- I - zoom
- K - unzoom
- P - pause / resume
- Y - go quicker
- T - go slower
- R - reverse switch
- A - (unstable) switch between Hashlife and the naive algorithm
- numbers - enter a jump size
- J - jumps forward
- H - jumps backward
- Echap - resets the jump size
