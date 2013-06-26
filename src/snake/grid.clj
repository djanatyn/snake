(ns snake.grid)

;; grid helper functions
;; ---------------------

(defn valid? 
  "determine if a space is valid"
  [size [x y]]
  (and (>= x 0) (>= y 0) (< x size) (< y size)))

(defn get-tile 
  "return the value of a square on a grid"
  [grid [x y]]
  (nth (nth grid y) x))

(defn adjacent [world direction]
  (map + (case direction :east [1 0] :west [-1 0] :north [0 1] :south [0 -1]) (:head world)))

(defn coords
  "return all the possible coordinates with a certain width and height"
  [width height]
  (mapcat (fn [x] (map (fn [y] [x y]) (range height))) (range width)))

(defn empty-grid [width height]
  (repeat height (repeat width '.)))

