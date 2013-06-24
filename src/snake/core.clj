(ns snake.core
  (:use snake.grid))

(defn random-world [size]
  "generate a new square grid with the snake and food placed randomly"
  (let [snake-loc [(rand-int size) (rand-int size)] food-loc [(rand-int size) (rand-int size)]]
    (if (= snake-loc food-loc) (random-world size)
        {:head snake-loc :tail (clojure.lang.PersistentQueue/EMPTY) :direction :north :food food-loc :boundary size})))

(defn face-food [world]
  (let [food-x (first (:food world)) snake-x (first (:head world)) 
        food-y (second (:food world)) snake-y (second (:head world))]
    (cond
     (> food-x snake-x) (assoc world :direction :east)
     (< food-x snake-x) (assoc world :direction :west)
     (> food-y snake-y) (assoc world :direction :north)
     (< food-y snake-y) (assoc world :direction :south)
     true world)))

;; main loop:
;; 1). check for collisions
;; 2). if there is a collision, flip out...
;; 3). ...unless it's food. in that case, make the food the head of the body
;; 4). generate a new food if the food is gone
;; 5). if you're not on food, remove the oldest part of the tail
;; 6). the head becomes the new block
