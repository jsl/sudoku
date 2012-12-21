(ns sudoku.core
  (:require [clojure.set :as set]))

(defn- solved? [value]
  (not (= 'x value)))

(defn- unsolved? [value]
  (not (solved? value)))

(defn- solved [collection]
  (set (filter #(not (unsolved? %)) collection)))

(defn- numbers-in-row [game x]
  (solved (game x)))

(defn- numbers-in-column [game y]
  (solved (map #(% y) game)))

(defn- block-start [position]
  (* (int (/ position 3)) 3))

(defn- block-end [position]
  (+ (block-start position) 3))

(defn- numbers-in-box [game x y]
  (solved (reduce into (map #(subvec % (block-start x) (block-end x))
              (subvec game (block-start y) (block-end y))))))

(defn- count-unsolved [game]
  (count (filter unsolved? (reduce into game))))

(defn- available-numbers [game x y]
  (set/difference (set (range 1 10))
                  (set/union (numbers-in-row game y)
                             (numbers-in-column game x)
                             (numbers-in-box game x y))))

(defn- random-valid-number [game x y]
  (rand-nth (vec (available-numbers game x y))))

(defn- next-position [x y]
  (if (> x 7)
    [0 (+ y 1)]
    [(+ x 1) y]))

(defn- final-board-position? [x y]
  (and (= x 8) (= y 8)))

(defn- complete? [game]
  (not (some unsolved? (reduce into game))))

(defn- update-game [game x y]
  (if (and (unsolved? (get-in game [y x]))
           (not (empty? (available-numbers game x y))))
    (assoc-in game [y x] (random-valid-number game x y))
    game))

(defn- solve-cell [game x y]
  (let [available-nums (available-numbers game x y)]
    (if (and (unsolved? (get-in game [y x]))
             (= 1 (count available-nums)))
      (assoc-in game [y x] (first available-nums))
      game)))

(defn- plug-and-play
  "Passes over board once, filling each cell that has only one possible value"
  ([game] (plug-and-play game 0 0))
  ([game x y]
     (let [solved (solve-cell game x y)
           next-pos (next-position x y)]
       (if (final-board-position? x y)
         solved
         (recur solved (next-pos 0) (next-pos 1))))))

(defn- fill-known-cells
  "Recursively iterates over game until no more known cells can be filled"
  [game]
  (let [filled-game (plug-and-play game)]
    (if (= game filled-game)
      game
      (recur filled-game))))

(defn- guess-solution
  "Recursively iterates over game, guessing possible values for each cell"
  ([game] (guess-solution game 0 0))
  ([game x y]
     (let [solved (fill-known-cells (update-game game x y))
           next-pos (next-position x y)]
       (if (final-board-position? x y)
         solved
         (if (and (unsolved? (get-in solved [y x]))
                  (empty? (available-numbers game x y)))
           solved
           (recur solved (next-pos 0) (next-pos 1)))))))

(defn solve
  "Solves the game"
  ([game] (solve game 0))
  ([game attempts]
     (let [result (guess-solution (fill-known-cells game))]
       (if (complete? result)
         (do
           (prn (str "Attempts before solution: " attempts))
           result)
         (recur game (+ attempts 1))))))
