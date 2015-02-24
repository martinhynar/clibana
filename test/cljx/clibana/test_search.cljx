(ns clibana.test-search
  #+clj (:require [clibana.search :as s]
                  [clojure.test :refer :all])
  #+cljs (:require-macros [cemerick.cljs.test :refer (is deftest testing)])
  #+cljs (:require [cemerick.cljs.test :as t]
           [clibana.search :as s])
  )


(deftest about-with-columns
  (testing "single column"
    (is (= (s/with-columns "single")
           {:columns ["single"]})))
  (testing "multiple column"
    (is (= (s/with-columns "one" "two")
           {:columns ["one" "two"]})))
  )

(deftest about-columns?
  (testing "no column"
    (is (nil? (s/columns? [{}]))))
  (testing "single column"
    (is (= (s/columns? [{:columns ["col"]}])
           ["col"])))
  (testing "multiple column"
    (is (= (s/columns? [{:columns ["col"]} {:columns ["extra"]}])
           ["col"])))
  )

(deftest about-with-sort
  (testing "ascending order"
    (is (= (s/with-sort "col" :asc)
           {:sort ["col" "asc"]})))
  (testing "descending order"
    (is (= (s/with-sort "col" :desc)
           {:sort ["col" "desc"]})))
  (testing "mispelled order defaults to asc"
    (is (= (s/with-sort "col" :invalid)
           {:sort ["col" "asc"]})))
  )

(deftest about-sort?
  (testing "no sort"
    (is (nil? (s/sort? [{}]))))
  (testing "single column"
    (is (= (s/sort? [{:sort ["col" "asc"]}])
           ["col" "asc"])))
  (testing "multiple column"
    (is (= (s/sort? [{:sort ["col" "asc"]} {:sort ["extra" "asc"]}])
           ["col" "asc"])))
  )



(deftest about-search
  (testing "no sort"
    (is (= (s/search "Search"
                     (s/with-description "Description")
                     (s/with-columns "A" "B" "C")
                     (s/with-sort "B" :asc))
           {:title                 "Search"
            :description           "Description"
            :columns               ["A" "B" "C"]
            :sort                  ["B" "asc"],
            :kibanaSavedObjectMeta {:searchSourceJSON "null"},}))
    )
  )
