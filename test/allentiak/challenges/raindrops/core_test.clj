(ns allentiak.challenges.raindrops.core-test
  (:require
   [allentiak.challenges.raindrops.core :as sut]
   [clojure.test :refer [use-fixtures]]
   [expectations.clojure.test :refer [defexpect expect expecting]]
   [malli.core :as m]
   [malli.generator :as mg]
   [malli.instrument :as mi]
   [clojure.test.check.generators :as tsgen]
   [clojure.test.check :as tc]))

(defn with-collect!
  [test-fn]
  (mi/collect!)
  (mi/instrument!)
  (test-fn))

(def valid-strings
  (m/schema
   [:enum "pling" "plang" "plong" "tshäng" "blob"]))

;; TODO: augmented-strings

(def =>raindrops
  (m/schema
   [:=> [:cat pos-int?] valid-strings]
   {::m/function-checker mg/function-checker}))

(defn raindrops
  [n]
  (condp #(zero? (mod %2 %1)) n
    2 "pling"
    3 "plang"
    5 "plong"
    17 "tshäng"
    "blob"))

(def base-cases
  (m/schema
   [:enum 2 3 5 17]))

(def not-3
  (m/schema
   [:and pos-int?
    [:not
     base-cases]]))

(def =>base-raindrops
  (m/schema
   [:=> [:cat not-3] [:enum "blob"]]
   {::m/function-checker mg/function-checker}))

(comment
  (mg/generate not-3)
  ;; => 1890
  ;; => 134158414
  ;; => 253
  ;; => 239032858
  (m/validate =>raindrops raindrops)
  ;; => true
  (m/validate (m/schema
               [:enum "abc"]) "abc")
  (m/validate =>base-raindrops raindrops)
  (println-str (m/explain =>base-raindrops raindrops))
  ;; => "{:schema [:=> [:cat [:and pos-int? [:not [:enum 2 3 5 17]]]] [:enum blob]], :value #function[allentiak.challenges.raindrops.core-test/raindrops], :errors ({:path [], :in [], :schema [:=> [:cat [:and pos-int? [:not [:enum 2 3 5 17]]]] [:enum blob]], :value #function[allentiak.challenges.raindrops.core-test/raindrops], :check {:total-nodes-visited 1, :depth 0, :pass? false, :result false, :result-data nil, :time-shrinking-ms 1, :smallest [(4)], :malli.generator/explain-output {:schema [:enum blob], :value pling, :errors ({:path [], :in [], :schema [:enum blob], :value pling})}}})}\n"
  (format
   (with-out-str
     (clojure.pprint/pprint (m/explain =>base-raindrops raindrops)))))
;; => "{:schema\n [:=> [:cat [:and pos-int? [:not [:enum 2 3 5 17]]]] [:enum \"blob\"]],\n :value #function[allentiak.challenges.raindrops.core-test/raindrops],\n :errors\n ({:path [],\n   :in [],\n   :schema\n   [:=> [:cat [:and pos-int? [:not [:enum 2 3 5 17]]]] [:enum \"blob\"]],\n   :value\n   #function[allentiak.challenges.raindrops.core-test/raindrops],\n   :check\n   {:total-nodes-visited 1,\n    :depth 0,\n    :pass? false,\n    :result false,\n    :result-data nil,\n    :time-shrinking-ms 0,\n    :smallest [(4)],\n    :malli.generator/explain-output\n    {:schema [:enum \"blob\"],\n     :value \"pling\",\n     :errors\n     ({:path [], :in [], :schema [:enum \"blob\"], :value \"pling\"})}}})}\n"


(comment

  (defn simple-fizz
    [n]
    (condp = n
      3 "fizz"
      n))

  (simple-fizz 2)
  ;; => 2

  (simple-fizz 3)
  ;; => "fizz"

  (require '[clojure.test.check :as tc])
  (require '[clojure.test.check.generators :as tcgen])
  (require '[clojure.test.check.properties :as prop])

  (def when-not-3-returns-n-prop
    (prop/for-all [n (tcgen/vector (tcgen/such-that #(not= % 3) tcgen/nat))]
                  (= (simple-fizz n) n)))

  (tc/quick-check 10000 when-not-3-returns-n-prop)
;; => {:result true, :pass? true, :num-tests 10000, :time-elapsed-ms 598, :seed 1710860400299}

  (require '[malli.core :as m])
  (require '[malli.generator :as mg])

  (def not-3
    (m/schema
     [:and pos-int?
      [:not
       [:enum 3]]]))

  (def =>simple-fizz
    (m/schema
     [:=> [:cat not-3] [:enum "blob"]]
     {::m/function-checker mg/function-checker}))

  (m/validate =>simple-fizz simple-fizz)
  ;; => false

  (simple-fizz 1)
;; => 1

  (m/check)

  (clojure.pprint/pprint
   (m/explain =>simple-fizz simple-fizz))

  (with-out-str
    (clojure.pprint/pprint
     (m/explain =>simple-fizz simple-fizz)))
  (m/validate not-3 simple-fizz)
  ;; => false
  (m/explain not-3 simple-fizz))

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
  #_(mi/collect!)
  (mi/instrument!)
  (mi/check)
  #_(mg/check schema f))
