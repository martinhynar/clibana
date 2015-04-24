(ns clibana.internal.url-int)

(def units {:second  "s" :seconds "s"
            "second" "s" "seconds" "s"

            :minute  "m" :minutes "m"
            "minute" "m" "minutes" "m"

            :hour    "h" :hours "h"
            "hour"   "h" "hours" "h"

            :day     "d" :days "d"
            "day"    "d" "days" "d"

            :week    "w" :weeks "w"
            "week"   "w" "weeks" "w"

            :month   "M" :months "M"
            "month"  "M" "months" "M"

            :year    "y" :years "y"
            "year"   "y" "years" "y"

            nil      "m"})

(def refresh-units {:second  "second" :seconds "seconds"
                    "second" "second" "seconds" "seconds"

                    :minute  "minute" :minutes "minutes"
                    "minute" "minute" "minutes" "minutes"

                    :hour    "hour" :hours "hours"
                    "hour"   "hour" "hours" "hours"

                    :day     "day" :days "days"
                    "day"    "day" "days" "days"

                    nil      "seconds"})

(defn to-milliseconds [amount unit]
  (case unit
    "seconds" (* amount 1000)
    "minutes" (* amount 1000 60)
    "hours" (* amount 1000 60 60)
    "days" (* amount 1000 60 60 24)
    )
  )
