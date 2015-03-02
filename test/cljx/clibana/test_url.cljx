(ns clibana.test-url
  #+clj (:require [clibana.url :as u]
                  [clibana.query :as q]
                  [clojure.test :refer :all])
  #+cljs (:require-macros [cemerick.cljs.test :refer (is deftest testing)])
  #+cljs (:require [cemerick.cljs.test :as t]
           [clibana.url :as u]
           [clibana.query :as q])
  )


(deftest about-with-query
  (testing "simple query"
    (is (= {:query "query:(query_string:(query:'simple'))"}
           (u/a-with-query {:query "simple"}))))
  (testing "with string query"
    (is (= {:query "query:(query_string:(query:'field%20%3A%20%22all%22'))"}
           (u/a-with-query (q/with-string "field" "all")))))
  )
