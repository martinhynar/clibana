(ns clibana.test-dashboard
  #+clj (:require [clibana.internal.common :as cic]
                  [clibana.dashboard :as cd]
                  [clojure.test :refer :all])
  #+cljs (:require-macros [cemerick.cljs.test :refer (is deftest testing)])
  #+cljs (:require [cemerick.cljs.test :as t]
           [clibana.dashboard :as cd]
           [clibana.internal.common :as cic]))


;
;(tabular
;  (fact "about is panel predicate" (d/is-panel? ?decoration) => ?expected)
;  ?decoration ?expected
;  ;:col :row :size_x :size_y :id
;  {:col :whatever} truthy
;  {:row :whatever} truthy
;  {:size_x :whatever} truthy
;  {:size_y :whatever} truthy
;  {:id :whatever} truthy
;  {:invalid :whatever} falsey
;  )
;
;(tabular
;  (fact "about is panel predicate returned values" (keys (d/is-panel? ?decoration)) => (just ?expected))
;  ?decoration ?expected
;  ;:col :row :size_x :size_y :id
;  {:col :whatever} :col
;  {:row :whatever} :row
;  {:row :whatever} :row
;  {:size_x :whatever} :size_x
;  {:size_y :whatever} :size_y
;  {:id :whatever} :id
;  ;; Try pair also
;  {:col :whatever :row :whatever} #{:col :row}
;  )

(deftest about-with-visualizations
  (testing "Visualization without position"
    (is (= (cd/with-visualization "ID")
           {:id "ID" :type "visualization"})))
  (testing "Visualization with single position"
    (is (= (cd/with-visualization "ID" {:col 5})
           {:id "ID" :col 5 :type "visualization"})))
  (testing "Visualization with multiple positions"
    (is (= (cd/with-visualization "ID" {:col 5} {:row 1})
           {:id "ID" :col 5 :row 1 :type "visualization"}))))

(deftest about-dashboard
  (testing "Dashboard with title"
    (is (= (cd/dashboard "Dashboard")
           {:description "" :panelsJSON "[]" :title "Dashboard"})))
  (testing "Dashboard with title and description"
    (is (= (cd/dashboard "Dashboard" (cic/with-description "Description"))
           {:description "Description" :panelsJSON "[]" :title "Dashboard"})))
  (testing "Dashboard with option to not encode into json"
    (is (= (cd/dashboard "Dashboard" (cic/with-options :encode-json? false))
           {:description "" :panelsJSON [] :title "Dashboard"})))
  (testing "Dashboard with option to not encode into json and single valid visualization"
    (is (= (cd/dashboard "Dashboard"
                         (cic/with-options :encode-json? false)
                         (cd/with-visualization "V1" {:row 1}))
           {:description "" :panelsJSON [{:id "V1" :row 1 :type "visualization"}] :title "Dashboard"})))
  (testing "Dashboard with option to not encode into json and multiple valid visualization"
    (is (= (cd/dashboard "Dashboard"
                         (cic/with-options :encode-json? false)
                         (cd/with-visualization "V1" {:row 1})
                         (cd/with-visualization "V2" {:col 2}))
           {:description "" :panelsJSON [{:id "V1" :row 1 :type "visualization"} {:id "V2" :col 2 :type "visualization"}] :title "Dashboard"})))
  (testing "Dashboard with option to not encode into json and single completelly valid and single partially valid visualization"
    (is (= (cd/dashboard "Dashboard"
                         (cic/with-options :encode-json? false)
                         (cd/with-visualization "V1" {:row 1})
                         (cd/with-visualization "V2" {:invalid 2}))
           {:description "" :panelsJSON [{:id "V1" :row 1 :type "visualization"} {:id "V2" :type "visualization"}] :title "Dashboard"}))))

(deftest about-sizes
  (testing "Full width default"
    (is (= (cd/full-screen-wide)
           {:size_x 12 :size_y 3})))
  (testing "Full width"
    (is (= (cd/full-screen-wide 5)
           {:size_x 12 :size_y 5}))))