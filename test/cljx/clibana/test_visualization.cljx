(ns clibana.test-visualization
  #+clj (:require
          [clibana.visualization :as cv]
          [clojure.test :refer :all])
  #+cljs (:require-macros
           [cemerick.cljs.test :refer (is deftest testing)])
  #+cljs (:require
           [cemerick.cljs.test :as t]
           [clibana.visualization :as cv])
  )


(deftest with-search
  (testing "no filter"
    (is (= {:search {:index  "an-index"
                     :query  {:query_string {:query "this is query"}}
                     :filter []}}
           (cv/with-search "an-index" {:query "this is query"})))))

(deftest with-parameter-creates-map
  (is (= (cv/chart-with-parameter :mode "stacked")
         {:param {:mode "stacked"}})))

(deftest about-invalid-aggregation
  (testing "invalid aggregation"
    (is (nil? (cv/chart-with-X-aggregation :invalid)))))



(deftest about-date-histogram-aggregation
  (testing "about max aggregation"
    (is (= (cv/chart-with-Y-aggregation :max :field "metric")
           {:aggregation-y {:type "max" :schema "metric" :params {:field "metric"}}}))))

(deftest about-date-histogram-aggregation
  (testing "date histogramvia dispatch, use defaults"
    (is (= (cv/chart-with-X-aggregation :date-histogram "timestamp")
           {:aggregation-x {:type   "date_histogram"
                            :schema "segment"
                            :params {:field           "timestamp"
                                     :interval        "auto"
                                     :min_doc_count   1
                                     :extended_bounds {}}}})))

  )

(deftest terms-via-dispatch-use-defaults
  (is (= (cv/chart-with-X-aggregation :terms "service")
         {:aggregation-x {:type "terms" :schema "group" :params {:field   "service"
                                                                 :size    10
                                                                 :order   "desc"
                                                                 :orderBy "1"
                                                                 }}}))
  )

(deftest terms-via-dispatch-bottom-15
  (is (= (cv/chart-with-X-aggregation :terms "service" :order :asc :size 15)
         {:aggregation-x {:type "terms" :schema "group" :params {:field   "service"
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
          (cv/with-bar-chart
            (cv/chart-with-parameter :mode :percentage)
            (cv/chart-with-Y-aggregation :max :field "metric")))))
  )

(deftest about-with-line-chart
  (testing "line chart with single parameter"
    (is (=
          {:visualization {:type      "line"
                           :listeners {}
                           :params    {:addLegend       true
                                       :addTooltip      true
                                       :defaultYExtents false
                                       :shareYAxis      true}
                           :aggs      [{:id "1" :type "max" :schema "metric" :params {:field "metric"}}]
                           }}
          (cv/with-line-chart
            (cv/chart-with-Y-aggregation :max :field "metric")))))
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
          (cv/with-area-chart
            (cv/chart-with-parameter :mode :percentage)
            (cv/chart-with-Y-aggregation :max :field "metric")))))
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
          (cv/with-pie-chart
            (cv/chart-with-parameter :isDonut true)
            (cv/chart-with-Y-aggregation :max :field "metric")))))
  )

(deftest about-with-metric-chart
  (testing "metric with single parameter"
    (is (=
          {:visualization {:type      "metric"
                           :listeners {}
                           :params    {:fontSize 80}
                           :aggs      [{:id "1" :type "max" :schema "metric" :params {:field "metric"}}]}}
          (cv/with-metric-chart
            (cv/chart-with-parameter :fontSize 80)
            (cv/chart-with-Y-aggregation :max :field "metric")))))
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
          (cv/with-data-table
            (cv/chart-with-parameter :perPage 20)
            (cv/chart-with-Y-aggregation :max :field "metric")))))
  )

(deftest about-with-markdown
  (testing "markdown with text"
    (is (=
          {:visualization {:type      "markdown"
                           :listeners {}
                           :params    {:markdown "A text to display"}
                           :aggs      []}}
          (cv/with-markdown
            (cv/chart-with-parameter :markdown "A text to display")
            ))))
  (testing "markdown with no content"
    (is (=
          {:visualization {:type      "markdown"
                           :listeners {}
                           :params    {:markdown ""}
                           :aggs      []}}
          (cv/with-markdown))))
  )

(deftest about-visualization
  (testing "visualization with terms ordered by count (first aggregation)"
    (is
      (=
        {:title                 "Example"
         :description           "Example description"
         :visState              {:aggs      [{:id "1" :type "count" :schema "metric" :params {}}
                                             {:id     "2" :schema "group" :type "terms"
                                              :params {:field "service" :order "desc" :orderBy "1" :size 10}}]
                                 :listeners {}
                                 :params    {:addLegend true :defaultYExtents false
                                             :mode      "stacked" :addTooltip true :shareYAxis true}
                                 :type      "area"}
         :kibanaSavedObjectMeta {:searchSourceJSON {:index  "index"
                                                    :query  {:query_string {:query "query"}}
                                                    :filter []}}
         }
        (cv/visualization "Example"
                          (cv/with-description "Example description")
                          ;; Let the output is clojure structure
                          (cv/with-options :encode-json? false)
                          (cv/with-area-chart
                            (cv/chart-with-parameter :mode :stacked)
                            (cv/chart-with-Y-aggregation :count)
                            (cv/chart-with-X-aggregation :terms "service"))
                          (cv/with-search "index" {:query "query"})))))

  (testing "visualization with terms ordered by second Y aggregation"
    (is
      (=
        {:title                 "Example"
         :description           "Example description"
         :visState              {:aggs      [{:id "1" :type "max" :schema "metric" :params {:field "field"}}
                                             {:id "2" :type "count" :schema "metric" :params {}}
                                             {:id "3" :schema "group" :type "terms" :params {:field   "service"
                                                                                             :order   "desc"
                                                                                             :orderBy "2"
                                                                                             :size    10}}]
                                 :listeners {}
                                 :params    {:addLegend       true :addTooltip true
                                             :defaultYExtents false :mode "stacked" :shareYAxis true}
                                 :type      "area"}
         :kibanaSavedObjectMeta {:searchSourceJSON {:index  "index"
                                                    :query  {:query_string {:query "query"}}
                                                    :filter []}}
         }
        (cv/visualization "Example"
                          (cv/with-description "Example description")
                          ;; Let the output is clojure structure
                          (cv/with-options :encode-json? false)
                          (cv/with-area-chart
                            (cv/chart-with-parameter :mode :stacked)
                            ;; id = 1
                            (cv/chart-with-Y-aggregation :max :field "field")
                            ;; id = 2
                            (cv/chart-with-Y-aggregation :count :id :count-1)
                            ;; id = 3
                            (cv/chart-with-X-aggregation :terms "service" :orderBy :count-1))
                          (cv/with-search "index" {:query "query"})))))

  )
