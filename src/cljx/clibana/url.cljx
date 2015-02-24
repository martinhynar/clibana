(ns clibana.url
  (:require [cemerick.url :refer (url url-encode)]))


(defn take-first [kw coll] (-> (filter kw coll) first kw))

(defn with-host
  ([protocol server port] {:host (str protocol "://" server ":" port)})
  ([protocol server] {:host (str protocol "://" server)})
  )

(defn with-id
  [id] {:id (url-encode id)})

;; Time
(def units {:seconds "s" :minutes "m"
            :hours   "h" :days "d"
            :weeks   "w" :months "M"
            :years   "y" nil "m"})

(defn with-time-relative
  ([amount unit]
   {:time (str "time:(from:now-" amount (unit units) ",mode:relative,to:now)")})
  ([amount unit round]
   {:time (str "time:(from:now-" amount (unit units) (when round (str (url-encode "/") (unit units))) ",mode:relative,to:now)")}))

(defn with-time-absolute [from to]
  {:time (str "time:(from:'" (url-encode from) "',mode:absolute,to:'" (url-encode to) "')")})


(defn with-g [& decorations]
  (let [t (take-first :time decorations)]
    {:g (str "?_g=(" t ")")}))


(defn with-title
  [title] {:title (str "title=" (url-encode title))})

(defn with-a [& decorations]
  (let [t (take-first :title decorations)]
    {:a (str "&_a=(" t ")")}))


(defn dashboard-url [& decorations]
  (let [host (take-first :host decorations)
        id (take-first :id decorations)
        g (take-first :g decorations)
        a (take-first :a decorations)]
    (str host "/#/dashboard/" id g a)
    )

  )


(defn example []
  (dashboard-url
    ;; http://localhost:5601/#/dashboard
    (with-host "http" "localhost" "5601")
    ;; http://localhost:5601/#/dashboard/999999%20::%20A
    (with-id "999999 :: A")
    (with-g
      (with-time-relative 5 :days))
    (with-a
      ())

    )
  )
;
;(defn h [] (-> (url "http://localhost:5601/" "#" "dashboard" (url-encode (str (:tcbuildid build) " :: " (:name kd))))
;               (assoc :query (str "_g=(time:(from:'" (url-encode (:started build)) "',mode:absolute,to:'" (url-encode (get build :finished "now")) "'))"))
;               (str)))
;;
;http://localhost:5601/#/dashboard/999999%20::%20JJJ?
;  _g=(time:(from:now-1M%2FM,mode:relative,to:now))&
;  _a=(filters:!(),
;      panels:!((col:1,id:'999999%20CPU%200',row:1,size_x:6,size_y:3,type:visualization),
;               (col:7,id:'999999%20CPU%201',row:1,size_x:6,size_y:3,type:visualization)),
;      query:(query_string:(analyze_wildcard:!t,query:'*')),
;      title:'999999%20::%20JJJ')

;
;http://localhost:5601/#/dashboard/CPU-1-Dash?_a=(filters:!(),panels:!((col:1,id:CPU-1-iowait-viz,row:1,size_x:3,size_y:2,type:visualization),(col:4,id:CPU-0-iowait-viz,row:1,size_x:3,size_y:2,type:visualization)),query:(query_string:(analyze_wildcard:!t,query:'*')),title:'CPU%201%20Dash')&_g=(time:(from:'2015-02-19T20:58:22.885Z',mode:absolute,to:'2015-02-19T20:59:39.000Z'))
;http://localhost:5601/#/dashboard/CPU-1-Dash?_a=(filters:!(),panels:!((id:CPU-1-iowait-viz,type:visualization),(id:CPU-0-iowait-viz,type:visualization)),query:(query_string:(analyze_wildcard:!t,query:'*')))&_g=(time:(from:'2015-02-19T20:58:22.885Z',mode:absolute,to:'2015-02-19T20:59:39.000Z'))
;http://localhost:5601/#/dashboard/CPU-1-Dash?_g=(time:(from:'2015-02-19T20:58:22.885Z',mode:absolute,to:'2015-02-19T20:59:39.000Z'))
;http://localhost:5601/#/dashboard/CPU-1-Dash?_a=(query:(query_string:(analyze_wildcard:!t,query:'service%20:%20(%22cpu%231%20iowait%22%20%22cpu%230%20system%22)')))&_g=(time:(from:'2015-02-19T20:58:22.885Z',mode:absolute,to:'2015-02-19T20:59:39.000Z'))