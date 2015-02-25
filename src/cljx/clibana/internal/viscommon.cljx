(ns clibana.internal.viscommon
  (:require [clibana.internal.common :as c]))



(defn resolve-params [default-params chart-params params]
  (reduce
    (fn [m [k v]]
      (if-let [f (k chart-params)]
        (assoc m k (f v)) m))
    default-params params)
  )


(defn chart [type default-params chart-params & decorations]
  (let [options (c/<-options decorations)
        params (resolve-params default-params chart-params (c/<-param decorations))
        listeners (c/<-listener decorations)
        aggregations (c/<-aggregation decorations)
        aggregations (mapv (fn [i a] (assoc a :id (str i))) (c/gen-ids) aggregations)
        ]
    {:visualization {:type      type
                     :params    params
                     :listeners listeners
                     :aggs      aggregations
                     }})
  )


(def modes {:stacked    "stacked"
            :percentage "percentage"
            :grouped    "grouped"
            :overlap    "overlap"
            :wiggle     "wiggle"
            :silhouette "silhouette"})

(def bar-params {:addLegend       #(true? %)
                 :addTooltip      #(true? %)
                 :shareYAxis      #(true? %)
                 :defaultYExtents #(true? %)
                 :mode            (fn [v]
                                    (let [relevant-modes [:stacked :percentage :grouped]
                                          modes (select-keys modes relevant-modes)]
                                      (get modes v "stacked")))})

(def area-params {:addLegend       #(true? %)
                  :addTooltip      #(true? %)
                  :shareYAxis      #(true? %)
                  :defaultYExtents #(true? %)
                  :mode            (fn [v]
                                     (let [relevant-modes [:stacked :percentage :overlap :wiggle :silhouette]
                                           modes (select-keys modes relevant-modes)]
                                       (get modes v "stacked")))})

(def pie-params {:addLegend  #(true? %)
                 :addTooltip #(true? %)
                 :shareYAxis #(true? %)
                 :isDonut    #(true? %)})

(def metric-params {:fontSize #(if (number? %) % 60)})


(def area-chart (partial chart "area"
                         {:addLegend       true
                          :addTooltip      true
                          :shareYAxis      true
                          :defaultYExtents false
                          :mode            "stacked"}
                         area-params))

(def bar-chart (partial chart "histogram"
                        {:addLegend       true
                         :addTooltip      true
                         :shareYAxis      true
                         :defaultYExtents false
                         :mode            "stacked"}
                        bar-params))

(def pie-chart (partial chart "pie"
                        {:addLegend  true
                         :addTooltip true
                         :shareYAxis true
                         :isDonut    false}
                        pie-params))

(def metric-chart (partial chart "metric"
                           {:fontSize 60}
                           metric-params))


(def data-table-params {:perPage               #(if (number? %) % 10)
                        :showPartialRows       #(true? %)
                        :showMeticsAtAllLevels #(true? %)})

(def data-table (partial chart "table"
                         {:perPage               10
                          :showPartialRows       false
                          :showMeticsAtAllLevels false}
                         data-table-params))

(def markdown-params {:markdown #(or % "")})

(def markdown (partial chart "markdown"
                       {:markdown ""}
                       markdown-params))
