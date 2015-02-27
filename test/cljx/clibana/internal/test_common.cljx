(ns clibana.internal.test-common
  #+clj (:require [clibana.internal.common :as c]
                  [clojure.test :refer :all])
  #+cljs (:require-macros [cemerick.cljs.test :refer (is deftest testing)])
  #+cljs (:require [cemerick.cljs.test :as t]
           [clibana.internal.common :as c])
  )

(deftest about-options
  (testing "no options"
    (is (nil? (c/<-options []))))
  (testing "single option"
    (is (= (c/<-options [{:options {:yes true}}])
           {:yes true})))
  (testing "multiple options"
    (is (= (c/<-options [{:options {:yes true :no false}}])
           {:yes true :no false})))
  )


(deftest about-description
  (testing "no description"
    (is (= (c/<-description []) "")))

  (testing "single description"
    (is (= (c/<-description [{:description "Description"}])
           "Description")))

  (testing "multiple descriptions"
    (is (= (c/<-description [{:description "Description"}
                             {:description "Shall be discarded"}])
           "Description")))
  )


(deftest about-param
  (testing "no parameter"
    (is (nil? (c/<-param [{:invalid :whatever}]))))
  (testing "single parameter"
    (is (= (c/<-param [{:param {:p1 1}}])
           {:p1 1})))
  (testing "multiple parameters"
    (is (= (c/<-param [{:param {:p1 1}} {:param {:p2 2}}])
           {:p1 1 :p2 2})))
  (testing "parameter mixed with something else"
    (is (= (c/<-param [{:param {:p1 1}} {:whatever :whatever}])
           {:p1 1})))
  (testing "same parameter twice with different value - latest wins"
    (is (= (c/<-param [{:param {:p1 1}} {:param {:p1 2}}])
           {:p1 2}))))


(deftest about-aggregation-x
  (testing "single aggregation"
    (is (= [{:a1 1}]
           (c/<-aggregation-x [{:aggregation-x {:a1 1}}]))))

  (testing "multiple aggregations"
    (is (= [{:a1 1} {:a2 2}]
           (c/<-aggregation-x [{:aggregation-x {:a1 1}} {:aggregation-x {:a2 2}}]))))

  (testing "aggregation mixed with something else"
    (is (= [{:a1 1}]
           (c/<-aggregation-x [{:aggregation-x {:a1 1}} {:whatever :whatever}])))))

(deftest about-aggregation-y
  (testing "single aggregation"
    (is (= [{:a1 1}]
           (c/<-aggregation-y [{:aggregation-y {:a1 1}}]))))

  (testing "multiple aggregations"
    (is (= [{:a1 1} {:a2 2}]
           (c/<-aggregation-y [{:aggregation-y {:a1 1}} {:aggregation-y {:a2 2}}]))))

  (testing "aggregation mixed with something else"
    (is (= [{:a1 1}]
           (c/<-aggregation-y [{:aggregation-y {:a1 1}} {:whatever :whatever}])))))


(deftest about-visualization
  (testing "single visualization"
    (is (= (c/<-visualization [{:visualization {:v1 1}}])
           {:v1 1})))

  (testing "multiple visualization - first wins"
    (is (= (c/<-visualization [{:visualization {:v1 1}} {:visualization {:v2 2}}])
           {:v1 1}))))



(deftest about-search
  (testing "no search"
    (is (nil? (c/<-search []))))
  (testing "single search"
    (is (= (c/<-search [{:search "search query"}])
           "search query")))
  (testing "multiple searches"
    (is (= (c/<-search [{:search "search query"} {:search "discarded search query"}])
           "search query")))
  )

(deftest about-saved-search
  (testing "no saved search"
    (is (nil? (c/<-saved-search []))))
  (testing "single search"
    (is (= (c/<-saved-search [{:saved-search "id"}])
           "id")))
  (testing "multiple searches"
    (is (= (c/<-saved-search [{:saved-search "id"} {:saved-search "discardedid"}])
           "id")))
  )

(deftest about-listener
  (testing "no listener"
    (is (= (c/<-listener []) {})))
  (testing "single listener"
    (is (= (c/<-listener [{:listener "whatever"}])
           {})))
  (testing "multiple listeners"
    (is (= (c/<-listener [{:listener "some"} {:listener "thing"}])
           {})))
  )



(deftest with-description
  (is (= (c/with-description "D") {:description "D"})))

(deftest with-options
  (is (= (c/with-options) {:options {}})))
