(ns clibana.test-url
  #+clj (:require [clibana.url :as cu]
                  [clibana.query :as cq]
                  [clojure.test :refer :all])
  #+cljs (:require-macros [cemerick.cljs.test :refer (is deftest testing)])
  #+cljs (:require [cemerick.cljs.test :as t]
           [clibana.url :as cu]
           [clibana.query :as cq])
  )


(deftest about-with-query
  (testing "simple query"
    (is (= {:query "query:(query_string:(query:'simple'))"}
           (cu/a-with-query {:query "simple"}))))
  (testing "with string query"
    (is (= {:query "query:(query_string:(query:'field%20%3A%20%22all%22'))"}
           (cu/a-with-query (cq/with-strings "field" "all")))))
  )
