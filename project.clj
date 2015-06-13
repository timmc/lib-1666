(defproject lib-1666 "0.1.0-SNAPSHOT"
  :description "PoC for heim nick autocompletion."
  :url "https://github.com/timmc/lib-1666"
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :plugins [[quickie "0.3.11"]]
  :test-matcher #"lib-1666(\.|\..*\.)t-.*" ;; for quickie
  :repl-options {:init-ns lib-1666.core})
