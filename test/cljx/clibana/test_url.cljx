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
           (cu/with-query {:query "simple"}))))
  (testing "with string query"
    (is (= {:query "query:(query_string:(query:'field%20%3A%20%22all%22'))"}
           (cu/with-query (cq/with-strings "field" "all")))))
  )

(deftest about-with-refresh
  (testing "Refresh in default unit"
    (is (= {:refresh "refreshInterval:(display:'6 seconds',section:1,value:6000)"}
           (cu/with-refresh 6))))
  (testing "Refresh in minutes"
    (is (= {:refresh "refreshInterval:(display:'5 minutes',section:1,value:300000)"}
           (cu/with-refresh 5 :minutes))))
  (testing "Refresh in hours"
    (is (= {:refresh "refreshInterval:(display:'2 hours',section:1,value:7200000)"}
           (cu/with-refresh 2 :hours))))
  (testing "Refresh in days"
    (is (= {:refresh "refreshInterval:(display:'2 days',section:1,value:172800000)"}
           (cu/with-refresh 2 :days)))))


(deftest about-dashboard-url
  (testing "complete url"
    (is (=
          (str "https://clibana.test:4545/#/dashboard/An-ID?"
               "_g=("
               "refreshInterval:(display:'10 seconds',section:1,value:10000),"
               "time:(from:now-5h,mode:relative,to:now)"
               ")&"
               "_a=("
               "query:(query_string:(query:'field%20%3A%20%22all%22')),"
               "title:A%20Title"
               ")")
          (cu/dashboard-url
            ;(cu/with-options :encode-json? false)
            (cu/with-host "https" "clibana.test" 4545)
            (cu/with-id "An-ID")
            (cu/with-time-relative 5 :hours)
            (cu/with-refresh 10 :seconds)
            (cu/with-title "A Title")
            (cu/with-query (cq/with-strings "field" "all"))))))
  )


