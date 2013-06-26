(ns snake.render
  (:import java.awt.image.BufferedImage java.awt.Color java.io.File javax.imageio.ImageIO)
  (:use snake.core))

(defn set-color!
  "iterates over an image with a function that takes coordinates and returns colors"
  [image [x y] color]
  (.setRGB image x y (.getRGB color)))

(defn clear-image!
  "iterates over an image with a function that takes coordinates and returns colors"
  [image]
  (doseq [x (range (.getWidth image)) y (range (.getHeight image))]
    (.setRGB image x y (.getRGB (Color/white))))) 

(defn world->image [world]
  (if (nil? world) nil
      (let [image (new BufferedImage (:size world) (:size world) (. BufferedImage TYPE_INT_RGB))]
        (clear-image! image) ;; clear the canvas
        (set-color! image (:head world) (Color/black)) ;; paint the head
        (doseq [tail-cell (:tail world)] (set-color! image tail-cell (Color/black))) ;; paint every tail cell
        (set-color! image (:food world) (Color/red)) ;; paint the food
        image)))

(defn write-file [image filename]
  (if (nil? image) nil
      (ImageIO/write image "png" (new File filename))))

(defn -main [& args]
  (let [timeline (iterate #(tick (face-food %)) (random-world 20))]
    (doseq [n (range 50)] (write-file (world->image (nth timeline n)) (str "out/gen" n ".png")))))
