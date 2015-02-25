(ns clibana.search
  (:require
    [clibana.internal.common :as c]
    #+clj [clojure.data.json :as json]
    #+cljs [goog.json :as json]
    )
  )

;; DESCRIPTION
(defn with-description
  "Give your search some human readable description."
  [description] (c/with-description description))

;;; COLUMNS
(defn with-columns [col & cols] {:columns (apply vector col cols)})
(defn columns? [decorations] (c/take-first :columns decorations))

;;; SORT
(def sorts {:asc "asc" :desc "desc"})
(defn with-sort [col order] {:sort [col (get sorts order "asc")]})
(defn sort? [decorations] (c/take-first :sort decorations))


(defn search? [decorations] (first (filter :search decorations)))


(defn search
  "Construct document describing saved search"
  [title & decorations]
  (let [description (c/<-description decorations)
        options (c/<-options decorations)
        columns (columns? decorations)
        sort (sort? decorations)
        search (:search (search? decorations))
        encode-json? (get options :encode-json? true)]
    {:title                 title
     :description           description
     :columns               (or columns ["_source"])
     :sort                  (or sort [(first columns) "asc"])
     :kibanaSavedObjectMeta {:searchSourceJSON (if encode-json?
                                                 #+clj (json/write-str search)
                                                 #+cljs (json/serialize (clj->js search))
                                                 search)}
     }))

(comment
  (defn example []
    (search "Some saved search"
            (with-description "Some custom saved search")
            (with-columns "_type" "_id")
            (with-sort "_type" :asc)
            )))

; "{\"index\":\"build-999999\",\"highlight\":{\"pre_tags\":[\"@kibana-highlighted-field@\"],\"post_tags\":[\"@/kibana-highlighted-field@\"],\"fields\":{\"*\":{}}},\"filter\":[],\"query\":{\"query_string\":{\"query\":\"*\",\"analyze_wildcard\":true}}}"