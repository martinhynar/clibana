(ns clibana.test-query
  #+clj (:require [clibana.query :as cq]
                  [clojure.test :refer :all])
  #+cljs (:require-macros [cemerick.cljs.test :refer (is deftest testing)])
  #+cljs (:require [cemerick.cljs.test :as t]
           [clibana.query :as cq])
  )


(deftest about-with-terms
  (testing "single term"
    (is (= {:query "field : term"}
           (cq/with-terms "field" "term"))))
  (testing "terms"
    (is (= {:query "field : (termA, termB)"}
           (cq/with-terms "field" "termA" "termB"))))
  )

(deftest about-with-strings
  (testing "single string"
    (is (= {:query "field : \"string\""}
           (cq/with-strings "field" "string"))))
  (testing "strings"
    (is (= {:query "field : (\"stringA\", \"stringB\")"}
           (cq/with-strings "field" "stringA" "stringB"))))
  )

(deftest about-query
  (testing "single string"
    (is (= {:query "string"}
           (cq/with-query "string"))))
  (testing "star"
    (is (= {:query "*"}
           (cq/with-query "*"))))
  )

(deftest about-with-and
  (testing "2 queries"
    (is (= {:query "Q1 AND Q2"}
           (cq/with-and {:query "Q1"} {:query "Q2"}))))
  (testing "more queries"
    (is (= {:query "Q1 AND Q2 AND Q3 AND Q4"}
           (cq/with-and {:query "Q1"} {:query "Q2"} {:query "Q3"} {:query "Q4"}))))
  )

(deftest about-with-or
  (testing "2 queries"
    (is (= {:query "Q1 OR Q2"}
           (cq/with-or {:query "Q1"} {:query "Q2"}))))
  (testing "more queries"
    (is (= {:query "Q1 OR Q2 OR Q3 OR Q4"}
           (cq/with-or {:query "Q1"} {:query "Q2"} {:query "Q3"} {:query "Q4"}))))
  )

(deftest about-with-number-range
  (testing "non number"
    (is (nil? (cq/with-number-range "field" :inclusive "s" :inclusive "s"))))
  (testing "lower higher"
    (is (= {:query "field : [5 TO 6]"}
           (cq/with-number-range "field" :inclusive 5 :inclusive 6))))
  (testing "higher lower"
    (is (= {:query "field : [5 TO 6]"}
           (cq/with-number-range "field" :inclusive 6 :inclusive 5))))
  (testing "equal"
    (is (= {:query "field : [5 TO 5]"}
           (cq/with-number-range "field" :inclusive 5 :inclusive 5))))
  (testing "equal exclusive"
    (is (= {:query "field : {5 TO 5}"}
           (cq/with-number-range "field" :exclusive 5 :exclusive 5))))
  )

(deftest about-with-date-range
  (testing "first non date"
    (is (nil? (cq/with-date-range "field" :inclusive "non date" :inclusive "2015-12-12T12:12:12-03:00"))))
  (testing "second non date"
    (is (nil? (cq/with-date-range "field" :inclusive "2015-12-12T12:12:12-03:00" :inclusive "non date"))))

  (testing "increasing order with T exclusive"
    (is (= {:query "field : {\"2015-12-12T12:12:12-03:00\" TO \"2015-12-12T12:12:12+03:00\"}"}
           (cq/with-date-range "field" :exclusive "2015-12-12T12:12:12-03:00" :exclusive "2015-12-12T12:12:12+03:00"))))

  (testing "increasing order with T"
    (is (= {:query "field : [\"2015-12-12T12:12:12-03:00\" TO \"2015-12-12T12:12:12+03:00\"]"}
           (cq/with-date-range "field" :inclusive "2015-12-12T12:12:12-03:00" :inclusive "2015-12-12T12:12:12+03:00"))))
  (testing "increasing order with space"
    (is (= {:query "field : [\"2015-12-12 12:12:12-03:00\" TO \"2015-12-12 12:12:12+03:00\"]"}
           (cq/with-date-range "field" :inclusive "2015-12-12 12:12:12-03:00" :inclusive "2015-12-12 12:12:12+03:00"))))
  (testing "increasing order mixed"
    (is (= {:query "field : [\"2015-12-12T12:12:12-03:00\" TO \"2015-12-12 12:12:12+03:00\"]"}
           (cq/with-date-range "field" :inclusive "2015-12-12T12:12:12-03:00" :inclusive "2015-12-12 12:12:12+03:00"))))

  )

