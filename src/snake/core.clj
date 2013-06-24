(ns snake.core
  (:use snake.grid))

(def snake {:head [5 5] :tail (clojure.lang.PersistentQueue/EMPTY) :direction :south})

(defn random-world [height width]
  (let [snake-loc [(rand-int width) (rand-int height)] food-loc [(rand-int width) (rand-int height)]]
    (if (= snake-loc food-loc) (random-world height width) {:head snake-loc :tail (clojure.lang.PersistentQueue/EMPTY) :direction :north :food food-loc})))

(defn face-food [world]
  (let [food-x (first (:food world)) snake-x (first (:head world)) 
        food-y (second (:food world)) snake-y (second (:head world))]
    (cond
     (> food-x snake-x) (assoc world :direction :east)
     (< food-x snake-x) (assoc world :direction :west)
     (> food-y snake-y) (assoc world :direction :north)
     (< food-y snake-y) (assoc world :direction :south))))

;; main loop:
;; 1). check for collisions
;; 2). if there is a collision, flip out...
;; 3). ...unless it's food. in that case, make the food the head of the body
;; 4). generate a new food if the food is gone
;; 5). if you're not on food, remove the oldest part of the tail
;; 6). the head becomes the new block
