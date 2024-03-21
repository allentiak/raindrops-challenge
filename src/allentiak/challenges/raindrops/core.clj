(ns allentiak.challenges.raindrops.core
  (:gen-class))

(def ^:private special-cases
  {:2 "pling"
   :3 "plang"
   :5 "plong"
   :17 "tshäng"})

(defn- divisible?
  "given an integer and an integer divisor, returns whether the number is divisible by the divisor"
  [n divisor]
  (zero? (mod n divisor)))

(defn- divisible-by-many?
  "given an integer and a vector of integer divisors, returns a vector with the result of applying 'divisible?' to the integer and each of the elements of the vector"
  [n divisors]
  (let [ns (take (count divisors) (repeat n))]
    (map divisible? ns divisors)))

(comment
  (take 3 (repeat 2))
  ;; => (2 2 2)
  (divisible-by-many? 119 [2 3 5 17])
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
