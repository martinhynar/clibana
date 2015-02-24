(ns clibana.test-query
  #+clj (:require [clibana.query :as q]
                  [clojure.test :refer :all])
  #+cljs (:require-macros [cemerick.cljs.test :refer (is deftest testing)])
  #+cljs (:require [cemerick.cljs.test :as t]
           [clibana.query :as q])
  )


(deftest about-search
  (testing "single term"
    (is (= (q/query "index-*"
                    (q/with-term "field" "term"))
           {:search {:index  "index-*"
                     :filter []
                     :query  "field : term"}})))
  (testing "single string"
    (is (= (q/query "index-*"
                    (q/with-string "field" "term"))
           {:search {:index  "index-*"
                     :filter []
                     :query  "field : \"term\""}})))
  (testing "terms"
    (is (= (q/query "index-*"
                    (q/with-terms "field" ["termA" "termB"]))
           {:search {:index  "index-*"
                     :filter []
                     :query  "field : (termA termB)"}})))
  (testing "strings"
    (is (= (q/query "index-*"
                    (q/with-strings "field" ["termA" "termB"]))
           {:search {:index  "index-*"
                     :filter []
                     :query  "field : (\"termA\" \"termB\")"}})))
  )

;
;(testing "about search"
;       (testing "search with no filter"
;             (let [index "build-123" query "service : cpu#1 steal"]
;               (q/with-search :index index :query query) => {:search {:index  index
;                                                                      :query  {:query_string {:query query}}
;                                                                      :filter []}})
;             )
;       )
;
