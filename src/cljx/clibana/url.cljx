(ns clibana.url
  (:require [cemerick.url :refer (url url-encode)]
            [clibana.internal.url-int :as ciu]
            [clibana.internal.common :as cic]))

;; OPTIONS
(defn with-options
  "Specify sequence of options to drive shape of the result document."
  [& options] (apply cic/with-options options))

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

(defn with-refresh
  ([amount unit] (let [unit (get ciu/refresh-units unit "seconds")]
                   {:refresh (str "refreshInterval:(display:'" amount " " unit "',section:1,value:" (ciu/to-milliseconds amount unit) ")")}))
  ([amount] (with-refresh amount :seconds)))


(defn with-title
  [title] {:title (str "title:" (url-encode title))})

(defn with-query
  [query] {:query (str "query:(query_string:(query:'" (url-encode (:query query)) "'))")})


(defn dashboard-url [& decorations]
  (let [options (cic/<-options decorations)
        host (cic/take-first :host decorations)
        id (cic/take-first :id decorations)
        title (cic/take-first :title decorations)
        query (cic/take-first :query decorations)
        time (cic/take-first :time decorations)
        refresh (cic/take-first :refresh decorations)
        encode-json? (get options :encode-json? true)]

    (str host "/#/dashboard/" id (str "?_g=("
                                      refresh
                                      "," time ")") (str "&_a=(" query "," title ")"))
    ))
