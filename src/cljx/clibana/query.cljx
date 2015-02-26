(ns clibana.query
  (:require [clibana.internal.date :as cid]))

(defn with-string [field term] {:query (str field " : \"" term "\"")})

(defn with-strings [field terms] {:query (str field " : (" (reduce str (interpose " " (map #(str "\"" % "\"") terms))) ")")})

(defn with-term [field term] {:query (str field " : " term)})

(defn with-terms [field terms] {:query (str field " : (" (reduce str (interpose " " terms)) ")")})

(def inexfrom {:inclusive "[" :exclusive "{"})
(def inexto {:inclusive "]" :exclusive "}"})
(defn with-number-range [field from-kw from to-kw to]
  (if (and (number? from) (number? to))
    {:query (str field " : "
                 (get inexfrom from-kw "[")
                 (min from to) " TO " (max from to)
                 (get inexto to-kw "]"))}
    nil))

(defn with-date-range
  "Warning! This function might be unrelizable, mainly in ClojureScript version.
   On inputs to this function, make yourself very sure, that string dates passed in
  are nice dates in 'RFC3339' format (2012-12-12T12:12:12+00:00)"
  [field from-kw from to-kw to]
  (if (and (cid/is-date? from) (cid/is-date? to))
    {:query (str field " : "
                 (get inexfrom from-kw "[")
                 "\"" from "\" TO \"" to "\""
                 (get inexto to-kw "]"))}
    nil))

(defn with-and [query1 query2 & queries]
  {:query (apply str (interpose " AND " (into [query1 query2] queries)))})

(defn with-or [query1 query2 & queries]
  {:query (apply str (interpose " OR " (into [query1 query2] queries)))})