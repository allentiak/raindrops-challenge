(ns allentiak.challenges.raindrops.core
  (:gen-class)
  (:require
   [clojure.string :as str]))

(defn- times-divisible
  "given two integers, 'n' and 'divisor',
  return how many times is 'n' divisible by 'divisor'"
  [n divisor]
  (loop [cnt 0
         num n]
    (if-not (zero? (mod num divisor))
      cnt
      (recur (inc cnt) (/ num divisor)))))

(defn- divisor-pairs
  "given an integer and a list of divisors,
  return divisor, times-divisible pairs"
  [n divisors]
  (->>
    divisors
    (map (partial times-divisible n))
    (zipmap divisors)
    (filter #(pos? (val %)))))

(divisor-pairs 54 [2 3 5 17])
;; => ([2 1] [3 3])

(defn- int->base-sound
  "given an integer,
  return its predefined sound as per the problem definition"
  [n]
  (condp = n
    2 "pling"
    3 "plang"
    5 "plong"
    17 "tshäng"
    "blob"))

(defn- divisor-pair->output
  [[divisor times]]
  (let [base-sound (int->base-sound divisor)]
    (condp = times
      1 base-sound
      2 (str/upper-case base-sound)
      3 (str "*" (str/upper-case base-sound) "*")
      4 (str/join ", " (list (str "*" (str/upper-case base-sound) "*") base-sound))
     "we only have 4 transformations defined so far")))

(defn raindrops
  [n]
  (let [divisors [2 3 5 17]
        output   (->>
                   divisors
                   (divisor-pairs n)
                   (map divisor-pair->output)
                   (str/join ", "))]
    (if (empty? output) "blob" output)))
