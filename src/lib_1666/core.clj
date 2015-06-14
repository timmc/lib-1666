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
  "From a partial and a nick produce a score."
  [part nick]
  ;; FIXME Use proper Unicode-aware case-folding, if not already
  (let [cf-part (.toLowerCase part)
        cf-nick (.toLowerCase nick)]
    (cond (.startsWith nick part)            7 ;; pre?
          (.startsWith cf-nick cf-part)      6 ;; cipre?
          (.contains nick part)              5 ;; inf?
          (.contains cf-nick cf-part)        4 ;; ciinf?
          (contains-subseq? nick part)       3 ;; sub?
          (contains-subseq? cf-nick cf-part) 2 ;; cisub?
          :else                              1)))

(defn rank-completions
  "Given a partial nick and a coll of usernames, yield sorted
  usernames by match relevancy (best first)."
  [part nicks]
  ;; reverse sort
  (sort-by (partial score-match part) > nicks))
