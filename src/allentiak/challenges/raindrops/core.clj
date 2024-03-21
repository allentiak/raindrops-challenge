(ns allentiak.challenges.raindrops.core
  (:gen-class))

(defrecord SpecialCase [divisor output])

(def special-cases
  #{{:divisor 2 :output "pling"}
    {:divisor 3 :output "plang"}
    {:divisor 5 :output "plong"}
    {:divisor 17 :output "tshäng"}})

(comment
  (map :divisor special-cases)
  ;; => (17 2 3 5)
  ,)

(defn- divisible?
  "given an integer and an integer divisor, returns whether the number is divisible by the divisor"
  [n divisor]
  (zero? (mod n divisor)))

(defn- divisible-by-special-case?
  "given an integer and a special case, return a new map with a new ':divisible' key with the result of applying 'divisible?' to the inteeger and the special case"
  [n special-case]
  (assoc special-case :divisible (divisible? n (:divisor special-case))))

(comment
  (divisible-by-special-case? 4 (first special-cases))
  ;; => {:divisor 17, :output "tshäng", :divisible false}
  (divisible-by-special-case? 4 (second special-cases))
  ;; => {:divisor 2, :output "pling", :divisible true}
  ,)

(defn- divisible-by-many?
  "given an integer and a seq of SpecialCase's, returns a seq augmenting each SpecialCase with a new key ':divisible' with the result of applying 'divisible?' to the integer and the divisor"
  [n divisors-set]
  (let [ns (take (count divisors-set) (repeat n))]
    (map divisible-by-special-case? ns divisors-set)))

(comment
  (assoc (first special-cases) :divisible "whatever")
  ;; => {:divisor 17, :output "tshäng", :divisible "whatever"}
  (let [n 17
        my-case (first special-cases)]
    (assoc my-case :divisible (divisible? n (:divisor my-case))))
  ;; => {:divisor 17, :output "tshäng", :divisible true}
  (divisible-by-many? 4 special-cases)
  ;; => ({:divisor 17, :output "tshäng", :divisible false} {:divisor 2, :output "pling", :divisible true} {:divisor 3, :output "plang", :divisible false} {:divisor 5, :output "plong", :divisible false})
  ,)


(comment
  (take 3 (repeat 2))
  ;; => (2 2 2)
  (divisible-by-many? 119 special-cases)
  ;; => (false false false true)
  ,)


(comment
  (:2 special-cases))
  ;; => "pling"

(def ^:private canned-response
  "blob")

(defn raindrops
  "given a natural integer, produce a specific sound for special cases, and 'blob' for the rest"
  ([n]
   (raindrops n special-cases canned-response))
  ([n special-cases]
   (raindrops n special-cases canned-response))
  ([n special-cases canned-response]
   (let [k (keyword (str n))]
     (if-let [val (k special-cases)]
       val
       canned-response))))

(comment
  (raindrops 2)
  ;; => "pling"
  (raindrops 4)
  (keyword (str 2))
  ((keyword (str 2)) special-cases))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Howdy! Invocation args are: " args))
