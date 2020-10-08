(ns compound.core
  (:require [clojure.pprint :refer [print-table]])
  (:gen-class))

;; items is :label, dividend rate, dividend growth rate
(def items [[:abbv 0.0504  0.209]
            [:mo   0.07    0.10]
            [:pru  0.0472  0.13]
            [:abc  0.0178  0.10]
            [:amp  0.0219  0.11]
            [:cm   0.0521  0.036]
            [:cmi  0.0316  0.118]
            [:cvs  0.0278  0.127]
            [:dal  0.0274  0.38]
            [:epd  0.068   0.042]
            [:oxy  0.0760  0.024]
            [:spg  0.06    0.107]
            [:wfc  0.0434  0.073]])

(defn total
  [capital rate growth years]
  (loop [r rate y years total capital]
    (if (= y 0)
      total
      (recur (+ r (* r growth))
             (- y 1)
             (+ total (* total r))))))

(defn yield-on-cost
  [rate growth years]
  (loop [r rate y years]
    (if (= y 0)
      r
      (recur (+ r (* r growth)) (- y 1)))))

;; nice format to stdout: (pprint/print-table (sort-by :yield-on-cost (report 15)))
(defn report
  [years]
  (map (fn [i] {:ticker (nth i 0) :yield-on-cost (yield-on-cost (nth i 1) (nth i 2) years)})
       items))
           
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (->> 30
       report
       (sort-by :yield-on-cost)
       reverse
       print-table))

(comment

  ;; in cider, put your cursor over the end form and hit c-x c-e
  ;; c-c c-v c-f e sends results to an external window, easy to copy 

  (require '[clojure.pprint :as pprint])
  
  (pprint
   
   {:test-name :?
    :test-total-1ooo-dollars (= 22764.3037016127   (total 1000 0.05 0.05 30))
    :test-total-100-dollars  (= 1625911.4580202834 (total 100  0.10 0.15 22))
    :test-total-1-dollar     (= 0 (total 1 0.10 0.04 10))
    :test-yoc-low-rates      (= (yield-on-cost 0.05 0.05 20) 0.13266488525722098)})

  (-main [])
)
     

