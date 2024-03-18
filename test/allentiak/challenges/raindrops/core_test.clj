(ns allentiak.challenges.raindrops.core-test
  (:require
   [allentiak.challenges.raindrops.core :as sut]
   [expectations.clojure.test :refer [defexpect expect expecting]]))

(defexpect core-should
    (expecting "put 2 and 2 together :)"
          (expect (= (+ 2 2) 4))))
