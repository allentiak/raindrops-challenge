(ns allentiak.challenges.raindrops.core-test
  (:require
   [allentiak.challenges.raindrops.core :as sut]
   [expectations.clojure.test :refer [defexpect expect expecting]]))

(defexpect core-should
  (expecting "base cases"
             (expect (= (sut/raindrops 2) "pling"))
             (expect (= (sut/raindrops 3) "plang"))
             (expect (= (sut/raindrops 5) "plong"))
             (expect (= (sut/raindrops 17) "tshäng")))

  (expecting "other cases"
    ;; for all integers from 1 to +infinite, except 2,3,5,and 17...
    ;; that are not divisible by any of the base case numbers
             (expect (= (sut/raindrops 1) "blob"))
             (expect (= (sut/raindrops 7) "blob"))
             (expect (= (sut/raindrops 113) "blob")))

  (expecting "more base cases"
             (expect (= (sut/raindrops 4) "pling"))
             (expect (= (sut/raindrops 9) "plang"))
             (expect (= (sut/raindrops 25) "plong"))
             (expect (= (sut/raindrops 119) "tshäng"))))
