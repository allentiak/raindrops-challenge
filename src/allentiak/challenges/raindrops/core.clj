(ns allentiak.challenges.raindrops.core
  (:gen-class))

(defn raindrops
  [n]
  (condp #(zero? (mod %2 %1)) n
    2 "pling"
    3 "plang"
    5 "plong"
    17 "tshäng"
    "blob"))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Howdy! Invocation args are: " args))
