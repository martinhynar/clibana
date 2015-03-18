(ns clibana.url
  (:require [cemerick.url :refer (url url-encode)]
            [clibana.internal.url-int :as ciu]
            [clibana.internal.common :as cic]))


(defn with-host
  ([protocol server port] {:host (str protocol "://" server ":" port)})
  ([protocol server] {:host (str protocol "://" server)})
  )

(defn with-id
  [id] {:id (url-encode id)})

;; Time
(defn with-time-relative
  ([amount unit]
   {:time (str "time:(from:now-" amount (unit ciu/units) ",mode:relative,to:now)")})
  ([amount unit round]
   {:time (str "time:(from:now-" amount (unit ciu/units) (when round (str (url-encode "/") (unit ciu/units))) ",mode:relative,to:now)")}))

(defn with-time-absolute [from to]
  {:time (str "time:(from:'" (url-encode from) "',mode:absolute,to:'" (url-encode to) "')")})


(defn with-g [& decorations]
  (let [t (cic/take-first :time decorations)]
    {:g (str "?_g=(" t ")")}))


(defn with-title
  [title] {:title (str "title=" (url-encode title))})

(defn a-with-query
  [query] {:query (str "query:(query_string:(query:'" (url-encode (:query query)) "'))")})

(defn with-a [& decorations]
  (let [t (cic/take-first :title decorations)
        query (cic/take-first :query decorations)]
    {:a (str "&_a=(" query ")")}))


(defn dashboard-url [& decorations]
  (let [host (cic/take-first :host decorations)
        id (cic/take-first :id decorations)
        g (cic/take-first :g decorations)
        a (cic/take-first :a decorations)]
    (str host "/#/dashboard/" id g a)
    ))