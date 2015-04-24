(ns clibana.test-search
  #+clj (:require [clibana.search :as cs]
                  [clibana.query :as cq]
                  [clojure.test :refer :all])
  #+cljs (:require-macros [cemerick.cljs.test :refer (is deftest testing)])
  #+cljs (:require [cemerick.cljs.test :as t]
           [clibana.search :as cs]
           [clibana.query :as cq])
  )


(deftest about-with-columns
  (testing "single column"
    (is (= (cs/with-columns "single")
           {:columns ["single"]})))
  (testing "multiple column"
    (is (= (cs/with-columns "one" "two")
           {:columns ["one" "two"]})))
  )

(deftest about-with-sort
  (testing "ascending order"
    (is (= (cs/with-sort "col" :asc)
           {:sort ["col" "asc"]})))
  (testing "descending order"
    (is (= (cs/with-sort "col" :desc)
           {:sort ["col" "desc"]})))
  (testing "mispelled order defaults to asc"
    (is (= (cs/with-sort "col" :invalid)
           {:sort ["col" "asc"]})))
  )

(deftest about-search
  (testing "no sort"
    (is (= (cs/search "Search"
                      (cs/with-description "Description")
                      (cs/with-columns "A" "B" "C")
                      (cs/with-index-pattern "index-*")
                      (cs/with-sort "B" :asc)
                      (cq/with-terms "A" "term"))
           {:title                 "Search"
            :description           "Description"
            :columns               ["A" "B" "C"]
            :sort                  ["B" "asc"],
            :kibanaSavedObjectMeta {:searchSourceJSON "{\"index\":\"index-*\",\"highlight\":{\"pre_tags\":[\"@kibana-highlighted-field@\"],\"post_tags\":[\"@\\/kibana-highlighted-field@\"],\"fields\":{\"*\":{}}},\"filter\":[],\"query\":{\"query_string\":{\"analyze_wildcard\":true,\"query\":\"A : term\"}}}"}}))
    )
  )