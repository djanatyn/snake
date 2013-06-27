(ns snake.path
  (:use snake.grid snake.core))



(defn distance [[x1 y1] [x2 y2]]
  (Math/sqrt (+ (Math/pow (- x2 x1) 2) (Math/pow (- y2 y1) 2))))

(defn start-path [world]
  (map (fn [cell] {:path [cell] :score (distance (:food world) cell)}) (neighbors world (:head world))))

(defn extend-path [path world]
  (map (fn [cell] {:path (cons cell (:path path)) :score (distance (:food world) cell)}) (neighbors world (first (:path path)))))

(defn current-best [paths]
  (apply (partial min-key :score) paths))

(defn step [world steps]
  (take steps (iterate #(current-best (extend-path % world)) (current-best (start-path world)))))

(defn neighbors
  "return the coordinates of all adjacent tiles"
  [grid [x y]]
  (let [delta-spaces [[-1 1] [0 1] [1 1] [-1 0] [1 0] [-1 -1] [0 -1] [1 -1]]
        surroundings (map #(map + [x y] %) delta-spaces)]
    (filter (partial valid? grid) surroundings)))

