(ns allentiak.challenges.raindrops.core
  (:gen-class))

(defn base-output
  [n]
  (condp = n
    2 "pling"
    3 "plang"
    5 "plong"
    17 "tshäng"
    "blob"))

