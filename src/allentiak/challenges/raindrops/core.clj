(ns allentiak.challenges.raindrops.core
  (:gen-class)
  (:require [clojure.string :as str]))

(def ^:private problem
  {:special-cases
   #{{:divisor 2 :base-output "pling"}
     {:divisor 3 :base-output "plang"}
     {:divisor 5 :base-output "plong"}
     {:divisor 17 :base-output "tshäng"}}
   :default-output-fn
   (constantly "blob")
   :transformation-fns
   [`first-transformation
    `second-transformation
    `third-transformation]})

(def ^:private special-cases
  (:special-cases problem))

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
  ;; => {:divisor 17, :base-output "tshäng", :divisible false, :times-divisible 0}
  (add-divisible-fields 4 (second special-cases))
  ;; => {:divisor 2, :base-output "pling", :divisible true, :times-divisible 2}
  :end)

(defn- augment-special-cases
  "given an integer and a seq of SpecialCase's, returns a seq augmenting each SpecialCase with a new key ':divisible' with the result of applying 'divisible?' to the integer and the divisor, ordered by ':divisor'"
  [n special-cases]
  (let [ns (take (count special-cases) (repeat n))]
    (sort-by :divisor (map add-divisible-fields ns special-cases))))

(comment
  (augment-special-cases 17 special-cases)
  ;; => ({:divisor 2, :base-output "pling", :divisible false, :times-divisible 0} {:divisor 3, :base-output "plang", :divisible false, :times-divisible 0} {:divisor 5, :base-output "plong", :divisible false, :times-divisible 0} {:divisor 17, :base-output "tshäng", :divisible true, :times-divisible 1})
  :end)

(defn- divisible-cases
  "given a positive integer and an seq of SpecialCase's, return a seq of Answers, consisting in only the fields ':divisor', ':base-output', and ':times-divisible' of the SpecialCases to which the integer is divisible by"
  [n special-cases]
  (let [augmented-special-cases (augment-special-cases n special-cases)]
    (map #(dissoc % :divisible) (filter :divisible augmented-special-cases))))

(comment
  (divisible-cases 4 special-cases)
  ;; => ({:divisor 2, :base-output "pling", :times-divisible 2})
  (divisible-cases 6 special-cases)
  ;; => ({:divisor 2, :base-output "pling", :times-divisible 3} {:divisor 3, :base-output "plang", :times-divisible 2})
  :end)

(defn- first-transformation
  [s]
  (str/upper-case s))

(defn- second-transformation
  [s]
  (str "*" (first-transformation s) "*"))

(defn- third-transformation
  [s]
  (str/join " " (list (second-transformation s) s)))

(comment
  (:special-cases problem)
  ;; => #{{:divisor 17, :base-output "tshäng"} {:divisor 2, :base-output "pling"} {:divisor 3, :base-output "plang"} {:divisor 5, :base-output "plong"}}
  (map :base-output (:special-cases problem))
  ;; => ("tshäng" "pling" "plang" "plong")
  (def ^:private sounds (map :base-output (:special-cases problem)))
  sounds
  ;; => ("tshäng" "pling" "plang" "plong")

  (map first-transformation sounds)
  ;; => ("TSHÄNG" "PLING" "PLANG" "PLONG")

  (map second-transformation sounds)
  ;; => ("*TSHÄNG*" "*PLING*" "*PLANG*" "*PLONG*")

  (map third-transformation sounds)
;; => ("*TSHÄNG* tshäng" "*PLING* pling" "*PLANG* plang" "*PLONG* plong")

  #_(let
     [normal-output         #"^\p{Ll}+$"
      first-transformation  #"^\p{Lu}+$"
      second-transformation #"^\*\p{Lu}+\*$"])

  :end)

(defn- transform-answer
  "given an answer map with ':divisor', ':base-output', and ':times-divisible', return an output map.
  This map has the key ':output', which contains the list of outputs"
  [{:keys [base-output times-divisible] :as answer}]
  {:output
   (condp = times-divisible
     1 base-output
     2 (first-transformation base-output)
     3 (second-transformation base-output)
     4 (third-transformation base-output)
     (third-transformation base-output))})

(comment
  (divisible-cases 4 special-cases)
  ;; => ({:divisor 2, :base-output "pling", :times-divisible 2})
  (transform-answer (first (divisible-cases 2 special-cases)))
;; => {:output "pling"}
  (transform-answer (first (divisible-cases 4 special-cases)))
;; => {:output "PLING"}
  (transform-answer (first (divisible-cases 6 special-cases)))
;; => {:output "*PLING*"}
  (transform-answer (first (divisible-cases 8 special-cases)))
;; => {:output "*PLING* pling"}
  :end)

(defn raindrops
  "given a natural integer, produce a specific sound for special cases, and 'blob' for the rest"
  ([n]
   (raindrops n problem))
  ([n {:keys [special-cases default-output-fn] :as problem-map}]
   (if-let [answers (not-empty (divisible-cases n special-cases))]
     (let [transformed-answers (map transform-answer answers)
           outputs (map :output transformed-answers)]
       (str/join ", " outputs))
     (default-output-fn n))))

(comment
  (map transform-answer (not-empty (divisible-cases 10 special-cases)))
;; => ({:output ("*PLING*" "pling")} {:output "PLONG"})

  (->>
    (divisible-cases 10 special-cases)
    not-empty
    (map transform-answer)
    (map :output)
    (map #(str/join " " %))
    (str/join ", "))
;; => "*PLING* pling, P L O N G"

  (->>
    (map :output (map transform-answer (not-empty (divisible-cases 10 special-cases)))))
;; => (("*PLING*" "pling") "PLONG")

  ;; it can even mimic a fizzbuzz!
  (let [new-problem-map (assoc problem :default-output-fn identity)]
    (raindrops 1 new-problem-map))
  ;; => 1

  ;; or anything else...
  (let [new-problem-map (assoc problem :default-output-fn inc)]
    (raindrops 1 new-problem-map))
    ;; => 2

  (:default-output-fn problem)
  ;; => #function[clojure.core/constantly/fn--5740]
  ((constantly "blob"))
  ;; => "blob"
  (def m {:fn (constantly "abc")})
  ((:fn m))
  ;; => "abc"
  (raindrops 1)
  ;; => "blob"
  (raindrops 2)
  ;; => "pling"
  (raindrops 10)
  ;; => "*PLING*, pling, PLONG"
  (if () "y" "n")
  ;; => "y"
  (if (not-empty ()) "y" "n")
  ;; => "n"
  (not-empty (divisible-cases 510 special-cases))
  ;; => ({:divisor 2, :base-output "pling", :times-divisible 255} {:divisor 3, :base-output "plang", :times-divisible 170} {:divisor 5, :base-output "plong", :times-divisible 102} {:divisor 17, :base-output "tshäng", :times-divisible 30})
  (map :base-output (not-empty (divisible-cases 510 special-cases)))
  ;; => ("pling" "plang" "plong" "tshäng")
  :end)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Howdy! Invocation args are: " args))
