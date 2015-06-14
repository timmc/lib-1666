(ns lib-1666.t-core
  (:require [lib-1666.core :as c]
            [clojure.test :refer :all]))

(deftest subseqs
  (are [n p r] (= r (c/contains-subseq? n p))
       ;; trivial
       "" "" true
       "name" "" true
       "" "cruft" false
       "name" "name" true
       ;; extra
       "name" "namex" false
       ;; standard
       "name" "nm" true
       "name" "e" true
       ;; off the end (potential index bug)
       "name" "es" false
       ;; order matters
       "name" "eman" false
       ;; count matters
       "name" "nnm" false))

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
