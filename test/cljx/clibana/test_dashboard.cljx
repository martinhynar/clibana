(ns clibana.test-dashboard
  #+clj (:require [clibana.internal.common :as c]
                  [clibana.dashboard :as d]
                  [clojure.test :refer :all])
  #+cljs (:require-macros [cemerick.cljs.test :refer (is deftest testing)])
  #+cljs (:require [cemerick.cljs.test :as t]
           [clibana.dashboard :as d]
           [clibana.internal.common :as c])
  )


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
    (is (= (d/with-visualization "ID")
           {:id "ID" :type "visualization"})))
  (testing "Visualization with single position"
    (is (= (d/with-visualization "ID" {:col 5})
           {:id "ID" :col 5 :type "visualization"})))
  (testing "Visualization with multiple positions"
    (is (= (d/with-visualization "ID" {:col 5} {:row 1})
           {:id "ID" :col 5 :row 1 :type "visualization"})))

  )

(deftest about-dashboard
  (testing "Dashboard with title"
    (is (= (d/dashboard "Dashboard")
           {:description "" :panelsJSON "[]" :title "Dashboard"})))
  (testing "Dashboard with title and description"
    (is (= (d/dashboard "Dashboard" (c/with-description "Description"))
           {:description "Description" :panelsJSON "[]" :title "Dashboard"})))
  (testing "Dashboard with option to not encode into json"
    (is (= (d/dashboard "Dashboard" (c/with-options :encode-json? false))
           {:description "" :panelsJSON [] :title "Dashboard"})))
  (testing "Dashboard with option to not encode into json and single valid visualization"
    (is (= (d/dashboard "Dashboard"
                        (c/with-options :encode-json? false)
                        (d/with-visualization "V1" {:row 1}))
           {:description "" :panelsJSON [{:id "V1" :row 1 :type "visualization"}] :title "Dashboard"})))
  (testing "Dashboard with option to not encode into json and multiple valid visualization"
    (is (= (d/dashboard "Dashboard"
                        (c/with-options :encode-json? false)
                        (d/with-visualization "V1" {:row 1})
                        (d/with-visualization "V2" {:col 2}))
           {:description "" :panelsJSON [{:id "V1" :row 1 :type "visualization"} {:id "V2" :col 2 :type "visualization"}] :title "Dashboard"})))
  (testing "Dashboard with option to not encode into json and single completelly valid and single partially valid visualization"
    (is (= (d/dashboard "Dashboard"
                        (c/with-options :encode-json? false)
                        (d/with-visualization "V1" {:row 1})
                        (d/with-visualization "V2" {:invalid 2}))
           {:description "" :panelsJSON [{:id "V1" :row 1 :type "visualization"} {:id "V2" :type "visualization"}] :title "Dashboard"})))
  )