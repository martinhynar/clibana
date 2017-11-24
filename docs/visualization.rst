Visualizations
=======================

In Kibana, you can create objects that describe what data will be displayed in some graph. In terms of Kibana, this
is called <i>visualization</i>. Visualizations are stored in underlying ElasticSearch database as documents that
could be referenced from dashboards and displayed. In the following examples,
visualizations with various graphs will be created and explained.

::
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

