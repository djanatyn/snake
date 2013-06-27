(ns snake.path
  (:use snake.grid snake.core))

(defn neighbors
  "return the coordinates of all adjacent tiles"
  [grid [x y]]
  (let [delta-spaces [[0 1] [-1 0] [1 0] [0 -1]]
        surroundings (map #(map + [x y] %) delta-spaces)]
    (filter (partial valid? grid) surroundings)))

(defn distance [[x1 y1] [x2 y2]]
  (Math/sqrt (+ (Math/pow (- x2 x1) 2) (Math/pow (- y2 y1) 2))))

(defn start-path [world]
  (set (map (fn [cell] {:path [cell] :score (distance (:food world) cell)}) (neighbors world (:head world)))))

(defn extend-path [path world]
  (let [new-spaces (neighbors world (first (:path path)))]
    (if (not= (:score path) 0)
      (if (empty? new-spaces) nil
        (map (fn [cell] {:path (cons cell (:path path)) :score (distance (:food world) cell)}) (neighbors world (first (:path path)))))
      path)))

(defn current-best [paths]
  (apply (partial min-key :score) paths))

(defn improve-paths [paths world]
  (let [best (current-best paths)
        new-paths (extend-path best world)]
    (flatten (conj (remove (partial = best) paths) new-paths))))

(defn walk-down-paths [world n]
  (last (take n (iterate #(improve-paths % world) (start-path world)))))
