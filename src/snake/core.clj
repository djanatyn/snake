(ns snake.core
  (:use snake.grid))

(defn random-world [size]
  "generate a new square grid with the snake and food placed randomly"
  (let [snake-loc [(rand-int size) (rand-int size)] food-loc [(rand-int size) (rand-int size)]]
    (if (= snake-loc food-loc) (random-world size)
        {:head snake-loc :tail (clojure.lang.PersistentQueue/EMPTY) :direction :north :food food-loc :size size})))

(defn turn
  "attempt to turn in a certain direction. if you can't, uhh, turn in a random direction please"
  [world direction]
  (let [space-to-move (map + (case direction :east [1 0] :west [-1 0] :north [0 -1] :south [0 1]) (:head world))]
    (if (valid? world space-to-move)
      (assoc world :direction direction)
      (turn world (case direction
         :west :north
         :north :east
         :east :south
         :south :west)))))

(defn face-food [world]
  "make the snake face the food (prioritizes moving on the x axis arbitrarily)"
  (if (nil? world) nil
      (let [food-x (first (:food world)) snake-x (first (:head world)) 
            food-y (second (:food world)) snake-y (second (:head world))]
        (cond
         (> food-x snake-x) (turn world :east)
         (< food-x snake-x) (turn world :west)
         (> food-y snake-y) (turn world :north)
         (< food-y snake-y) (turn world :south)
         true world))))

(defn move-food [world]
  "move the food after the snake collects it"
  (let [new-food [(rand-int (:size world)) (rand-int (:size world))]]
    (if (and (valid? world new-food) (not (= new-food (:head world))))
      (assoc world :food new-food)
      (move-food world))))

(defn tick [world]
  (if (nil? world) nil
      (let [new-food? (= (:head world) (:food world))
            new-head (adjacent world (:direction world))]
        (if (valid? world new-head)
          ((if new-food? move-food identity)
           (assoc world
             :head new-head
             :tail (conj ((if new-food? identity pop) (:tail world)) (:head world))))
          nil))))
