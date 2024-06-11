(ns allentiak.challenges.raindrops.cli
  (:gen-class)
  (:require
   [allentiak.challenges.raindrops.core :refer [raindrops]]
   [allentiak.challenges.raindrops.sequence :refer [monotone-seq]]
   [clojure.pprint :as pprint]
   [clojure.string :as str]
   [clojure.tools.cli :as cli]))

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
