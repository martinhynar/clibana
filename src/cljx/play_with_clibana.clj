(ns play-with-clibana
  (:require [clibana.search :as cs]
            [clibana.query :as cq]
            [clibana.url :as cu]
            [clibana.visualization :as cv]
            [clojurewerkz.elastisch.native :as es]
            [clojurewerkz.elastisch.native.index :as esi]
            [clojurewerkz.elastisch.query :as esq]
            [clojurewerkz.elastisch.native.document :as esd]))

(def client (atom nil))

(defn init-es []
  (reset! client (es/connect [["127.0.0.1" 9300]]
                             {"cluster.name" "elasticsearch"})))


(defn delete-all-kibana-documents []
  (esd/delete-by-query-across-all-types @client ".kibana" (esq/match-all)))

(defn delete-all-kibana-search-documents []
  (esd/delete-by-query @client ".kibana" "search" (esq/match-all)))


(defn store-search [search-doc]
  (esd/create @client ".kibana" "search" search-doc))



(defn example []
  )




(comment
  ;http:// localhost:5601/# / dashboard/Dash1?
  ;_g= (refreshInterval: (display:'5%20seconds', section:1, value:5000), time: (from:now-15m, mode:quick, to:now))
  ;&
  ;_a= (filters:! (), panels:! ((col:1, id:Viz1, row:1, size_x:3, size_y:2, type:visualization)),, title:Dash1)
  )

(defn url-example []
  (cu/dashboard-url
    (cu/with-host " http " " localhost " 5601)
    (cu/with-title " Dashboard Title ")
    (cu/with-time-relative 5 :hour)
    (cu/with-id " Dashboard ID ")                           ; This shall be mandatory
    (cu/with-query (clibana.query/with-terms :field " term "))
    )
  )

(def search (cs/search "A search"))

(defn visualization-example []
  (cv/visualization "An Example"
                 (cv/with-description "This visualization shows ...")
                 (cv/with-options :encode-json? false)
                 (cv/with-bar-chart
                   (cv/chart-with-parameter :mode :stacked)
                   (cv/chart-with-parameter :spyPerPage 10)
                   (cv/chart-with-Y-aggregation :max :field "metric")
                   (cv/chart-with-X-aggregation :date-histogram "@timestamp")
                   (cv/chart-with-X-aggregation :terms "service"))
                 (cv/with-saved-search search))
  )