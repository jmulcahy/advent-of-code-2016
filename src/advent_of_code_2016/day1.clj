(ns advent-of-code-2016.day1
  (:require [clojure.string :as str]))

(defn turn-left
  [current-heading]
  (vector (* (second current-heading) -1)
          (* (first current-heading) 1)))

(defn turn-right
  [current-heading]
  (vector (* (second current-heading) 1)
          (* (first current-heading) -1)))

(defn turn
  [current-heading turn-direction]
  (if (= turn-direction \L)
    (turn-left current-heading)
    (turn-right current-heading)))

(defn move
  [current-position direction distance]
  (map + (map #(* distance %) direction) current-position))

(defn turn-and-move
  [current-position-and-heading direction]
  (let [[current-position current-heading] current-position-and-heading
        turn-direction (get direction 0)
        distance (read-string (re-find #"\d+" direction))
        new-heading (turn current-heading turn-direction)]
    (vector (move current-position new-heading distance) new-heading)))

(defn run-directions
  [initial-position-and-heading directions]
  (reduce turn-and-move initial-position-and-heading directions))

(defn abs
  [x]
  (if (pos? x)
    x
    (* -1 x)))

(defn manhattan-distance
  [coordinate]
  (if (nil? coordinate)
    nil
    (reduce + (map abs coordinate))))

(defn parse-day1
  [input]
  (str/split input #", "))

(defn part1
  [input]
  (let [directions (parse-day1 input)]
    (manhattan-distance (get (run-directions [[0 0] [0 1]] directions) 0))))

(defn scale-and-add
  [scalar start slope]
  (mapv + start (mapv #(* scalar %) slope)))

(defn interpolate-positions
  "Find all points unit distance apart between two points on grid
  start exclusive, end inclusive"
  [start-position end-position]
  (let [difference (map - end-position start-position)
        distance (manhattan-distance difference)
        heading (map #(/ % distance) difference)]
    (for [n (range 1 (inc distance))] (scale-and-add n start-position heading))))

(defn in
  [coll elem]
  (if (some #(= elem %) coll)
    elem))

(defn first-match
  "find the first match between an element of x1s and x2s"
  [x1s x2s]
  (first (filter some? (for [x2 x2s] (in x1s x2)))))

(defn run-directions-check-if-returned
  [initial-position-and-heading directions]
  (loop [position-and-heading initial-position-and-heading
         [direction & remaining-directions] directions
         visited-positions [(get initial-position-and-heading 0)]]
    (let [next-position-and-heading (turn-and-move position-and-heading direction)
          [position _] position-and-heading
          [next-position _] next-position-and-heading
          new-points (interpolate-positions position next-position)
          match (first-match new-points visited-positions)]
      (cond
        (boolean match) match
        (empty? remaining-directions) nil
        :else (recur next-position-and-heading remaining-directions
                     (into visited-positions new-points))))))

(defn part2
  [input]
  (let [directions (parse-day1 input)]
    (manhattan-distance
     (run-directions-check-if-returned [[0 0] [0 1]] directions))))
