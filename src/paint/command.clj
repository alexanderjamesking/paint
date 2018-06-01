(ns paint.command)

(defn- create-canvas [[width height]]
  {:command :create-canvas
   :width (Integer/parseInt width)
   :height (Integer/parseInt height)})

(defn- draw-line [[x1 y1 x2 y2]]
  {:command :draw-line
   :x1 (Integer/parseInt x1)
   :y1 (Integer/parseInt y1)
   :x2 (Integer/parseInt x2)
   :y2 (Integer/parseInt y2)})

(defn- draw-rectangle [[x1 y1 x2 y2]]
  {:command :draw-rectangle
   :x1 (Integer/parseInt x1)
   :y1 (Integer/parseInt y1)
   :x2 (Integer/parseInt x2)
   :y2 (Integer/parseInt y2)})

(defn parse [command-str]
  (let [[command & args] (re-seq #"\w+" command-str)]
    (try
      (case command
        "C" (create-canvas args)
        "L" (draw-line args)
        "R" (draw-rectangle args)
        "Q" {:command :quit})
      (catch Exception e
        {:command :unknown}))))
