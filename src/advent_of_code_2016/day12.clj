(ns advent-of-code-2016.day12
  (:require [clojure.string :as str]))

(defn inst-arg
  "if arg is a number, returns that number, otherwise uses arg as a key from state and returns that value"
  [state arg]
  (if (number? arg)
    arg
    (state arg)))

(defn copy
  [x1 x2 state]
  (assoc state x2 (inst-arg state x1)))

(defn increment
  [x state]
  (update state x inc))

(defn decrement
  [x state]
  (update state x dec))

(defn jump
  [x1 x2 state]
  (if (zero? (inst-arg state x1))
    state
    (update state :pc #(+ % (inst-arg state x2) -1))))

(defn form-full-instruction
  [instruction-and-args]
  (let [[instruction & args] instruction-and-args]
    #(apply instruction (flatten (conj (list %) args)))))

(defn exec-next-instruction
  "exec an instruction and increment the program counter"
  [state]
  (let [pc (state :pc) program (state :program)
        instruction-and-args (program pc)
        full-instruction (form-full-instruction instruction-and-args)]
    (update (full-instruction state) :pc inc)))

(defn exec-instructions
  [state]
  (let [pc (state :pc)
        program-length (count (state :program))]
    (if (or (< pc 0) (>= pc program-length))
      state
      (recur (exec-next-instruction state)))))

(defn transform-arg
  [arg]
  (if (re-matches #"\d+" arg)
    (Integer/parseInt arg)
    arg))

(defn transform-line
  [instruction-map line]
  (let [[instruction & args] (str/split line #" ")
        instruction-fn [(instruction-map instruction)]
        transformed-args (map transform-arg args)]
    (reduce conj instruction-fn transformed-args)))

(defn parse-input
  [instruction-map input]
  (mapv #(transform-line instruction-map %) (str/split-lines input)))

(defn execute
  [instruction-map registers input]
  (let [program (parse-input instruction-map input)
        initial-state (merge registers {:pc 0 :program program})]
    (exec-instructions initial-state)))
