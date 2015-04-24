(ns clibana.search
  (:require
    [clibana.internal.common :as cic]
    [clibana.internal.search-int :as cis]
    #+clj [clojure.data.json :as json]
    #+cljs [goog.json :as json])
  )

;; OPTIONS
(defn with-options
  "Specify sequence of options to drive shape of the result document."
  [& options] (apply cic/with-options options))

;; STORABLE ELASTICSEARCH ID
(defn get-document-id
  "Creates string that could be used as ElasticSearch document id.
  Parameter 'search' could be
  string - will be slugified to replace invalid characters
  search map - (produced by clibana.search/search) from which its title will be taken and slugified"
  [search]
  (cond
    (string? search) (cic/as-elastic-id search)
    (map? search) (cic/as-elastic-id (:title search))))

;; DESCRIPTION
(defn with-description
  "Give your search some human readable description."
  [description] (cic/with-description description))

;;; COLUMNS
(defn with-columns
  "Define list of fields that shall be displayed in result."
  [col & cols] {:columns (apply vector col cols)})

;;; SORT
(defn with-sort
  "Sort result based on values in 'column'
  Parameter 'order' is expected to be one of: :asc, :desc, \"asc\", \"desc\"
  Default order is ascending"
  ([column order] {:sort [column (get cis/sorts order "asc")]})
  ([column] {:sort [column "asc"]})
  )

;; Things burried in 'kibanaSavedObjectMeta'

(defn with-index-pattern
  "Index pattern to be used for searching."
  [index-pattern] {:index index-pattern})


(defn with-query
  "Define query. Use 'clibana.query' for definition."
  [& decorations]
  (let [query (cic/<-query decorations)]
    {:index     (cic/take-first :index decorations)
     :highlight {:pre_tags  ["@kibana-highlighted-field@"]
                 :post_tags ["@/kibana-highlighted-field@"]
                 :fields    {"*" {}}}
     :filter    (get decorations :filter [])
     :query     {:query_string {:analyze_wildcard true :query query}}
     }))

(defn search
  "Construct document describing saved search"
  [title & decorations]
  (let [description (cic/<-description decorations)
        options (cic/<-options decorations)
        columns (cic/take-first :columns decorations)
        sort (cic/take-first :sort decorations)
        _search (apply with-query decorations)
        encode-json? (get options :encode-json? true)]
    {:title                 title
     :description           description
     :columns               (or columns ["_source"])
     :sort                  (or sort [(first columns) "asc"])
     :kibanaSavedObjectMeta {:searchSourceJSON (if encode-json?
                                                 #+clj (json/write-str _search)
                                                 #+cljs (json/serialize (clj->js _search))
                                                 _search)}
     }))

