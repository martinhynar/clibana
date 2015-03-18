(ns clibana.internal.test-visualization_int
  #+clj (:require [clibana.internal.visualization_int :as civ]
                  [clojure.test :refer :all])
  #+cljs (:require-macros [cemerick.cljs.test :refer (is deftest testing)])
  #+cljs (:require [cemerick.cljs.test :as t]
           [clibana.internal.visualization_int :as civ])
  )

(deftest about-resolve-params

  (testing "area-params"
    (is (= {:addLegend       true
            :addTooltip      true
            :shareYAxis      true
            :defaultYExtents false
            :mode            "percentage"}
           (civ/resolve-params {:addLegend       true
                                :addTooltip      true
                                :shareYAxis      true
                                :defaultYExtents false
                                :mode            "stacked"}
                               civ/area-params {:mode :percentage}))))

  )