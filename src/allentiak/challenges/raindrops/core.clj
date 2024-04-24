(ns allentiak.challenges.raindrops.core
  (:gen-class))

(defn int->base-sound
  "given an integer,
  return its predefined sound as per the problem definition"
  [n]
  (condp = n
    2 "pling"
    3 "plang"
    5 "plong"
    17 "tshäng"
    "blob"))

(defn times-divisible
  "given two integers, 'n' and 'divisor',
  return how many times is 'n' divisible by 'divisor'"
  [n divisor]
  (loop [cnt 0
         num n]
    (if-not (zero? (mod num divisor))
      cnt
      (recur (inc cnt) (/ num divisor)))))
