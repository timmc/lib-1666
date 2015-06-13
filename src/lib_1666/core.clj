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
        cf-nick (.toLowerCase nick)
        ;; Test matrix:
        ;;
        ;; pre > cipre
        ;;  v      v
        ;; inf > ciinf
        ;;  v      v
        ;; sub > cisub
        ;;
        ;; Higher row implies lower; left column implies right column.
        pre? (.startsWith nick part)
        cipre? (or pre? (.startsWith cf-nick cf-part))
        inf? (or pre? (.contains nick part))
        ciinf? (or inf? cipre? (.contains cf-nick cf-part))
        sub? (or inf? (contains-subseq? nick part))
        cisub? (or sub? ciinf? (contains-subseq? cf-nick cf-part))]
    [pre? cipre? inf? ciinf? sub? cisub?]))

(defn rank-completions
  "Given a partial nick and a coll of usernames, yield sorted
  usernames by match relevancy (best first)."
  [part nicks]
  (sort-by (partial score-match part) (comp - h/lexicomp) nicks))
