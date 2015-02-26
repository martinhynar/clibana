(ns clibana.test-query
  #+clj (:require [clibana.query :as q]
                  [clojure.test :refer :all])
  #+cljs (:require-macros [cemerick.cljs.test :refer (is deftest testing)])
  #+cljs (:require [cemerick.cljs.test :as t]
           [clibana.query :as q])
  )


(deftest about-with-term
  (testing "single term"
    (is (= (q/with-term "field" "term")
           {:query "field : term"})))
  (testing "terms"
    (is (= (q/with-terms "field" ["termA" "termB"])
           {:query "field : (termA termB)"})))
  )

(deftest about-with-string
  (testing "single string"
    (is (= (q/with-string "field" "term")
           {:query "field : \"term\""})))
  (testing "strings"
    (is (= (q/with-strings "field" ["termA" "termB"])
           {:query "field : (\"termA\" \"termB\")"})))
  )

(deftest about-with-and
  (testing "2 queries"
    (is (= {:query "Q1 AND Q2"}
           (q/with-and "Q1" "Q2"))))
  (testing "more queries"
    (is (= {:query "Q1 AND Q2 AND Q3 AND Q4"}
           (q/with-and "Q1" "Q2" "Q3" "Q4"))))
  )

