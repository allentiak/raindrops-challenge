(ns allentiak.challenges.raindrops.core
  (:gen-class)
  (:require
   [clojure.string :as str]))

(defn- times-divisible
  "Given two integers, 'n' and 'divisor',
  return how many times is 'n' divisible by 'divisor'."
  [n divisor]
  (loop [cnt 0
         num n]
    (if-not (zero? (mod num divisor))
      cnt
      (recur (inc cnt) (/ num divisor)))))

(defn- divisor-pairs
  "Given an integer n and a list of divisors,
  return pairs [divisor times-divisible]
  for each divisor by which n is divisible."
  [n divisors]
  (->>
    divisors
    (map (partial times-divisible n))
    (zipmap divisors)
    (filter #(pos? (val %)))))

(comment
  (divisor-pairs 54 [2 3 5 17])
  ;; => ([2 1] [3 3])
  :end)

(defn- int->base-sound
  "Given an integer,
  return its predefined sound as per the problem definition."
  [n]
  (condp = n
    2 "pling"
    3 "plang"
    5 "plong"
    17 "tshäng"
    "blob"))

(defn- divisor-pair->output
  "Given a division pair [divisor times-divisible],
  return the corresponding output string."
  [[divisor times-divisible]]
  (let [base-sound (int->base-sound divisor)]
    (loop [t times-divisible
           output []]
      (condp = t
        1 (str/join ", " (reverse (conj output base-sound)))
        2 (str/join ", " (reverse (conj output (str/upper-case base-sound))))
        3 (str/join ", " (reverse (conj output (str "*" (str/upper-case base-sound) "*"))))
        4 (str/join ", " (reverse (conj output (str/join ", " (list (str "*" (str/upper-case base-sound) "*") base-sound)))))
        (recur (- t 4) (conj output (str/join ", " (list (str "*" (str/upper-case base-sound) "*") base-sound))))))))

(defn raindrops
  "Given an integer,
  return the corresponding raindrop sound."
  [n]
  (let [divisors [2 3 5 17]
        output   (->>
                   divisors
                   (divisor-pairs n)
                   (map divisor-pair->output)
                   (str/join ", "))]
    (if (empty? output) "blob" output)))
