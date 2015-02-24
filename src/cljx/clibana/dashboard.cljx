(ns clibana.dashboard
  (:require
    [clibana.internal.common :as c]
    #+clj [clojure.data.json :as json]
    #+cljs [goog.json :as json]
    ))


(defn with-position-and-size [col row width height] {:col col :row row :size_x width :size_y height})

(defn with-position [col row] {:col col :row row})

(defn with-size [width height] {:size_x width :size_y height})

(defn wide [width] {:size_x width})

(defn high [height] {:size_y height})

(defn in-column [col] {:col col})

(defn in-row [row] {:row row})



;; Couple of usefull positioning and sizing aliases
(defn half-screen-wide
  ([height] (with-size 6 height))
  ([] (half-screen-wide 3))
  )

(defn is-panel? [decoration]
  (let [decoration (select-keys decoration [:col :row :size_x :size_y :id :type])]
    (if (empty? decoration) false decoration)))

(defn with-visualization [id & positions] (apply merge {:type "visualization" :id (if (keyword? id) (name id) id)} positions))

(defn dashboard
  ([title & decorations]
   (let [description (c/description? decorations)
         options (c/options? decorations)
         panels (filter map? (map is-panel? decorations))
         ]
     {:title       title
      :description description
      ;:hits 0
      :panelsJSON  (if (get options :encode-json? true)

                     #+clj (json/write-str (into [] panels))
                     #+cljs (json/serialize (clj->js (into [] panels)))
                     (into [] panels))
      }))
  )




(defn example-dashboard []
  (dashboard "Dashboard"
             (c/with-description "Description")
             (c/with-options :encode-json? false)
             (with-visualization "Visualization A" (half-screen-wide))
             (with-visualization "Visualization C" (half-screen-wide))
             (with-visualization "Visualization B" (half-screen-wide)))
  )




