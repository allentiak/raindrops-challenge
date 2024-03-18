(ns allentiak.challenges.raindrops.core
  (:gen-class))

(defn raindrops
  [n]
  (cond
    (= (mod n 2) 0) "pling"
    (= (mod n 3) 0) "plang"
    (= (mod n 5) 0) "plong"
    (= (mod n 17) 0) "tshäng"
    :else "blob"))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Howdy! Invocation args are: " args))
