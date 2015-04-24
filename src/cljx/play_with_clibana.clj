(ns play-with-clibana
  (:require [clibana.search :as cs]
            [clibana.query :as cq]
            [clojurewerkz.elastisch.native :as es]
            [clojurewerkz.elastisch.native.index :as esi]
            [clojurewerkz.elastisch.query :as esq]
            [clojurewerkz.elastisch.native.document :as esd]))

(def client (atom nil))

(defn init-es []
  (reset! client (es/connect [["127.0.0.1" 9300]]
                             {"cluster.name" "elasticsearch"})))

(defn store-search [search-doc]
  (esd/create @client ".kibana" "search" search-doc))

(defn delete-all-search-documents []
  (esd/delete-by-query @client ".kibana" "search" (esq/match-all)))



(defn example []
  (cs/search "Test Search"
             (cs/with-description "Match all documents with service foo.")
             (cs/with-columns "service" "metric")
             (cs/with-sort "metric" :asc)
             (cs/with-index-pattern "test")
             (cq/with-terms "service" "foo")
             ))