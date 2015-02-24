(ns clibana.internal.test-common
  #+clj (:require [clibana.internal.common :as c]
                  [clojure.test :refer :all])
  #+cljs (:require-macros [cemerick.cljs.test :refer (is deftest testing)])
  #+cljs (:require [cemerick.cljs.test :as t]
           [clibana.internal.common :as c])
  )

(deftest about-is-options
  (testing "about is options predicate"
    (testing "is options"
      (is (= (c/is-options? {:options "whatever"})
             "whatever")))
    (testing "is not options"
      (is (nil? (c/is-options? {:not-option "whatever"}))))
    ))

(deftest about-is-description
  (testing "about is description predicate"
    (testing "is description"
      (is (= (c/is-description? {:description "whatever"})
             "whatever")))
    (testing "is not description"
      (is (nil? (c/is-description? {:not-description "whatever"}))))
    ))

(deftest with-description
  (is (= (c/with-description "D") {:description "D"})))

(deftest with-options
  (is (= (c/with-options) {:options {}})))

(deftest about-options?
  (testing "no options"
    (is (nil? (c/options? []))))
  (testing "single option"
    (is (= (c/options? [{:options {:yes true}}])
           {:yes true})))
  (testing "multiple options"
    (is (= (c/options? [{:options {:yes true :false false}}])
           {:yes true :false false})))
  )


(deftest about-description?
  (testing "no description"
    (is (= (c/description? []) "")))

  (testing "single description"
    (is (= (c/description? [{:description "Description"}])
           "Description")))

  (testing "multiple descriptions"
    (is (= (c/description? [{:description "Shall be discarded"}
                            {:description "Description"}])
           "Description")))
  )
