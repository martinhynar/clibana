(ns clibana.sample.visualization-histogram
  (:require [clibana.visualization :as cv]
            [clibana.query :as cq]))

(cv/visualization
  "vis-id"
  (cv/with-bar-chart
    ;; Display histogram as percentage
    (cv/chart-with-parameter :mode "percentage")
    ;; max of metric on Y axis
    (cv/chart-with-aggregation :max "metric")
    ;; time values on X axis
    (cv/chart-with-aggregation :date-histogram "@timestamp")
    ;; Split histogram bars by terms of service field
    (cv/chart-with-aggregation :terms "service"))
  (cv/with-saved-search "saved search id"))
