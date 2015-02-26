(ns clibana.internal.date
  #+clj (:import (java.text SimpleDateFormat ParseException))
  )

#+clj
(defn- rfc3339 [date-string middle-char]
  (let [rfc3339 (SimpleDateFormat. (str "yyyy-MM-dd'" middle-char "'HH:mm:ssX"))]
    (try
      ;; If this passes, it shall be valid date
      (.parse rfc3339 date-string)
      date-string
      (catch ParseException _ false)
      )
    )
  )

#+cljs
(defn- rfc3339 [date-string _]
  (try
    ;; If this passes, it shall be valid date
    (.toISOString (js/Date. date-string))
    date-string
    (catch js/Error _ false)))

(defn rfc3339-with-T [date-string]
  (rfc3339 date-string "T")
  )

(defn rfc3339-with-space [date-string]
  (rfc3339 date-string " ")
  )

(defn is-date? [date-string]
  (or (rfc3339-with-T date-string) (rfc3339-with-space date-string)))