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

  (expecting "divisible by different divisors"
             (expect (= (sut/raindrops 6) "*PLING*, PLANG"))
             (expect (= (sut/raindrops 10) "*PLING* pling, PLONG"))
             (expect (= (sut/raindrops 34) "*PLING* pling, TSHÄNG"))
             (expect (= (sut/raindrops 510) "*PLING* pling, *PLANG* plang, *PLONG* plong, *TSHÄNG* tshäng")))

  (expecting "first transformation"
             (expect (= (sut/raindrops 4) "PLING"))))
