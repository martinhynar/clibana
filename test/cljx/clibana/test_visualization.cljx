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


(deftest with-parameter-creates-map
  (is (= (v/with-parameter :mode "stacked")
         {:param {:mode "stacked"}})))

(deftest about-param?
  (testing "no parameter"
    (is (nil? (v/param? [{:invalid :whatever}]))))
  (testing "single parameter"
    (is (= (v/param? [{:param {:p1 1}}])
           {:p1 1})))
  (testing "multiple parameters"
    (is (= (v/param? [{:param {:p1 1}} {:param {:p2 2}}])
           {:p1 1 :p2 2})))
  (testing "parameter mixed with something else"
    (is (= (v/param? [{:param {:p1 1}} {:whatever :whatever}])
           {:p1 1})))
  (testing "same parameter twice with different value - latest wins"
    (is (= (v/param? [{:param {:p1 1}} {:param {:p1 2}}])
           {:p1 2}))))


(deftest about-aggregation?
  (testing "single aggregation"
    (is (= (v/aggregation? [{:aggregation {:a1 1}}])
           [{:a1 1}])))

  (testing "multiple aggregations"
    (is (= (v/aggregation? [{:aggregation {:a1 1}} {:aggregation {:a2 2}}])
           [{:a1 1} {:a2 2}])))

  (testing "aggregation mixed with something else"
    (is (= (v/aggregation? [{:aggregation {:a1 1}} {:whatever :whatever}])
           [{:a1 1}])))

  )


(deftest about-visualization?
  (testing "single visualization"
    (is (= (v/visualization? [{:visualization {:v1 1}}])
           {:v1 1})))

  (testing "multiple visualization - first wins"
    (is (= (v/visualization? [{:visualization {:v1 1}} {:visualization {:v2 2}}])
           {:v1 1}))))


(deftest about-invalid-aggregation
  (testing "invalid aggregation"
    (is (nil? (v/with-aggregation :invalid)))))



(deftest about-date-histogram-aggregation
  (testing "about max aggregation"
    (is (= (v/with-aggregation :max "metric")
           {:aggregation {:type "max" :schema "metric" :params {:field "metric"}}}))))

(deftest about-date-histogram-aggregation
  (testing "date histogramvia dispatch, use defaults"
    (is (= (v/with-aggregation :date-histogram "timestamp")
           {:aggregation {:type   "date_histogram"
                          :schema "segment"
                          :params {:field           "timestamp"
                                   :interval        "auto"
                                   :min_doc_count   1
                                   :extended_bounds {}}}})))

  )

(deftest terms-via-dispatch-use-defaults
  (is (= (v/with-aggregation :terms "service")
         {:aggregation {:type "terms" :schema "group" :params {:field   "service"
                                                              :size    10
                                                              :order   "desc"
                                                              :orderBy "1"
                                                              }}}))
  )

(deftest terms-via-dispatch-bottom-15
  (is (= (v/with-aggregation :terms "service" :order :asc :size 15)
         {:aggregation {:type "terms" :schema "group" :params {:field   "service"
                                                               :size    15
                                                               :order   "asc"
                                                               :orderBy "1"
                                                               }}}))
  )

(deftest about-with-histogram
  (testing "histogram with single parameter"
    (is (= (v/with-histogram
             (v/with-parameter :mode "percentage")
             (v/with-aggregation :max "metric"))
           {:visualization {:type "histogram" :params {:addLegend       true
                                                       :addTooltip      true
                                                       :defaultYExtents false
                                                       :mode            "percentage"
                                                       :shareYAxis      true}
                            :aggs [{:id "1" :type "max" :schema "metric" :params {:field "metric"}}] :listeners {}}})))
  )
