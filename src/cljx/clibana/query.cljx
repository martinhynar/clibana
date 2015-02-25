(ns clibana.query
  (:require
    [clibana.internal.common :as c]))


(defn query? [decorations] (first (filter :query decorations)))


(defn with-term [field term] {:query (str field " : " term)})
(defn with-string [field term] {:query (str field " : \"" term "\"")})

(defn with-terms [field terms] {:query (str field " : (" (reduce str (interpose " " terms)) ")")})
(defn with-strings [field terms] {:query (str field " : (" (reduce str (interpose " " (map #(str "\"" % "\"") terms))) ")")})


(defn query "Construct a search query"
  [index & decorations]
  (let [options (c/<-options decorations)
        query (:query (query? decorations))
        ]
    {:search {:index  index
              :filter []
              :query  query
              }}))



