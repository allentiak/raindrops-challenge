(ns allentiak.challenges.raindrops.core
  (:gen-class)
  (:require
   [clojure.pprint :as pprint]
   [clojure.string :as str]
   [clojure.tools.cli :as cli]))

(def ^:private problem
  {:special-cases
   (sort-by :divisor
            #{{:divisor 2 :base-output "pling"}
              {:divisor 3 :base-output "plang"}
              {:divisor 5 :base-output "plong"}
              {:divisor 17 :base-output "tshäng"}})
   :default-output-fn
   (constantly "blob")
   :transformation-fns
   `[default-response
     identity
     first-transformation
     second-transformation
     third-transformation]})

(defn default-response [n]
  "blob")

(comment
  (:special-cases problem)
;; => ({:divisor 2, :base-output "pling"} {:divisor 3, :base-output "plang"} {:divisor 5, :base-output "plong"} {:divisor 17, :base-output "tshäng"})
  :end)

(def ^:private special-cases
  (:special-cases problem))

(def ^:private transformation-fns
  (:transformation-fns problem))

(defn- divisible?
  "given an integer and an integer divisor, returns whether the number is divisible by the divisor"
  [n divisor]
  (zero? (mod n divisor)))

(defn- how-many-times [n divisor]
  (if-not (divisible? n divisor)
    0
    (loop [cnt 1
           num n]
      (if (divisible? (/ num divisor) divisor)
        (recur (inc cnt) (/ num divisor))
        cnt))))

(comment
  (map (partial how-many-times 128) [2 3 5 17])
  ;; => (7 0 0 0)
  (map (partial how-many-times 80) [2 3 5 17])
  ;; => (4 0 1 0)
  :end)

(defn- add-divisible-fields
  "given an integer and a special case map, return an augmented map with the new keys: ':divisible' with the result of applying 'divisible?' to the inteeger and the special case, and ':times-divisible' with how many times it is divisible"
  [n special-case]
  (let [d? (divisible? n (:divisor special-case))
        case-with-divisible (assoc special-case :divisible d?)]
    (assoc case-with-divisible :times-divisible (how-many-times n (:divisor special-case)))))

(comment
  (add-divisible-fields 4 (first special-cases))
  ;; => {:divisor 17, :base-output "tshäng", :divisible false, :times-divisible 0}
  (add-divisible-fields 4 (second special-cases))
  ;; => {:divisor 2, :base-output "pling", :divisible true, :times-divisible 2}
  (add-divisible-fields 8 (first special-cases))
;; => {:divisor 17, :base-output "tshäng", :divisible false, :times-divisible 0}
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
  (divisible-cases 128 special-cases)
;; => ({:divisor 2, :base-output "pling", :times-divisible 7})
  :end)

(defn- first-transformation
  [s]
  (str/upper-case s))

(defn- second-transformation
  [s]
  (str "*" (first-transformation s) "*"))

(defn- third-transformation
  [s]
  (str/join ", " (list (second-transformation s) s)))


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
  ;; => ("*TSHÄNG*, tshäng" "*PLING*, pling" "*PLANG*, plang" "*PLONG*, plong")

  :end)

(defn- transform-answer
  "given an answer map (with ':divisor', ':base-output', and ':times-divisible'), and a transformation fns vector, return an output map.
  This map has the key ':output', which contains the list of outputs."
  [{:keys [base-output times-divisible] :as answer} transformation-fns-vector]
  (loop [out []
         t times-divisible]
    (let [c (count transformation-fns-vector)
          last-fn (resolve (nth transformation-fns-vector (dec c)))
          f (resolve (nth transformation-fns-vector (mod t c)))]
        (if (< t c)
          {:output (str/join ", "(reverse (conj out (f base-output))))}
          (recur (conj out (last-fn base-output)) (- t (dec c)))))))


(comment
  (resolve (nth transformation-fns 3))
;; => #'allentiak.challenges.raindrops.core/second-transformation
  (resolve (nth transformation-fns 0))
  problem
  (mod 0 7)
  (nat-int? 0)
;; => true
  (pos-int? 0)
;; => false
  ((resolve (nth transformation-fns 4)) "sssss")
  (first (divisible-cases 2 special-cases))
  ;; => {:divisor 2, :base-output "pling", :times-divisible 1}

  (transform-answer (first (divisible-cases 2 special-cases)) transformation-fns)
  ;; => {:output "pling"}
  (transform-answer (first (divisible-cases 4 special-cases)) transformation-fns)
  ;; => {:output "PLING"}
  (transform-answer (first (divisible-cases 6 special-cases)) transformation-fns)
  ;; => {:output "pling"}
  (divisible? 8 2)
;; => true
  (how-many-times 8 2)
;; => 3
  (transform-answer (first (divisible-cases 8 special-cases)) transformation-fns)
  ;; => {:output "*PLING*"}
  (how-many-times 32 2)
;; => 5
  (how-many-times 16 2)
;; => 4
  (transform-answer (first (divisible-cases 16 special-cases)) transformation-fns)
;; => {:output "*PLING*, pling"}
  (transform-answer (first (divisible-cases 32 special-cases)) transformation-fns)
  (how-many-times 96 2)
  (/ 96 2)
  (/ 48 2)
  (/ 24 2)
  (/ 12 2)
  (/ 6 2)
  (transform-answer (first (divisible-cases 96 special-cases)) transformation-fns)
  (transform-answer (first (divisible-cases 128 special-cases)) transformation-fns)
;; => {:output "PLING, *PLING*, pling"}
  :end)

(defn raindrops
  "given a natural integer, produce a specific sound for special cases, and 'blob' for the rest"
  ([n]
   (raindrops n problem))
  ([n {:keys [special-cases default-output-fn] :as problem-map}]
   (if-let [answers (not-empty (divisible-cases n special-cases))]
     (let [transformed-answers (map transform-answer answers (take (count answers) (repeat transformation-fns)))
           outputs (map :output transformed-answers)]
       (str/join ", " outputs))
     (default-output-fn n))))

(comment
  ;; it can even mimic a fizzbuzz!
  (let [new-problem-map (assoc problem :default-output-fn identity)]
    (raindrops 1 new-problem-map))
  ;; => 1

  ;; or anything else...
  (let [new-problem-map (assoc problem :default-output-fn inc)]
    (raindrops 1 new-problem-map))
  ;; => 2

  (raindrops 1)
  ;; => "blob"

  (raindrops 2)
  ;; => "pling"

  (raindrops 10)
  ;; => "*PLING*, pling, PLONG"

  :end)

(defn- monotone-seq
  "returns an infinite pseudo-random monotone sequence starting at n"
  [n]
  (iterate
   #(max % (rand-int (* % (+ 1 (rand 1)))))
   n))

(comment
  (take 45 (monotone-seq 22))

  (take 200 (monotone-seq 3))
;; => (3 3 3 3 3 3 3 4 4 4 4 4 4 4 4 5 5 7 7 7 7 7 7 8 8 8 8 8 12 17 17 17 17 17 17 23 26 36 36 36 36 45 45 52 52 52 77 90 100 100 110 110 110 110 110 110 153 153 153 175 175 175 175 175 175 175 175 175 308 308 308 308 308 308 308 308 308 308 308 509 509 747 747 747 747 747 747 747 747 747 747 747 1246 1246 1246 1246 1246 1246 1246 1246 1276 1819 1819 2821 2821 2949 2949 2949 3700 3700 3700 3700 3700 4552 4552 4552 4552 4853 7031 7031 7031 7031 7031 10603 12311 12311 12311 12311 17047 17047 17047 17047 17047 17047 17252 17252 17252 17252 19866 23434 23434 23434 24565 24565 24565 26305 32263 39779 42541 42541 42541 42541 48261 58702 105571 137956 142735 142735 142735 142735 142735 142999 167595 167595 314565 314565 314565 447812 569482 1043716 1043716 1043716 1054499 1054499 1054499 1054499 1054499 1054499 1054499 1054499 1054499 1054499 1054499 1054499 1054499 1369831 1369831 1701288 1701288 1701288 1701288 2487792 2487792 2487792 2487792 2487792 2487792 2487792 2659534 2659534)

  :end)

(def cli-options
  [["-n" "--sample SAMPLE" "Sample size"
    :default 10
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % Integer/MAX_VALUE) "Must be a number between 0 and Integer/MAX_VALUE"]]

   ["-s" "--seed SEED" "Initial number"
    :default 1
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % Integer/MAX_VALUE) "Must be a number between 0 and Integer/MAX_VALUE"]]

   ["-h" "--help"]])

(defn usage [options-summary]
  (->> ["This is Leandro Doctors' solution to the raindrops coding challenge. There are many like it, but this one is his."
        ""
        "Usage: raindrops start [options]"
        ""
        "(Yeap: the program won't do anything useful unless you use the command \"start\" :)"
        ""
        "Options:"
        options-summary
        ""
        "Please refer to the README.md for more information."]
       (str/join \newline)))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (str/join \newline errors)))

(defn validate-args
  "Validate command line arguments. Either return a map indicating the program
  should exit (with an error message, and optional ok status), or a map
  indicating the action the program should take and the options provided."
  [args]
  (let [{:keys [arguments options errors summary]} (cli/parse-opts args cli-options)]
    (cond
      (:help options) ; help => exit OK with usage summary
      {:exit-message (usage summary) :ok? true}
      errors ; errors => exit with description of errors
      {:exit-message (error-msg errors)}
      ;; custom validation of arguments
      (and (= (count arguments) 1)
           (#{"start"} (first arguments)))
      {:options options}
      :else ; failed custom validation => exit with usage summary
      {:exit-message (usage summary)})))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn execute
  [{:keys [sample seed]}]
  (let [sequence (take sample (monotone-seq seed))]
    (println "Printing raining sequence...")
    (pprint/pprint
     (map raindrops sequence))))

(defn -main [& args]
  (let [{:keys [options exit-message ok?]} (validate-args args)]
    (if exit-message
      (exit (if ok? 0 1) exit-message)
      (execute options))))
