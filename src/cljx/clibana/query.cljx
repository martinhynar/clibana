(ns clibana.query
  (:require [clibana.internal.date :as cid]
            [clibana.internal.query-int :as ciq]))

(defn with-strings
  ([field string]
   {:query (str field " : \"" string "\"")})

  ([field string & strings]
   {:query (str field " : (" (reduce str (interpose ", " (map #(str "\"" % "\"") (conj strings string)))) ")")})
  )

(defn with-terms
  ([field term] {:query (str field " : " term)})
  ([field term & terms] {:query (str field " : (" (reduce str (interpose ", " (conj terms term))) ")")})
  )

(defn with-number-range [field from-kw from to-kw to]
  (if (and (number? from) (number? to))
    {:query (str field " : "
                 (get ciq/inexfrom from-kw "[")
                 (min from to) " TO " (max from to)
                 (get ciq/inexto to-kw "]"))}
    nil))

(defn with-date-range
  "Warning! This function might be unrelizable, mainly in ClojureScript version.
   On inputs to this function, make yourself very sure, that string dates passed in
  are nice dates in 'RFC3339' format (2012-12-12T12:12:12+00:00)"
  [field from-kw from to-kw to]
  (if (and (cid/is-date? from) (cid/is-date? to))
    {:query (str field " : "
                 (get ciq/inexfrom from-kw "[")
                 "\"" from "\" TO \"" to "\""
                 (get ciq/inexto to-kw "]"))}
    nil))

(defn with-and
  "Constructs queries of the form q1 AND q2 ..."
  [query1 query2 & queries]
  {:query (apply str (interpose " AND " (into [(:query query1) (:query query2)] (map :query queries))))})

(defn with-or
  "Constructs queries of the form q1 OR q2 ..."
  [query1 query2 & queries]
  {:query (apply str (interpose " OR " (into [(:query query1) (:query query2)] (map :query queries))))})