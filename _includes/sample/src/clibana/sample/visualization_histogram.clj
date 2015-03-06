(ns clibana.sample.visualization-histogram
  (:require [clibana.visualization :as cv]))

(cv/visualization
  "vis-id"
  (cv/with-bar-chart
    ;; max of field metric on Y axis
    (cv/chart-with-Y-aggregation :count)
    ;; time values on X axis
    (cv/chart-with-X-aggregation :date-histogram :field "@timestamp"))
  ;; Display data based on saved search
  (cv/with-saved-search "saved search id"))
