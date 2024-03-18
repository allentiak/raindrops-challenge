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

    (expecting "other base cases"
               ;; for all integers from 1 to +infinite, except 2,3,5,17...
               (expect (= (sut/raindrops 1) "blob"))))
