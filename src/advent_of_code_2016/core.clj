(ns advent-of-code-2016.core
  (:require [advent-of-code-2016.day1 :as day1])
  (:gen-class))

(defn do-day1
  [part]
  (let [input (slurp (clojure.java.io/resource "day1.txt"))]
    (cond
      (= part \a) (day1/part1 input)
      (= part \b) (day1/part2 input)
      :else "Invalid Part")))

(defn pick-day
  [day part]
  (cond
    (= day \1) (do-day1 part)
    :else "Invalid Day"))

(defn -main
  "Menu for Advent of Code 2016 Days and Parts"
  [[day part] & args]
  (println (pick-day day part)))
