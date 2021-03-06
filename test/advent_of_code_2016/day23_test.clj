(ns advent-of-code-2016.day23-test
  (:require [advent-of-code-2016.day23 :as sut]
            [advent-of-code-2016.day12 :as day12]
            [clojure.test :refer :all]))

(deftest toggled-arity-1-increment-test
  (testing "increment case"
    (is (= (apply sut/toggled [day12/increment "a"]) [day12/decrement "a"]))))

(deftest toggled-arity-1-not-increment-test
  (testing "not increment case"
    (is (= (apply sut/toggled [:not-increment "a"]) [day12/increment "a"]))))

(deftest toggled-arity-2-jump-test
  (testing "jump case"
    (is (= (apply sut/toggled [day12/jump "a" "b"]) [day12/copy "a" "b"]))))

(deftest toggled-arity-2-not-jump-test
  (testing "not jump case"
    (is (= (apply sut/toggled [:not-jump "a" "b"]) [day12/jump "a" "b"]))))

(deftest toggle-with-arity-1-test
  (testing "arity 1 case"
    (let [state {:pc 0 "a" 0 :program [[day12/increment "a"]
                                       [day12/copy "a" "b"]]}
          exp-state {:pc 0 "a" 0 :program [[day12/decrement "a"]
                                           [day12/copy "a" "b"]]}]
      (is (= (sut/toggle "a" state) exp-state)))))

(deftest toggle-with-arity-2-test
  (testing "arity 2 case"
    (let [state {:pc 0 "a" 1 :program [[day12/increment "a"]
                                       [day12/copy "a" "b"]]}
          exp-state {:pc 0 "a" 1 :program [[day12/increment "a"]
                                           [day12/jump "a" "b"]]}]
      (is (= (sut/toggle "a" state) exp-state)))))

(deftest toggle-out-of-bounds-test
  (testing "toggling an instruction outside the program should do nothing"
    (let [state {:pc 0 "a" 10 :program [[day12/increment "a"]
                                       [day12/copy "a" "b"]]}]
      (is (= (sut/toggle "a" state) state)))))

(deftest execute-test
  (testing "execute a sample program with new toggle instruction"
    (let [instruction-map {"cpy" day12/copy "inc" day12/increment
                           "dec" day12/decrement "jnz" day12/jump
                           "tgl" sut/toggle}
          registers {"a" 0 "b" 0}
          input "cpy 2 a\ntgl a\ntgl a\ntgl a\ncpy 1 a\ndec a\ndec a"
          exp-program [[day12/copy 2 "a"]
                       [sut/toggle "a"]
                       [sut/toggle "a"]
                       [day12/increment "a"]
                       [day12/jump 1 "a"]
                       [day12/decrement "a"]
                       [day12/decrement "a"]]
          exp-final-state {"a" 3 "b" 0 :pc 7 :program exp-program}]
      (is (= (day12/execute instruction-map registers input)
             exp-final-state)))))
