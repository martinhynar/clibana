(ns clibana.test-index
  #+clj (:require [clibana.index :as ci]
                  [clojure.test :refer :all])
  #+cljs (:require-macros [cemerick.cljs.test :refer (is deftest)])
  #+cljs (:require [cemerick.cljs.test :as t]
           [clibana.index :as ci])
  )



(deftest index-pattern-with-pattern
  (let [pattern "pattern"]
    (is (= {:title         pattern
            :timeFieldName "@timestamp"
            :customFormats "{}"}
           (ci/index-pattern pattern))))
  )

(deftest index-pattern-with-pattern-and-timefield
  (let [pattern "pattern"
        timefield "timefield"]
    (is (= {:title         pattern
            :timeFieldName "timefield"
            :customFormats "{}"}
           (ci/index-pattern pattern timefield))))
  )