(ns clibana.visualization
  (:require
    [clibana.internal.common :as c]
    #+clj [clojure.data.json :as json]
    #+cljs [goog.json :as json]
    ))


;; DESCRIPTION
(def with-description c/with-description)

;;; SEARCH
(defn with-saved-search [id] {:saved-search id})

(defn with-search [& options]
  {:search (let [options (apply hash-map options)]
             {:index  (:index options)
              :query  {:query_string {:query (:query options)}}
              :filter (get options :filter [])})})




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




(defn with-aggregation [type & parameters]
  (condp = type
    :max (apply aggregation-max parameters)
    :terms (apply aggregation-terms parameters)
    :date-histogram (apply aggregation-date-histogram parameters)
    ;; Default sink
    nil
    )
  )









(defn visualization? [decorations] (:visualization (first (filter :visualization decorations))))
(defn search? [decorations] (first (filter :search decorations)))

(defn param? [decorations] (apply merge (map :param (filter :param decorations))))

(defn listener? [decorations] (filter :listener decorations))
(defn aggregation? [decorations] (remove nil? (map :aggregation decorations)))




(defn with-histogram
  "Construct histogram plot"
  [& decorations]
  (let [options (c/options? decorations)
        params (merge
                 ;; Default parameters
                 {:addLegend       true
                  :addTooltip      true
                  :defaultYExtents false
                  :mode            "stacked"
                  :shareYAxis      true}
                 ;; Provided parameters
                 (param? decorations))
        ;; TODO: Listener is map
        listeners (listener? decorations)
        listeners {}
        aggregations (aggregation? decorations)
        aggregations (mapv (fn [i a] (assoc a :id (str i))) (c/gen-ids) aggregations)
        ]
    {:visualization {:type      "histogram"
                     :params    params
                     :listeners listeners
                     :aggs      aggregations
                     }}))




(defn with-parameter
  "define parameter for visualization plot"
  [parameter-name parameter-value] {:param {parameter-name parameter-value}})

(defn visualization
  "Construct document describing visualization"
  [title & decorations]
  (let [description (c/description? decorations)
        options (c/options? decorations)
        visualization (visualization? decorations)
        search (:search (search? decorations))
        encode-json? (get options :encode-json? true)]
    {:title                 title
     :description           description
     :visState              (if encode-json?
                              #+clj (json/write-str visualization)
                              #+cljs (json/serialize (clj->js visualization))
                              visualization)
     :kibanaSavedObjectMeta {:searchSourceJSON (if encode-json?
                                                 #+clj (json/write-str search)
                                                 #+cljs (json/serialize (clj->js search))
                                                 search)}
     }))


(comment
  (defn example-visualization []
    (visualization "An Example"
                   (with-options :encode-json? false)
                   (with-histogram
                     (with-parameter :mode :stacked)
                     (with-parameter :spyPerPage 10)
                     (with-aggregation :max "metric")
                     (with-aggregation :date-histogram "@timestamp")
                     (with-aggregation :terms "service"))
                   (with-search :index "build-*" :query "service : \"cpu#1 user\"" :filter []))
    ))