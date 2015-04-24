(ns clibana.internal.search-int
  (:require [clibana.internal.common :as cic]))

(def sorts
  "Map keyword to string and keep string as is."
  {:asc  "asc" :desc "desc"
   "asc" "asc" "desc" "desc"})


(defn construct-search
  "Use in-place created search."
  [& decorations]
  (let [query (cic/<-query decorations)]
    {:index     (cic/take-first :index decorations)
     :highlight {:pre_tags  ["@kibana-highlighted-field@"]
                 :post_tags ["@/kibana-highlighted-field@"]
                 :fields    {"*" {}}}
     :filter    (get decorations :filter [])
     :query     query
     }))


;"{\"index\":\"idx\",
;  \"highlight\":{\"pre_tags\":[\"@kibana-highlighted-field@\"],\"post_tags\":[\"@/kibana-highlighted-field@\"], \"fields\":{\"*\":{}}},
;  \"filter\":[],
;  \"query\":{\"query_string\":{\"analyze_wildcard\":true,\"query\":\"number: 5\"}}}" } }
