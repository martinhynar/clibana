(ns clibana.index
  #+clj (:require [clojure.data.json :as json]
                  [clibana.internal.common :as cic]
                  [clibana.internal.index :as cii])
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
  ([pattern time-field & decorations]
   (let [options (cic/<-options decorations)
         encode-json? (get options :encode-json? true)
         fields (map :field (filter #(contains? %1 :field) decorations))]
     {:title         pattern
      :timeFieldName time-field
      :fields        (if encode-json?
                       #+clj (json/write-str fields)
                       #+cljs (json/serialize (clj->js fields))
                       fields)
      :customFormats (if encode-json?
                       #+clj (json/write-str {})
                       #+cljs (json/serialize (clj->js {}))
                       fields)
      }))
  )
(let [field-definition {:name       "name"
                        :type       :_type
                        :scripted   false
                        :indexed    false
                        :analyzed   false
                        :doc_values true}]
  (defn with-string-field [field-name]
    {:field (into field-definition {:name field-name :type "string"})}
    )
  (defn with-number-field [field-name]
    {:field (into field-definition {:name field-name :type "number"})}
    )
  (defn with-date-field [field-name]
    {:field (into field-definition {:name field-name :type "date"})}
    )
  (defn with-boolean-field [field-name]
    {:field (into field-definition {:name field-name :type "boolean"})}
    )

  )