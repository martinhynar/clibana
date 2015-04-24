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
    (is (= {:columns ["single"]}
           (cs/with-columns "single"))))
  (testing "multiple column"
    (is (= {:columns ["one" "two"]}
           (cs/with-columns "one" "two"))))
  )

(deftest about-with-sort
  (testing "ascending order"
    (is (= {:sort ["col" "asc"]}
           (cs/with-sort "col" :asc))))
  (testing "descending order"
    (is (= {:sort ["col" "desc"]}
           (cs/with-sort "col" :desc))))
  (testing "mispelled order defaults to asc"
    (is (= {:sort ["col" "asc"]}
           (cs/with-sort "col" :invalid))))
  )

(deftest about-search
  (testing "no sort"
    (is (= {:title                 "Search"
            :description           "Description"
            :columns               ["A" "B" "C"]
            :sort                  ["B" "asc"],
            :kibanaSavedObjectMeta {:searchSourceJSON (str "{\"index\":\"index-*\",\"highlight\":{\"pre_tags\":[\"@kibana-highlighted-field@\"],\"post_tags\":[\""
                                                           #+cljs "@/kibana-highlighted-field@"
                                                           #+clj "@\\/kibana-highlighted-field@"
                                                           "\"],\"fields\":{\"*\":{}}},\"filter\":[],\"query\":{\"query_string\":{\"analyze_wildcard\":true,\"query\":\"A : term\"}}}")}}
           (cs/search "Search"
                      (cs/with-description "Description")
                      (cs/with-columns "A" "B" "C")
                      (cs/with-index-pattern "index-*")
                      (cs/with-sort "B" :asc)
                      (cq/with-terms "A" "term")))))
  )