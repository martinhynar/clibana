(ns clibana.visualization
  (:require
    [clibana.internal.common :as c]
    [clibana.internal.viscommon :as vc]
    #+clj [clojure.data.json :as json]
    #+cljs [goog.json :as json]
    ))

;; OPTIONS
(defn with-options
  "Specify sequence of options to drive shape of the result document."
  [& options] (c/with-options options))

;; DESCRIPTION
(defn with-description
  "Give your visualization some human readable description."
  [description] (c/with-description description))

;;; SEARCH
(defn with-saved-search
  "In visualization, use stored search with given ID."
  [id] {:saved-search id})

(defn with-search
  "Use in-place created search."
  [index & decorations]
  (let [query (c/<-query decorations)]
    {:search {:index  index
              :query  {:query_string {:query query}}
              :filter (get decorations :filter [])}}))




;;; AGGREGATIONS

(defn aggregation-max [field] {:aggregation {:type "max" :schema "metric" :params {:field field}}})

(defn aggregation-terms [field & options]
  (let [options (apply hash-map options)]
    {:aggregation {:type   "terms"
                   :schema "group"
                   :params {:field   field
                            :size    (get options :size 10)
                            :order   (name (get options :order "desc"))
                            :orderBy "1"
                            }}}))

(defn aggregation-date-histogram [field & options]
  (let [options (apply hash-map options)]
    {:aggregation {:type   "date_histogram"
                   :schema "segment"
                   :params {:field           field
                            :interval        (get options :interval "auto")
                            :min_doc_count   (get options :min-doc 1)
                            :extended_bounds {}}}}))




(defn chart-with-aggregation [type & parameters]
  (condp = type
    :max (apply aggregation-max parameters)
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
  (apply vc/bar-chart decorations))

(defn with-area-chart
  "Construct area plot"
  [& decorations]
  (apply vc/area-chart decorations))

(defn with-pie-chart
  "Construct pie plot"
  [& decorations]
  (apply vc/pie-chart decorations))

(defn with-metric-chart
  "Construct metric"
  [& decorations]
  (apply vc/metric-chart decorations))

(defn with-data-table
  "Construct data table"
  [& decorations]
  (apply vc/data-table decorations))

(defn with-markdown
  "Construct markdown component"
  [& decorations]
  (apply vc/markdown decorations))


(defn visualization
  "Construct document describing visualization"
  [title & decorations]
  (let [description (c/<-description decorations)
        options (c/<-options decorations)
        visualization (c/<-visualization decorations)
        search (c/<-search decorations)
        encode-json? (get options :encode-json? true)
        vis-doc (atom {:title                 title
                       ;; If there is no description given, empty string is used
                       :description           description

                       :visState              (if encode-json?
                                                #+clj (json/write-str visualization)
                                                #+cljs (json/serialize (clj->js visualization))
                                                visualization)
                       :kibanaSavedObjectMeta {:searchSourceJSON (if encode-json?
                                                                   #+clj (json/write-str search)
                                                                   #+cljs (json/serialize (clj->js search))
                                                                   search)}
                       })]

    (when-let [saved-search (c/<-saved-search decorations)]
      (swap! vis-doc assoc :savedSearchId saved-search))

    @vis-doc
    ))


(comment
  ;; With defined search
  (visualization "An Example"
                 (with-description "This visualization shows ...")
                 (with-options :encode-json? false)
                 (with-area-chart
                   (chart-with-parameter :mode :stacked)
                   (chart-with-parameter :spyPerPage 10)
                   (chart-with-aggregation :max "metric")
                   (chart-with-aggregation :date-histogram "@timestamp")
                   (chart-with-aggregation :terms "service"))
                 (with-search "build-*" {:query "service : \"cpu#1 user\""}))

  ;; With saved search
  (visualization "An Example"
                 (with-description "This visualization shows ...")
                 (with-options :encode-json? false)
                 (with-bar-chart
                   (chart-with-parameter :mode :stacked)
                   (chart-with-parameter :spyPerPage 10)
                   (chart-with-aggregation :max "metric")
                   (chart-with-aggregation :date-histogram "@timestamp")
                   (chart-with-aggregation :terms "service"))
                 (with-saved-search "saved-search-id"))
  )