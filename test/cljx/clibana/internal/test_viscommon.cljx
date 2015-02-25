(ns clibana.internal.test-viscommon
  #+clj (:require [clibana.internal.viscommon :as vc]
                  [clibana.visualization :as v]
                  [clojure.test :refer :all])
  #+cljs (:require-macros [cemerick.cljs.test :refer (is deftest testing)])
  #+cljs (:require [cemerick.cljs.test :as t]
           [clibana.internal.viscommon :as vc]
           [clibana.visualization :as v])
  )

(deftest about-resolve-params

  (testing "area-params"
    (is (= {:addLegend       true
            :addTooltip      true
            :shareYAxis      true
            :defaultYExtents false
            :mode            "percentage"}
           (vc/resolve-params {:addLegend       true
                               :addTooltip      true
                               :shareYAxis      true
                               :defaultYExtents false
                               :mode            "stacked"}
                              vc/area-params {:mode :percentage}))))

  )