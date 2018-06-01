(ns paint.core)

(defn validate-inputs! [x1 y1 x2 y2]
  (when (> x1 x2)
    (throw (IllegalArgumentException. "x2 cannot be greater than x1")))
  (when (> y1 y2)
    (throw (IllegalArgumentException. "y2 cannot be greater than y1"))))

(defn line
  "Draw a new line from coordinates (x1, y1) to (x2, y2)
  horizontally or vertically"
  [x1 y1 x2 y2 fill]
  (validate-inputs! x1 y1 x2 y2)
  (loop [x x1
         y y1
         coordinates {}]
    (let [coordinates (assoc coordinates [y x] fill)]
      (if (and (= x x2)
               (= y y2))
        coordinates
        (let [next-x (if (< x x2) (inc x) x)
              next-y (if (< y y2) (inc y) y)]
          (recur next-x next-y coordinates))))))

(defn rectangle
  "Draw a new rectangle, with upper left corner at coordinate (x1, y1)
  and lower right coordinate at (x2, y2)"
  [x1 y1 x2 y2 fill]
  (let [top-line    (line x1 y1 x2 y1 fill)
        bottom-line (line x1 y2 x2 y2 fill)
        left-line   (line x1 y1 x1 y2 fill)
        right-line  (line x2 y1 x2 y2 fill)]
    (merge top-line bottom-line left-line right-line)))

(defn- top-border
  [{:keys [width]}]
  (line 0 0 (dec width) 0 "-"))

(defn- bottom-border
  [{:keys [width height]}]
  (line 0 (dec height) (dec width) (dec height) "-"))

(defn- left-border
  [{:keys [height]}]
  (line 0 1 0 (- height 2) "|"))

(defn- right-border
  [{:keys [width height]}]
  (line (- width 1) 1 (- width 1) (- height 2) "|"))

(defn- empty-canvas [{:keys [width height]}]
  (into {} (for [x (range height)
                 y (range width)]
             {[x y] " "})))

(defn create-canvas [width height]
  (let [canvas {:width (+ 2 width)
                :height (+ 2 height)}]
    (merge canvas
           (empty-canvas canvas)
           (top-border canvas)
           (bottom-border canvas)
           (left-border canvas)
           (right-border canvas))))

(defn draw-line [canvas x1 y1 x2 y2]
  (merge canvas (line x1 y1 x2 y2 "x")))

(defn draw-rectangle [canvas x1 y1 x2 y2]
  (merge canvas (rectangle x1 y1 x2 y2 "x")))

(defn render-canvas [canvas]
  (for [row (range (:height canvas))]
    (reduce str (for [column (range (:width canvas))]
                  (get canvas [row column])))))

(defn print-canvas [canvas]
  (doseq [line (render-canvas canvas)]
    (println line)))
