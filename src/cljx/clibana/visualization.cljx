(ns clibana.visualization
  (:require
    [clibana.internal.common :as cic]
    [clibana.internal.visualization_int :as civ]
    #+clj [clojure.data.json :as json]
    #+cljs [goog.json :as json]
    ))

;; OPTIONS
(defn with-options
  "Specify sequence of options to drive shape of the result document."
  [& options] (apply cic/with-options options))

;; STORABLE ELASTICSEARCH ID
(defn get-document-id
  "Creates string that could be used as ElasticSearch document id.
  Parameter 'visualization' could be
  string - will be slugified to replace invalid characters
  search map - (produced by clibana.visualization/visualization) from which its title will be taken and slugified"
  [visualization]
  (cond
    (string? visualization) (cic/as-elastic-id visualization)
    (map? visualization) (cic/as-elastic-id (:title visualization))))

;; DESCRIPTION
(defn with-description
  "Give your visualization some human readable description."
  [description] (cic/with-description description))

;;; SEARCH
(defn with-saved-search
  "In visualization, use stored search. Parameter 'search' could be either
   string or search structure (from clibana.search/search). The id will be
   produced with get-document-id."
  [search] {:saved-search (get-document-id search)})

(defn with-search
  "Use in-place created search."
  [index & decorations]
  (let [query (cic/<-query decorations)]
    {:search {:index  index
              :query  {:query_string {:query query}}
              :filter (get decorations :filter [])}}))


;;; AGGREGATIONS

(defn aggregation-terms [field & options]
  (let [options (apply hash-map options)]
    {:aggregation-x {:type   "terms"
                     :schema (get options :schema "group")
                     :params {:field   field
                              :size    (get options :size 10)
                              :order   (name (get options :order "desc"))
                              :orderBy (if-let [oby (get options :orderBy)] oby "1")
                              }}}))

(defn aggregation-date-histogram [field & options]
  (let [options (apply hash-map options)]
    {:aggregation-x {:type   "date_histogram"
                     :schema "segment"
                     :params {:field           field
                              :interval        (get options :interval "auto")
                              :min_doc_count   (get options :min-doc 1)
                              :extended_bounds {}}}}))




(defn chart-with-Y-aggregation [type & parameters]
  (let [parameters (apply hash-map parameters)]
    (condp = type
      :count (let [base {:type "count" :schema "metric" :params {}}]
               (if-let [id (:id parameters)]
                 {:aggregation-y (assoc base :id id)}
                 {:aggregation-y base}
                 )
               )

      :average (civ/aggregation-y-template "avg" parameters)
      :sum (civ/aggregation-y-template "sum" parameters)
      :max (civ/aggregation-y-template "max" parameters)
      :min (civ/aggregation-y-template "min" parameters)
      :standard-deviation (civ/aggregation-y-template "std_dev" parameters)
      :unique-count (civ/aggregation-y-template "cardinality" parameters)
      :percentiles {:aggregation-y {:type   "percentiles" :schema "metric"
                                    :params {:field    (:field parameters)
                                             :percents (if-let [percents (:percens parameters)]
                                                         percents
                                                         [1 5 25 50 75 95 99])}}}
      ;; Default sink
      nil
      )))

(defn chart-with-X-aggregation [type & parameters]
  (condp = type
    :terms (apply aggregation-terms parameters)
    :date-histogram (apply aggregation-date-histogram parameters)
    ;; Default sink
    nil
    )
  )



;; CHART PARAMETERS
(defn chart-with-parameter
  "Define parameter for chart"
  [parameter-name parameter-value] {:param {parameter-name parameter-value}})


(defn with-bar-chart
  "Construct histogram bar plot"
  [& decorations]
  (apply civ/bar-chart decorations))

(defn with-line-chart
  "Construct line plot"
  [& decorations]
  (apply civ/line-chart decorations))

(defn with-area-chart
  "Construct area plot"
  [& decorations]
  (apply civ/area-chart decorations))

(defn with-pie-chart
  "Construct pie plot"
  [& decorations]
  (apply civ/pie-chart decorations))

(defn with-metric-chart
  "Construct metric"
  [& decorations]
  (apply civ/metric-chart decorations))

(defn with-data-table
  "Construct data table"
  [& decorations]
  (apply civ/data-table decorations))

(defn with-markdown
  "Construct markdown component"
  [& decorations]
  (apply civ/markdown decorations))


(defn- asearch [search]
  (if search
    #+clj (json/write-str search)
    #+cljs (json/serialize (clj->js search))
    #+clj (json/write-str {:filter []})
    #+cljs (json/serialize (clj->js {:filter []}))
    ))

(defn visualization
  "Construct document describing visualization"
  [title & decorations]
  (let [description (cic/<-description decorations)
        options (cic/<-options decorations)
        visualization (cic/<-visualization decorations)
        search (cic/<-search decorations)
        encode-json? (get options :encode-json? true)
        vis-doc (atom {:title                 title
                       ;; If there is no description given, empty string is used
                       :description           description

                       :visState              (if encode-json?
                                                #+clj (json/write-str visualization)
                                                #+cljs (json/serialize (clj->js visualization))
                                                visualization)
                       :kibanaSavedObjectMeta {:searchSourceJSON (if encode-json?
                                                                   (asearch search)
                                                                   search)}
                       })]

    (when-let [saved-search (cic/<-saved-search decorations)]
      (swap! vis-doc assoc :savedSearchId saved-search))
    @vis-doc))


(comment
  ;; With defined search
  (visualization "An Example"
                 (with-description "This visualization shows ...")
                 (with-options :encode-json? false)
                 (with-area-chart
                   (chart-with-parameter :mode :stacked)
                   (chart-with-parameter :spyPerPage 10)
                   (chart-with-Y-aggregation :max "metric")
                   (chart-with-X-aggregation :date-histogram "@timestamp")
                   (chart-with-X-aggregation :terms "service"))
                 (with-search "build-*" {:query "service : \"cpu#1 user\""}))

  ;; With saved search
  (visualization "An Example"
                 (with-description "This visualization shows ...")
                 (with-options :encode-json? false)
                 (with-bar-chart
                   (chart-with-parameter :mode :stacked)
                   (chart-with-parameter :spyPerPage 10)
                   (chart-with-Y-aggregation :max "metric")
                   (chart-with-X-aggregation :date-histogram "@timestamp")
                   (chart-with-X-aggregation :terms "service"))
                 (with-saved-search "saved-search-id"))
  )