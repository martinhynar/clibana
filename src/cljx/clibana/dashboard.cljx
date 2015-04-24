(ns clibana.dashboard
  (:require
    [clibana.internal.common :as cic]
    #+clj [clojure.data.json :as json]
    #+cljs [goog.json :as json]
    ))


;; OPTIONS
(defn with-options
  "Specify sequence of options to drive shape of the result document."
  [& options] (cic/with-options options))

;; STORABLE ELASTICSEARCH ID
(defn get-document-id
  "Creates string that could be used as ElasticSearch document id.
  Parameter 'dashboard' could be
  string - will be slugified to replace invalid characters
  search map - (produced by clibana.dashboard/dashboard) from which its title will be taken and slugified"
  [dashboard]
  (cond
    (string? dashboard) (cic/as-elastic-id dashboard)
    (map? dashboard) (cic/as-elastic-id (:title dashboard))))

;; DESCRIPTION
(defn with-description
  "Give your dashboard some human readable description."
  [description] (cic/with-description description))


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

(defn with-visualization [visualization & positions]
  (apply merge {:type "visualization"
                :id   (cond
                        (string? visualization) visualization
                        (map? visualization) (cic/as-elastic-id (:title visualization)))}
         positions))

(defn with-search [id & positions]
  (apply merge {:type "search"
                :id   (if (keyword? id) (name id) id)}
         positions))

(defn dashboard
  ([title & decorations]
   (let [description (cic/<-description decorations)
         options (cic/<-options decorations)
         panels (filter map? (map is-panel? decorations))
         ]
     {:title       title
      :description description
      :panelsJSON  (if (get options :encode-json? true)

                     #+clj (json/write-str (into [] panels))
                     #+cljs (json/serialize (clj->js (into [] panels)))
                     (into [] panels))
      }))
  )




(comment
  (defn example-dashboard []
    (dashboard "Dashboard"
               (with-description "Description")
               (with-options :encode-json? false)
               (with-visualization "Visualization A" (half-screen-wide))
               (with-visualization "Visualization C" (half-screen-wide))
               (with-visualization "Visualization B" (half-screen-wide)))
    ))




