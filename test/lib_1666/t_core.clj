(ns lib-1666.t-core
  (:require [lib-1666.core :as c]
            [clojure.test :refer :all]))

(deftest subseqs
  (are [n p res] (let [[f c s :as actual] (c/find-subseq n p)]
                   (if f
                     (= actual res)
                     (= (first actual) (first res))))
       ;; trivial
       "" "" [true true true]
       "name" "" [true true true]
       "" "cruft" [false nil nil]
       "name" "name" [true true true]
       ;; extra
       "name" "namex" [false nil nil]
       ;; standard
       "name" "nm" [true false true]
       "name" "e" [true true false]
       ;; off the end (potential index bug)
       "name" "es" [false nil nil]
       ;; order matters
       "name" "eman" [false nil nil]
       ;; count matters
       "name" "nnm" [false nil nil]))

(deftest scoring
  (are [n p r] (= r (c/score-match p n))
       "" "" 7
       "TimMc" "" 7
       "TimMc" "tm" 2
       "TimMc" "TM" 3
       "TimMc" "tim" 6
       "TimMc" "Tim" 7))

(deftest matching
  (let [subject c/rank-completions
        users ["TimMc" "chromakode" "Max" "mac" "intortus" "logan"]]
    (testing "Case, prefix, infix, and stable sort"
      (is (= (subject "M" users)
             ["Max" "mac" "TimMc" "chromakode" "intortus" "logan"])))
    (testing "subseqs less than infix ci, and stable"
      (is (= (subject "mc" users)
             ["TimMc" "mac" "chromakode" "Max" "intortus" "logan"])))))
