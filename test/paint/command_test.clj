(ns paint.command-test
  (:require [paint.command :as command]
            [clojure.test :refer :all]))

(deftest parse-command-test
  (testing "create-canvas"
    (is (= {:command :create-canvas :width 20 :height 5}
           (command/parse "C 20 5")))

    (testing "invalid parameters passed"
      (is (= {:command :unknown}
             (command/parse "C f 5")))))

  (testing "draw-line"
    (is (= {:command :draw-line
            :x1 1
            :y1 3
            :x2 7
            :y2 3}
           (command/parse "L 1 3 7 3"))))

  (testing "draw-rectangle"
    (is (= {:command :draw-rectangle
            :x1 15
            :y1 2
            :x2 20
            :y2 5}
           (command/parse "R 15 2 20 5"))))

  (testing "quit"
    (is (= {:command :quit} (command/parse "Q"))))

  (testing "first part of command not matched"
    (is (= {:command :unknown} (command/parse "U")))))
