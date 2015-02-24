(ns clibana.internal.common)


(defn is-description? [decoration] (:description decoration))

(defn is-options? [decoration] (:options decoration))



(defn with-description [description] {:description description})

(defn with-options [& options] {:options (apply hash-map options)})

(defn description? [decorations] (get (apply merge (filter is-description? decorations)) :description ""))

(defn options? [decorations] (:options (apply merge (filter is-options? decorations))))

(defn gen-ids
  ([] (gen-ids 1))
  ([n] (cons n (lazy-seq (gen-ids (inc n))))))


(defn take-first [kw coll] (-> (filter kw coll) first kw))
