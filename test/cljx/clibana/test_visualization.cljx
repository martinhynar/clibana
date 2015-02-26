(ns clibana.test-visualization
  #+clj (:require
          [clibana.visualization :as v]
          [clojure.test :refer :all])
  #+cljs (:require-macros
           [cemerick.cljs.test :refer (is deftest testing)])
  #+cljs (:require
           [cemerick.cljs.test :as t]
           [clibana.visualization :as v])
  )


(deftest with-search
  (testing "no filter"
    (is (= {:search {:index  "an-index"
                     :query  {:query_string {:query "this is query"}}
                     :filter []}}
           (v/with-search "an-index" {:query "this is query"})))))

(deftest with-parameter-creates-map
  (is (= (v/chart-with-parameter :mode "stacked")
         {:param {:mode "stacked"}})))

(deftest about-invalid-aggregation
  (testing "invalid aggregation"
    (is (nil? (v/chart-with-aggregation :invalid)))))



(deftest about-date-histogram-aggregation
  (testing "about max aggregation"
    (is (= (v/chart-with-aggregation :max "metric")
           {:aggregation {:type "max" :schema "metric" :params {:field "metric"}}}))))

(deftest about-date-histogram-aggregation
  (testing "date histogramvia dispatch, use defaults"
    (is (= (v/chart-with-aggregation :date-histogram "timestamp")
           {:aggregation {:type   "date_histogram"
                          :schema "segment"
                          :params {:field           "timestamp"
                                   :interval        "auto"
                                   :min_doc_count   1
                                   :extended_bounds {}}}})))

  )

(deftest terms-via-dispatch-use-defaults
  (is (= (v/chart-with-aggregation :terms "service")
         {:aggregation {:type "terms" :schema "group" :params {:field   "service"
                                                               :size    10
                                                               :order   "desc"
                                                               :orderBy "1"
                                                               }}}))
  )

(deftest terms-via-dispatch-bottom-15
  (is (= (v/chart-with-aggregation :terms "service" :order :asc :size 15)
         {:aggregation {:type "terms" :schema "group" :params {:field   "service"
                                                               :size    15
                                                               :order   "asc"
                                                               :orderBy "1"
                                                               }}}))
  )

(deftest about-with-bar-chart
  (testing "histogram with single parameter"
    (is (=
          {:visualization {:type      "histogram"
                           :listeners {}
                           :params    {:addLegend       true
                                       :addTooltip      true
                                       :defaultYExtents false
                                       :mode            "percentage"
                                       :shareYAxis      true}
                           :aggs      [{:id "1" :type "max" :schema "metric" :params {:field "metric"}}]
                           }}
          (v/with-bar-chart
            (v/chart-with-parameter :mode :percentage)
            (v/chart-with-aggregation :max "metric")))))
  )

(deftest about-with-area-chart
  (testing "area with single parameter"
    (is (=
          {:visualization {:type      "area"
                           :listeners {}
                           :params    {:addLegend       true
                                       :addTooltip      true
                                       :defaultYExtents false
                                       :mode            "percentage"
                                       :shareYAxis      true}
                           :aggs      [{:id "1" :type "max" :schema "metric" :params {:field "metric"}}]
                           }}
          (v/with-area-chart
            (v/chart-with-parameter :mode :percentage)
            (v/chart-with-aggregation :max "metric")))))
  )

(deftest about-with-pie-chart
  (testing "pie with single parameter"
    (is (=
          {:visualization {:type      "pie"
                           :listeners {}
                           :params    {:addLegend  true
                                       :addTooltip true
                                       :isDonut    true
                                       :shareYAxis true}
                           :aggs      [{:id "1" :type "max" :schema "metric" :params {:field "metric"}}]

                           }}
          (v/with-pie-chart
            (v/chart-with-parameter :isDonut true)
            (v/chart-with-aggregation :max "metric")))))
  )

(deftest about-with-metric-chart
  (testing "metric with single parameter"
    (is (=
          {:visualization {:type      "metric"
                           :listeners {}
                           :params    {:fontSize 80}
                           :aggs      [{:id "1" :type "max" :schema "metric" :params {:field "metric"}}]}}
          (v/with-metric-chart
            (v/chart-with-parameter :fontSize 80)
            (v/chart-with-aggregation :max "metric")))))
  )

(deftest about-with-data-table
  (testing "data table with single parameter"
    (is (=
          {:visualization {:type      "table"
                           :listeners {}
                           :params    {:perPage               20
                                       :showPartialRows       false
                                       :showMeticsAtAllLevels false}
                           :aggs      [{:id "1" :type "max" :schema "metric" :params {:field "metric"}}]}}
          (v/with-data-table
            (v/chart-with-parameter :perPage 20)
            (v/chart-with-aggregation :max "metric")))))
  )

(deftest about-with-markdown
  (testing "markdown with text"
    (is (=
          {:visualization {:type      "markdown"
                           :listeners {}
                           :params    {:markdown "A text to display"}
                           :aggs      []}}
          (v/with-markdown
            (v/chart-with-parameter :markdown "A text to display")
            ))))
  (testing "markdown with no content"
    (is (=
          {:visualization {:type      "markdown"
                           :listeners {}
                           :params    {:markdown ""}
                           :aggs      []}}
          (v/with-markdown))))
  )
