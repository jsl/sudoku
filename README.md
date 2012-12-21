# sudoku

A simple sudoku solver.

## Usage

(def game
  [['x  5  8 'x  4 'x  9 'x 'x]
   ['x 'x  9  7 'x  8 'x 'x  4]
   [ 2 'x 'x  5  3 'x 'x  8 'x]
   [ 9 'x 'x 'x 'x 'x 'x  2  3]
   ['x 'x 'x  3  9  4 'x 'x 'x]
   [ 4  6 'x 'x 'x 'x 'x 'x  5]
   ['x  4 'x 'x  7  5 'x 'x  1]
   [ 7 'x 'x  1 'x  6  3 'x 'x]
   ['x 'x  6 'x  2 'x  5  7 'x]])

(solve game)

## License

Copyright (C) 2012 Justin Leitgeb

Distributed under the Eclipse Public License, the same as Clojure.
