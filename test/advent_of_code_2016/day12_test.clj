(ns advent-of-code-2016.day12-test
  (:require [advent-of-code-2016.day12 :as sut]
            [clojure.test :refer :all]
            [clojure.string :as str]))

(deftest inst-arg-string-test
  (testing "string case"
    (let [state {"a" 1}]
      (is (= (sut/inst-arg state "a") 1)))))

(deftest inst-arg-number-test
  (testing "number case"
    (let [state {"a" 1}]
      (is (= (sut/inst-arg state 10) 10)))))

(deftest copy-register-test
  (testing "copy register b to a"
    (let [state {"a" 1 "b" 2} exp-state {"a" 2 "b" 2}]
      (is (= (sut/copy "b" "a" state) exp-state)))))

(deftest copy-number-test
  (testing "copy 10 to a"
    (let [state {"a" 1 "b" 2} exp-state {"a" 10 "b" 2}]
      (is (= (sut/copy 10 "a" state) exp-state)))))

(deftest increment-test
  (testing "increment register a"
    (let [state {"a" 1 "b" 2} exp-state {"a" 2 "b" 2}]
      (is (= (sut/increment "a" state) exp-state)))))

(deftest decrement-test
  (testing "decrement register a"
    (let [state {"a" 1 "b" 2} exp-state {"a" 0 "b" 2}]
      (is (= (sut/decrement "a" state) exp-state)))))

(deftest jump-registers-test
  (testing "jump using only register arguments"
    (let [state {"a" 11 "b" 12 :pc 10} exp-state {"a" 11 "b" 12 :pc 21}]
      (is (= (sut/jump "a" "b" state) exp-state)))))

(deftest jump-numbers-test
  (testing "jump using only number arguments"
    (let [state {"a" 11 "b" 12 :pc 10} exp-state {"a" 11 "b" 12 :pc 11}]
      (is (= (sut/jump 1 2 state) exp-state)))))

(deftest no-jump-test
  (testing "no jump case where first arg is 0"
    (let [state {"a" 11 "b" 12 :pc 10} exp-state {"a" 11 "b" 12 :pc 10}]
      (is (= (sut/jump 0 2 state) exp-state)))))

(deftest form-full-instruction-test
  (testing "the full instruction from [sut/increment \"a\"] should be equivalent to #(increment \"a\" %)"
    (let [instruction-and-args [sut/increment "a"]
          full-instruction (sut/form-full-instruction instruction-and-args)
          state {"a" 11 "b" 12}
          exp-state {"a" 12 "b" 12}]
      (is (= (full-instruction state) exp-state)))))

(deftest exec-next-instruction-test
  (testing "a single iteration through a program"
    (let [program [[sut/increment "a"] [sut/decrement "b"]]
          state {"a" 11 "b" 12 :pc 0 :program program}
          exp-state {"a" 12 "b" 12 :pc 1 :program program}]
      (is (= (sut/exec-next-instruction state) exp-state)))))

(deftest exec-instructions-test
  (testing "run a full program"
    (let [program [[sut/increment "a"]
                   [sut/decrement "b"]
                   [sut/copy "b" "c"]
                   [sut/jump "b" -3]]
          state {"a" 11 "b" 12 "c" 0 :pc 0 :program program}
          exp-state {"a" 23 "b" 0 "c" 0 :pc 4 :program program}]
      (is (= (sut/exec-instructions state) exp-state)))))

(deftest transform-string-arg-test
  (testing "\"a\" becomes \"a\""
    (is (= (sut/transform-arg "a") "a"))))

(deftest transform-integer-arg-test
  (testing "\"1\" becomes 1"
    (is (= (sut/transform-arg "1") 1))))

(deftest transform-line-test
  (testing "transform a single line"
    (is (= (sut/transform-line {"cpy" sut/copy} "cpy a b") [sut/copy "a" "b"]))))

(deftest parse-input-test
  (testing "transform a short program"
    (let [input "cpy a b\ncpy 1 b"
          result (sut/parse-input {"cpy" sut/copy} input)
          exp-result [[sut/copy "a" "b"]
                      [sut/copy 1 "b"]]]
      (is (= result result)))))

(deftest execute-test
  (testing "execute a short test program"
    (let [instruction-map {"cpy" sut/copy "inc" sut/increment
                           "dec" sut/decrement "jnz" sut/jump}
          registers {"a" 0 "b" 0}
          input "cpy 41 a\ninc a\ninc a\ndec a\njnz a 2\ndec a"
          exp-program [[sut/copy 41 "a"]
                       [sut/increment "a"]
                       [sut/increment "a"]
                       [sut/decrement "a"]
                       [sut/jump "a" 2]
                       [sut/decrement "a"]]
          exp-final-state {"a" 42 "b" 0 :pc 6 :program exp-program}]
      (is (= (sut/execute instruction-map registers input)
             exp-final-state)))))
