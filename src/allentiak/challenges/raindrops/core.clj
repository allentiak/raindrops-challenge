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

(defn- add-divisible-fields
  "given an integer and a special case map, return an augmented map with the new keys: ':divisible' with the result of applying 'divisible?' to the inteeger and the special case, and ':times-divisible' with how many times it is divisible"
  [n special-case]
  (let [d? (divisible? n (:divisor special-case))
        case-with-divisible (assoc special-case :divisible d?)
        how-many-times (if d?
                         (/ n (:divisor special-case))
                         0)]
      (assoc case-with-divisible :times-divisible how-many-times)))

(comment
  (add-divisible-fields 4 (first special-cases))
;; => {:divisor 17, :output "tshäng", :divisible false, :times-divisible 0}
  (add-divisible-fields 4 (second special-cases))
;; => {:divisor 2, :output "pling", :divisible true, :times-divisible 2}
  ,)

(defn- augment-special-cases
  "given an integer and a seq of SpecialCase's, returns a seq augmenting each SpecialCase with a new key ':divisible' with the result of applying 'divisible?' to the integer and the divisor"
  [n special-cases]
  (let [ns (take (count special-cases) (repeat n))]
    (map add-divisible-fields ns special-cases)))

(comment
  (first special-cases)
  ;; => {:divisor 17, :output "tshäng"}
  (augment-special-cases 17 special-cases)
  ;; => ({:divisor 17, :output "tshäng", :divisible true, :times-divisible 1} {:divisor 2, :output "pling", :divisible false, :times-divisible 0} {:divisor 3, :output "plang", :divisible false, :times-divisible 0} {:divisor 5, :output "plong", :divisible false, :times-divisible 0})
  ,)

(defn- divisible-cases
  "given a positive integer and an seq of SpecialCase's, return only the SpecialCases to which the integer is divisible by"
  [n special-cases]
  (let [augmented-special-cases (augment-special-cases n special-cases)]
    (filter :divisible augmented-special-cases)))

(comment
  (divisible-cases 4 special-cases)
;; => ({:divisor 2, :output "pling", :divisible true, :times-divisible 2})
  (divisible-cases 6 special-cases)
;; => ({:divisor 2, :output "pling", :divisible true, :times-divisible 3} {:divisor 3, :output "plang", :divisible true, :times-divisible 2})
  :end)

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
