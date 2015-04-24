(ns clibana.internal.test-common
  #+clj (:require [clibana.internal.common :as cic]
                  [clojure.test :refer :all])
  #+cljs (:require-macros [cemerick.cljs.test :refer (is deftest testing)])
  #+cljs (:require [cemerick.cljs.test :as t]
           [clibana.internal.common :as cic])
  )

(deftest as-elastic-id
  (testing "no replacement"
    (is (= "valid"
           (cic/as-elastic-id "valid"))))
  (testing "slash"
    (is (= "with-slash-character"
           (cic/as-elastic-id "with/character"))))
  (testing "questionmark"
    (is (= "with-questionmark-character"
           (cic/as-elastic-id "with?character"))))
  (testing "ampersand"
    (is (= "with-ampersand-character"
           (cic/as-elastic-id "with&character"))))
  (testing "equal"
    (is (= "with-equal-character"
           (cic/as-elastic-id "with=character"))))
  (testing "space"
    (is (= "with-space"
           (cic/as-elastic-id "with space"))))
  (testing "long space"
    (is (= "with-space"
           (cic/as-elastic-id "with     space"))))
  (testing "multiple dashes"
    (is (= "with-dash"
           (cic/as-elastic-id "with  ---  dash"))))
  (testing "multiple subsequent specials"
    (is (= "with-slash-questionmark-ampersand-equal-characters"
           (cic/as-elastic-id "with/?&=characters"))))
  (testing "double slugification with multiple subsequent specials"
    (is (= "with-slash-questionmark-ampersand-equal-characters"
           (cic/as-elastic-id (cic/as-elastic-id "with/?&=characters")))))
  )

(deftest about-options
  (testing "no options"
    (is (nil? (cic/<-options []))))
  (testing "single option"
    (is (= (cic/<-options [{:options {:yes true}}])
           {:yes true})))
  (testing "multiple options"
    (is (= (cic/<-options [{:options {:yes true :no false}}])
           {:yes true :no false})))
  )


(deftest about-description
  (testing "no description"
    (is (= (cic/<-description []) "")))

  (testing "single description"
    (is (= (cic/<-description [{:description "Description"}])
           "Description")))

  (testing "multiple descriptions"
    (is (= (cic/<-description [{:description "Description"}
                               {:description "Shall be discarded"}])
           "Description")))
  )


(deftest about-param
  (testing "no parameter"
    (is (nil? (cic/<-param [{:invalid :whatever}]))))
  (testing "single parameter"
    (is (= (cic/<-param [{:param {:p1 1}}])
           {:p1 1})))
  (testing "multiple parameters"
    (is (= (cic/<-param [{:param {:p1 1}} {:param {:p2 2}}])
           {:p1 1 :p2 2})))
  (testing "parameter mixed with something else"
    (is (= (cic/<-param [{:param {:p1 1}} {:whatever :whatever}])
           {:p1 1})))
  (testing "same parameter twice with different value - latest wins"
    (is (= (cic/<-param [{:param {:p1 1}} {:param {:p1 2}}])
           {:p1 2}))))


(deftest about-aggregation-x
  (testing "single aggregation"
    (is (= [{:a1 1}]
           (cic/<-aggregation-x [{:aggregation-x {:a1 1}}]))))

  (testing "multiple aggregations"
    (is (= [{:a1 1} {:a2 2}]
           (cic/<-aggregation-x [{:aggregation-x {:a1 1}} {:aggregation-x {:a2 2}}]))))

  (testing "aggregation mixed with something else"
    (is (= [{:a1 1}]
           (cic/<-aggregation-x [{:aggregation-x {:a1 1}} {:whatever :whatever}])))))

(deftest about-aggregation-y
  (testing "single aggregation"
    (is (= [{:a1 1}]
           (cic/<-aggregation-y [{:aggregation-y {:a1 1}}]))))

  (testing "multiple aggregations"
    (is (= [{:a1 1} {:a2 2}]
           (cic/<-aggregation-y [{:aggregation-y {:a1 1}} {:aggregation-y {:a2 2}}]))))

  (testing "aggregation mixed with something else"
    (is (= [{:a1 1}]
           (cic/<-aggregation-y [{:aggregation-y {:a1 1}} {:whatever :whatever}])))))


(deftest about-visualization
  (testing "single visualization"
    (is (= (cic/<-visualization [{:visualization {:v1 1}}])
           {:v1 1})))

  (testing "multiple visualization - first wins"
    (is (= (cic/<-visualization [{:visualization {:v1 1}} {:visualization {:v2 2}}])
           {:v1 1}))))



(deftest about-search
  (testing "no search"
    (is (nil? (cic/<-search []))))
  (testing "single search"
    (is (= (cic/<-search [{:search "search query"}])
           "search query")))
  (testing "multiple searches"
    (is (= (cic/<-search [{:search "search query"} {:search "discarded search query"}])
           "search query")))
  )

(deftest about-saved-search
  (testing "no saved search"
    (is (nil? (cic/<-saved-search []))))
  (testing "single search"
    (is (= (cic/<-saved-search [{:saved-search "id"}])
           "id")))
  (testing "multiple searches"
    (is (= (cic/<-saved-search [{:saved-search "id"} {:saved-search "discardedid"}])
           "id")))
  )

(deftest about-listener
  (testing "no listener"
    (is (= (cic/<-listener []) {})))
  (testing "single listener"
    (is (= (cic/<-listener [{:listener "whatever"}])
           {})))
  (testing "multiple listeners"
    (is (= (cic/<-listener [{:listener "some"} {:listener "thing"}])
           {})))
  )



(deftest with-description
  (is (= (cic/with-description "D") {:description "D"})))

(deftest with-options
  (is (= (cic/with-options) {:options {}})))
