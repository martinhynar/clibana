(ns clibana.index
  #+clj (:require [clojure.data.json :as json])
  #+cljs (:require [goog.json :as json])
  )


(defn index-pattern
  ([pattern]
   {:title         pattern
    :timeFieldName "@timestamp"
    :customFormats
    #+clj (json/write-str {})
    #+cljs (json/serialize (clj->js {}))
    })
  ([pattern time-field]
   {:title         pattern
    :timeFieldName time-field
    :customFormats
    #+clj (json/write-str {})
    #+cljs (json/serialize (clj->js {}))
    })
  )


(comment
  (index-pattern "pattern" "timefield")
  )