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
            :customFormats "{}"
            :fields "[]"}
           (ci/index-pattern pattern timefield))))
  )

(deftest index-pattern-with-typed-field
  (let [pattern "pattern"
        timefield "timefield"
        field-name "str_field"]
    (is (= {:title         pattern
            :timeFieldName "timefield"
            :customFormats "{}"
            :fields        "[{\"name\":\"str_field\",\"type\":\"string\",\"scripted\":false,\"indexed\":false,\"analyzed\":false,\"doc_values\":true}]"}
           (ci/index-pattern pattern timefield
                             (ci/with-string-field field-name)))))
  )

