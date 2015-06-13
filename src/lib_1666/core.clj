(ns lib-1666.core
  (:require [org.timmc.handy :as h]))

(defn contains-subseq?
  "Determines if nick contains the characters in the partial nick, in
  order."
  [nick part]
  (loop [offset 0
         remain part]
    (if (empty? remain)
      true
      (let [nexdex (.indexOf nick (str (first remain)) offset)]
        (if (neg? nexdex)
          false
          (recur (inc nexdex) (rest remain)))))))

(defn score-match
  "From a partial and a nick produce a seq of scores."
  [part nick]
  ;; FIXME Use proper Unicode-aware case-folding, if not already
  (let [cf-part (.toLowerCase part)
        cf-nick (.toLowerCase nick)]
    (concat [(.startsWith nick part)
             (.startsWith cf-nick cf-part)
             (.contains nick part)
             (.contains cf-nick cf-part)]
            (lazy-seq
             [(contains-subseq? nick part)
              (contains-subseq? cf-nick cf-part)]))))

(defn rank-completions
  "Given a partial nick and a coll of usernames, yield sorted
  usernames by match relevancy (best first)."
  [part nicks]
  (sort-by (partial score-match part) (comp - h/lexicomp) nicks))
