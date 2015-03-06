(ns clibana.sample.url-dashboard
  (:require
    [clibana.url :as cu]
    [clibana.query :as cq]))

;; Link to the dashboard
(cu/dashboard-url
  ;; Where is Kibana listening
  (cu/with-host "http" "localhost" "5601")
  ;; Use ID of dashboard to search ElasticSearch
  (cu/with-id "CPU :: from collectd")
  ;; define "global parameters"
  (cu/with-g
    ;; Display time frame between now and 5 days ago
    (cu/with-time-relative 5 :days))
  ;; Define artifacts
  (cu/with-a
    (cu/a-with-query (cq/with-terms "service" ["termA" "termB"]))))
