(ns advent-of-code-2016.core
  (:gen-class)
  (:require [advent-of-code-2016.day1 :as day1])
  (:require [advent-of-code-2016.day12 :as day12]))

(defn do-day1
  [part]
  (let [input (slurp (clojure.java.io/resource "day1.txt"))]
    (cond
      (= part \a) (day1/part1 input)
      (= part \b) (day1/part2 input)
      :else "Invalid Part")))

(defn do-day12
  [part]
  (cond
    (= part \a) (let [instruction-map {"cpy" day12/copy
                                       "inc" day12/increment
                                       "dec" day12/decrement
                                       "jnz" day12/jump}
                      registers {"a" 0 "b" 0 "c" 0 "d" 0}
                      input (slurp (clojure.java.io/resource "day12a.txt"))]
                  (day12/execute instruction-map registers input))
    :else "Invalid Part"))

(defn pick-day
  [day part]
  (cond
    (= day \1) (do-day1 part)
    (= day \2) (do-day12 part)
    :else "Invalid Day"))

(defn -main
  "Menu for Advent of Code 2016 Days and Parts"
  [[day part] & args]
  (println (pick-day day part)))
