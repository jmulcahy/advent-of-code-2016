(ns advent-of-code-2016.day23
  (:require [advent-of-code-2016.day12 :as day12]))

(defn toggled
  ([instruction x1]
   (vector (if (= instruction day12/increment)
             day12/decrement
             day12/increment) x1))
  ([instruction x1 x2]
   (vector (if (= instruction day12/jump)
             day12/copy
             day12/jump) x1 x2)))

(defn toggle
  [arg state]
  (let [program (state :program)
        target-pc (+ (state :pc) (day12/inst-arg state arg))
        update-program #(assoc program target-pc %)
        update-state #(assoc state :program (update-program %))]
    (if (<= 0 target-pc (count program))
      (update-state (apply toggled (program target-pc)))
      state)))
