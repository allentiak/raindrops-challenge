(ns allentiak.challenges.raindrops.sequence)

(defn monotone-seq
  "Returns an infinite pseudo-random monotone integer sequence,
     starting at n."
  [n]
  (iterate #(max % (rand-int (* % (inc (rand 1))))) n))

(comment
  (take 45 (monotone-seq 22))
  ;; => (22 41 41 41 61 61 61 88 88 88 88 88 88 88 144 144 225 225 225 337 534 534 534 534 534 534 534 680 903 903 1012 1012 1098 1098 1098 1098 1389 1389 1389 1389 1389 1801 1801 1801 1801)
  :end)
