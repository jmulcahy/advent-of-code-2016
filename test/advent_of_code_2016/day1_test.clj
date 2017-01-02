(ns advent-of-code-2016.day1-test
  (:require [advent-of-code-2016.day1 :as sut]
            [clojure.test :refer :all]))

(deftest turn-left-test
  (testing "left of north (0, 1) is west (-1, 0)"
    (is (= (sut/turn-left [0 1]) [-1 0]))))

(deftest turn-right-test
  (testing "right of north (0, 1) is east (1, 0)"
    (is (= (sut/turn-right [0 1]) [1 0]))))

(deftest turn-right-then-left-test
  (testing "right then left gives identity"
    (is (= (sut/turn-left (sut/turn-right [0 1])) [0 1]))))

(deftest turn-left-then-left-test
  (testing "left then left from north gives south"
    (is (= (sut/turn-left (sut/turn-left [0 1])) [0 -1]))))

(deftest turn-right-then-right-test
  (testing "right then right from north gives south"
    (is (= (sut/turn-right (sut/turn-right [0 1])) [0 -1]))))

(deftest turn-test-1
  (testing "turn right of south (0, 1) to west (-1, 0)")
  (is (= (sut/turn [0 -1] \R) [-1 0])))

(deftest turn-test-2
  (testing "turn left of south (0, 1) to east (1, 0)")
  (is (= (sut/turn [0 -1] \L) [1 0])))

(deftest move-test
  (testing "move west 2 from (3, 4)"
    (is (= (sut/move [3 4] [-1 0] 2) [1 4]))))

(deftest turn-and-move-test
  (testing "R22 from north at (1, 2) is (23, 2)"
    (is (= (sut/turn-and-move [[1 2] [0 1]] "R22") [[23 2] [1 0]]))))

(deftest manhattan-distance-test
  (testing "Manhattan distance of (-1 10) is 11"
    (is (= (sut/manhattan-distance [-1 10]) 11))))

(deftest day1p1-test-1
  (testing "Following R2, L3 leaves you 2 blocks East and 3 blocks North, or 5 blocks away."
    (is (= (sut/part1 "R2, L3") 5))))

(deftest day1p1-test-2
  (testing "R2, R2, R2 leaves you 2 blocks due South of your starting position, which is 2 blocks away."
    (is (= (sut/part1 "R2, R2, R2") 2))))

(deftest dayp1p1-test-3
  (testing "R5, L5, R5, R3 leaves you 12 blocks away."
    (is (= (sut/part1 "R5, L5, R5, R3") 12))))

(deftest interpolate-positions-test
  (testing "all the points between (1, 2) and (5, 2)"
    (let [betweens '([2 2] [3 2] [4 2] [5 2])
          results (sut/interpolate-positions [1, 2] [5, 2])]
      (is (= results betweens)))))

(deftest first-match-test
  (testing "find a match"
    (let [maybe-matches (seq '((1 1) (2 1) (3 1) (4 1)))
          match-against (seq '((3 1) (0 0) (2 1)))]
      (is (= (sut/first-match maybe-matches match-against) '(3 1))))))

(deftest day1p2-test-1
  (testing "if your instructions are R8, R4, R4, R8, the first location you visit twice is 4 blocks away, due East"
    (is (= (sut/part2 "R8, R4, R4, R8") 4))))
