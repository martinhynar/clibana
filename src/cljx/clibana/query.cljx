(ns clibana.query
  (:require
    [clibana.internal.common :as c]))



(defn with-term [field term] {:query (str field " : " term)})
(defn with-string [field term] {:query (str field " : \"" term "\"")})

(defn with-terms [field terms] {:query (str field " : (" (reduce str (interpose " " terms)) ")")})
(defn with-strings [field terms] {:query (str field " : (" (reduce str (interpose " " (map #(str "\"" % "\"") terms))) ")")})


(defn with-and [query1 query2 & queries]
  {:query (apply str (interpose " AND " (into [query1 query2] queries)))})