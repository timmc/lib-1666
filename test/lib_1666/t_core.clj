(ns lib-1666.t-core
  (:require [lib-1666.core :as c]
            [clojure.test :refer :all]))

(deftest subseqs
  (are [n p r] (= (c/contains-subseq? n p) r)
       "" "" true
       "name" "" true
       "" "cruft" false
       "name" "name" true
       "name" "namex" false
       "name" "nm" true
       "name" "e" true
       "name" "es" false))
