(ns paint.main
  (:require [paint.command :as command]
            [paint.core :as p]))

;; default canvas is 20x5
(def canvas (atom (p/create-canvas 20 5)))

(defn- print-instructions! []
  (println "Valid commands:")
  (println "C width height (C 20 5 - Create a canvas with a width of 20 and height of 5)")
  (println "L x1 y1 x2 y2 (L 1 3 7 3 - Draw a line from 1,3 to 7,3)")
  (println "R x1 y1 x2 y2 (R 15 2 20 5 - Draw a rectangle from 15,2 to 20,5")
  (println "Q (Quit"))

(defn- create-canvas! [{:keys [width height]}]
  (reset! canvas (p/create-canvas width height)))

(defn- draw-line! [{:keys [x1 y1 x2 y2]}]
  (swap! canvas p/draw-line x1 y1 x2 y2))

(defn- draw-rectangle! [{:keys [x1 y1 x2 y2]}]
  (swap! canvas p/draw-rectangle x1 y1 x2 y2))

(defn- safely-execute-command! [f command]
  (try
    (f command)
    (catch IllegalArgumentException e
      (println "Input error: " (.getMessage e))
      (print-instructions!))))

(defn- quit! []
  (System/exit 0))

(defn read-command! []
  (println "enter command:")
  (let [input (read-line)
        command (command/parse input)]
    (case (:command command)
      :create-canvas (safely-execute-command! create-canvas! command)
      :draw-line (safely-execute-command! draw-line! command)
      :draw-rectangle (safely-execute-command! draw-rectangle! command)
      :quit (quit!)
      :unknown (do (println "Unable to parse command " input)
                   (print-instructions!)
                   (read-command!)))
    (p/print-canvas @canvas)
    (read-command!)))

(defn -main [& args]
  (print-instructions!)
  (read-command!))
