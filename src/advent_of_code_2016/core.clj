(ns advent-of-code-2016.core
  (:gen-class)
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:require [clojure.string :as str])
  (:require [advent-of-code-2016.day1 :as day1])
  (:require [advent-of-code-2016.day12 :as day12])
  (:require [advent-of-code-2016.day23 :as day23]))

(defn do-day1
  [part]
  (let [input (slurp (clojure.java.io/resource "day1.txt"))]
    (case part
      1 (format "distance: %d" (day1/part1 input))
      2 (format "distance: %d" (day1/part2 input))
      (format "Unimplemented Part: %d" part))))

(defn do-day12
  [part]
  (let [instruction-map {"cpy" day12/copy
                         "inc" day12/increment
                         "dec" day12/decrement
                         "jnz" day12/jump}
        input (slurp (clojure.java.io/resource "day12a.txt"))]
  (case part
    1 (let [registers {"a" 0 "b" 0 "c" 0 "d" 0}
            result (day12/execute instruction-map registers input)]
        (format "register a: %d" (result "a")))
    2 (let [registers {"a" 0 "b" 0 "c" 1 "d" 0}
            result (day12/execute instruction-map registers input)]
        (format "register a: %d" (result "a")))
    (format "Unimplemented Part: %d" part))))

(defn do-day23
  [part]
  (let [instruction-map {"cpy" day12/copy
                         "inc" day12/increment
                         "dec" day12/decrement
                         "jnz" day12/jump
                         "tgl" day23/toggle}]
    (case part
      1 (let [registers {"a" 7 "b" 0 "c" 0 "d" 0}
              input (slurp (clojure.java.io/resource "day23part1.txt"))
              result (day12/execute instruction-map registers input)]
          (format "register a: %d" (result "a")))
      2 (let [registers {"a" 12 "b" 0 "c" 0 "d" 0}
              input (slurp (clojure.java.io/resource "day23part2.txt"))
              result (day12/execute instruction-map registers input)]
          (format "register a: %d" (result "a")))
      (format "Unimplemented Part: %d" part))))

(defn pick-day
  [day part]
  (case day
    1 (do-day1 part)
    12 (do-day12 part)
    23 (do-day23 part)
    (format "Unimplemented Day: %d" day)))

(defn usage [options-summary]
  (->> ["This program implements the tasks from Advent of Code 2016"
        ""
        "Usage: advent-of-code-2016 [options]"
        ""
        "Options:"
        options-summary]
       (str/join \newline)))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (str/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn -main
  "Menu for Advent of Code 2016 Days and Parts"
  [& args]
  (let [cli-options
        [["-d" "--day DAY" "Day number"
          :parse-fn #(Integer/parseInt %)
          :validate [#(<= 1 % 25) "Day must be between 1 and 25"]]
         ["-p" "--part PART" "Part number"
          :parse-fn #(Integer/parseInt %)
          :validate [#(<= 1 % 2) "Part must be between 1 and 2"]]
         ["-h" "--help"]]
        {:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (options :help) (exit 0 (usage summary))
      (not= (count arguments) 2) (exit 1 (count arguments))
      errors (exit 1 (error-msg errors))
      :else (println (pick-day (options :day) (options :part))))))
