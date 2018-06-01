(ns paint.core-test
  (:require [clojure.test :refer :all]
            [paint.core :refer :all]
            [paint.command :as command]))

(deftest line-test
  (testing "horizontal line"
    (is (= {[0 0] "x", [0 1] "x", [0 2] "x"}
           (line 0 0 2 0 "x")))

    (is (= {[3 1] "x", [3 2] "x", [3 3] "x", [3 4] "x", [3 5] "x", [3 6] "x", [3 7] "x"}
           (line 1 3 7 3 "x"))))

  (testing "vertical line"
    (is (= {[0 0] "x", [1 0] "x", [2 0] "x"}
           (line 0 0 0 2 "x")))

    (is (= {[1 7] "x", [2 7] "x", [3 7] "x"}
           (line 7 1 7 3 "x"))))

  (testing "diagonal line"
    (is (= {[0 0] "x", [1 1] "x", [2 2] "x"}
           (line 0 0 2 2 "x"))))

  (testing "input validation"
    (testing "x1 must be smaller or equal to x2"
      (is (thrown? IllegalArgumentException (line 5 5 0 0 "x"))))

    (testing "y1 must be smaller or equal to y2"
      (is (thrown? IllegalArgumentException (line 0 5 0 2 "x"))))))

(deftest rectangle-test
  (testing "rectangle"
    (is (= {[0 0] "x", [0 1] "x", [0 2] "x"
            [1 0] "x",            [1 2] "x"
            [2 0] "x", [2 1] "x", [2 2] "x"}
           (rectangle 0 0 2 2 "x")))))

(deftest create-canvas-test
  (testing "Create canvas 1x1"
    (let [canvas (create-canvas 1 1)]
      (is (= {:width 3
              :height 3
              [0 0] "-", [0 1] "-", [0 2] "-"
              [1 0] "|", [1 1] " ", [1 2] "|"
              [2 0] "-", [2 1] "-", [2 2] "-"}
             canvas))))

  (testing "Create canvas 3x2"
    (let [canvas (create-canvas 3 2)]
      (is (= {:width 5
              :height 4
              [0 0] "-", [0 1] "-", [0 2] "-", [0 3] "-", [0 4] "-"
              [1 0] "|", [1 1] " ", [1 2] " ", [1 3] " ", [1 4] "|"
              [2 0] "|", [2 1] " ", [2 2] " ", [2 3] " ", [2 4] "|"
              [3 0] "-", [3 1] "-", [3 2] "-", [3 3] "-", [3 4] "-"}
             canvas)))))

(deftest render-canvas-test
  (testing "Draw an empty canvas 1x1"
    (is (= ["---"
            "| |"
            "---"]
           (render-canvas (create-canvas 1 1)))))

  (testing "Draw an empty canvas 3x2"
    (is (= ["-----"
            "|   |"
            "|   |"
            "-----"]
           (render-canvas (create-canvas 3 2))))))

(deftest draw-line-test
  (testing "Draw a horizontal line on an empty canvas"
    (is (= ["-----"
            "|xx |"
            "|   |"
            "-----"]
           (-> (create-canvas 3 2)
               (draw-line 1 1 2 1)
               render-canvas))))

  (testing "Draw a vertical line on an empty canvas"
    (is (= ["-----"
            "| x |"
            "| x |"
            "-----"]
           (-> (create-canvas 3 2)
               (draw-line 2 1 2 2)
               render-canvas)))))

(deftest integration-test
  (testing "Create an empty canvas with a width: 20 and height: 5"
    (is (= ["----------------------"
            "|                    |"
            "|                    |"
            "|                    |"
            "|                    |"
            "|                    |"
            "----------------------"]
           (-> (create-canvas 20 5)
               render-canvas))))

  (testing "Draw a horizontal line from 1,3 to 7,3"
    (is (= ["----------------------"
            "|                    |"
            "|                    |"
            "|xxxxxxx             |"
            "|                    |"
            "|                    |"
            "----------------------"]
           (-> (create-canvas 20 5)
               (draw-line 1 3 7 3)
               render-canvas))))

  (testing "Draw a vertical line from 7,1 to 7,3"
    (is (= ["----------------------"
            "|      x             |"
            "|      x             |"
            "|xxxxxxx             |"
            "|                    |"
            "|                    |"
            "----------------------"]
           (-> (create-canvas 20 5)
               (draw-line 1 3 7 3)
               (draw-line 7 1 7 3)
               render-canvas))))

  (testing "Draw a horizontal line from 1,1 to 3,3"
    (is (= ["-----"
            "|x  |"
            "| x |"
            "|  x|"
            "-----"]
           (-> (create-canvas 3 3)
               (draw-line 1 1 3 3)
               render-canvas))))

  (testing "Draw a rectangle from 1,1 to 7,3"
    (is (= ["---------"
            "|xxxxxxx|"
            "|x     x|"
            "|xxxxxxx|"
            "---------"]
           (-> (create-canvas 7 3)
               (draw-rectangle 1 1 7 3)
               render-canvas))))

  (testing "Draw a rectangle from 15,2 to 20,5"
    (is (= ["----------------------"
            "|      x             |"
            "|      x       xxxxxx|"
            "|xxxxxxx       x    x|"
            "|              x    x|"
            "|              xxxxxx|"
            "----------------------"]
           (-> (create-canvas 20 5)
               (draw-line 1 3 7 3)
               (draw-line 7 1 7 3)
               (draw-rectangle 15 2 20 5)
               render-canvas)))))
