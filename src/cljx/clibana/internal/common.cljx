(ns clibana.internal.common)



(defn take-first
  "
 From collection of maps, takes all that rhave given key, selects first and returns its value.
 E.g. (<-saved-search [{:saved-search 1} {:saved-search 2}]) -> 1
 "
  ([kw coll default] (get (first (filter kw coll)) kw default))
  ([kw coll] (-> (filter kw coll) first kw)))


(defn <-description [decorations] (take-first :description decorations ""))

(defn <-options [decorations] (:options (apply merge (filter :options decorations))))

(defn <-visualization [decorations] (take-first :visualization decorations))

(defn <-search [decorations] (take-first :search decorations))

(defn <-saved-search [decorations] (take-first :saved-search decorations))

(defn <-param [decorations] (apply merge (map :param (filter :param decorations))))

(defn <-query [decorations] (take-first :query decorations))

(defn <-filter [decorations] (take-first :filter decorations))


;; TODO I don't know yet what are listeners for
;(defn <-listener [decorations] (filter :listener decorations))
(defn <-listener [decorations] {})

(defn <-aggregation [decorations] (remove nil? (map :aggregation decorations)))



;; Common with-* decorators
(defn with-description [description] {:description description})

(defn with-options [& options] {:options (apply hash-map options)})



(defn gen-ids
  ([] (gen-ids 1))
  ([n] (cons n (lazy-seq (gen-ids (inc n))))))
