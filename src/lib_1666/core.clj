(ns lib-1666.core
  (:require [org.timmc.handy :as h]))

(defn find-subseq
  "Given a nick and a partial, yield a vector of:

- Whether a subseq was found (that is, nick contains the characters in
  the partial nick, in order)
- Whether it was contiguous
- Whether it started at the beginning

If the first element is false, the other two are not defined."
  [nick part]
  (loop [offset 0 ;; where we start scanning from next
         remain part
         previous nil ;; location of previous find, or nil if just starting
         contiguous true
         start-zero nil]
    (if (empty? remain)
      [true contiguous (if previous start-zero true)]
      (let [nexdex (.indexOf nick (str (first remain)) offset)]
        (if (neg? nexdex)
          [false nil nil]
          (recur (inc nexdex)
                 (rest remain)
                 nexdex
                 (and contiguous (or (not previous)
                                     (= 1 (- nexdex previous))))
                 (or start-zero (zero? nexdex))))))))

(defn score-match
  "From a partial and a nick produce a score."
  [part nick]
  ;; FIXME Use proper Unicode-aware case-folding, if not already
  (let [cf-part (.toLowerCase part)
        cf-nick (.toLowerCase nick)
        [found contig start] (find-subseq nick part)
        [found-ci contig-ci start-ci] (find-subseq cf-nick cf-part)]
    (cond found (if contig
                  (if start 7 5)
                  3)
          found-ci (if contig-ci
                     (if start-ci 6 4)
                     2)
          :else 1)))

(defn rank-completions
  "Given a partial nick and a coll of usernames, yield sorted
  usernames by match relevancy (best first)."
  [part nicks]
  ;; reverse sort
  (sort-by (partial score-match part) > nicks))
