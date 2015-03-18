(ns clibana.search
  (:require
    [clibana.internal.common :as cic]
    [clibana.internal.search-int :as cis]
    #+clj [clojure.data.json :as json]
    #+cljs [goog.json :as json]
    )
  )

;; DESCRIPTION
(defn with-description
  "Give your search some human readable description."
  [description] (cic/with-description description))

;;; COLUMNS
(defn with-columns [col & cols] {:columns (apply vector col cols)})

;;; SORT
(defn with-sort [col order] {:sort [col (get cis/sorts order "asc")]})


(defn search
  "Construct document describing saved search"
  [title & decorations]
  (let [description (cic/<-description decorations)
        options (cic/<-options decorations)
        columns (cic/take-first :columns decorations)
        sort (cic/take-first :sort decorations)
        search (cic/<-search decorations)
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
