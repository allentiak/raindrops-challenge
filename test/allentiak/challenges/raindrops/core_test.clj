(ns allentiak.challenges.raindrops.core-test
  (:require
   [allentiak.challenges.raindrops.core :as sut]
   [clojure.test :refer [use-fixtures]]
   [expectations.clojure.test :refer [defexpect expect expecting]]
   [malli.core :as m]
   [malli.generator :as mg]
   [malli.instrument :as mi]))

(defn with-collect!
  [test-fn]
  (mi/collect!)
  (mi/instrument!)
  (f))

(def valid-strings
  (m/schema
    #{"pling" "plang" "plong" "tshäng" "blob"}))

;; TODO: augmented-strings

(defn raindrops
  {:malli/schema
   [:=> [:cat pos-int?] valid-strings]}
  [n]
  (condp #(zero? (mod %2 %1)) n
    2 "pling"
    3 "plang"
    5 "plong"
    17 "tshäng"
    "blob"))

(def base-cases
  #{2 3 5 17})

(defn non-base-case?
  [n]
  (and (not (contains? base-cases n))
       (pos-int? n)))

(comment
  (non-base-case? 1)
;; => true
  (non-base-case? 10)
;; => true
  (non-base-case? 3)
;; => false
  ,)

(use-fixtures :once with-collect!)

(defexpect core-should
  (expecting "base cases"
             (expect (= (raindrops 2) "pling"))
             (expect (= (raindrops 3) "plang"))
             (expect (= (raindrops 5) "plong"))
             (expect (= (raindrops 17) "tshäng")))

  (expecting "non-base cases (generative)"
               ;; for all integers from 1 to +infinite, except 2,3,5,and 17...
             (expect (mi/check raindrops)
                     (= (raindrops 1) "blob"))))


(comment
  (mi/collect!)
  (mi/instrument!)
  (mi/check)
  (mg/check schema f)
  ,)
