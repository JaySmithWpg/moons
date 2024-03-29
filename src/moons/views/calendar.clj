(ns moons.views.calendar
  (:require [moons.views.common :as common])
  (:use [noir.core :only [defpage defpartial]]
        [hiccup.element :only [image]]
        [hiccup.core]
        [hiccup.form]))

(defrecord Moon [image-path cycle-length cycle-start])
(def moons (list (Moon. "/img/Illeiya%d.png" 10 0)
                 (Moon. "/img/Mneme%d.png" 28 0)))
(def inverse-moons (list (Moon. "/img/Illeiya%d.png" 10 5)
                         (Moon. "/img/Mneme%d.png" 28 14))) 

(defrecord Calendar [path title days-in-month months-in-year moons])
(def calendars (list (Calendar. "Illeiya" "Illeiyan Calendar" 30 12 moons)
                     (Calendar. "Mneme" "Mneme Calendar" 28 13 moons)
                     (Calendar. "Waltz" "Moon Waltz" 140 2 moons)
                     (Calendar. "Illeiya2" "Inverse Illeiyan Calendar" 30 12 inverse-moons)
                     (Calendar. "Mneme2" "Inverse Mneme Calendar" 28 13 inverse-moons)
                     (Calendar. "Waltz2" "Inverse Moon Waltz" 140 2 inverse-moons)))

(defn moon-image [moon day-since-epoch]
  (image (format (:image-path moon)
                 (mod (+ day-since-epoch
                         (:cycle-start moon))
                      (:cycle-length moon)))))

(defn get-day [day-since-epoch calendar]
  (let [day-of-month (inc (mod day-since-epoch (:days-in-month calendar)))]
    (html [:span
           [:h3 day-of-month]
           (map #(moon-image % day-since-epoch) (:moons calendar))])))

(defpartial nav-controls [year month calendar]
  (label "year" "Year: ")
  (drop-down "year" (range 0 3001) year)
  (label "month" "Month: ")
  (drop-down "month" (range 1 (inc (:months-in-year calendar))) month))

(defpartial render-calendar [calendar year month]
  (let [year (Integer. year)
        month (Integer. month)
        days-in-month (:days-in-month calendar)
        days-in-year (* (:days-in-month calendar) (:months-in-year calendar))
        start-date (+ (* year days-in-year) (* (dec month) days-in-month))]
    (common/layout
      [:header
       [:h1 (:title calendar)]
       [:h2 "Year " year " Month " month]]
      [:nav
       (form-to [:get (str "/" (:path calendar)) ]
               (nav-controls year month calendar)
               (submit-button "Go"))]
      [:section
       (map #(get-day % calendar)
            (range start-date (+ start-date days-in-month)))])))

(defpage "/:calendar" {:keys [calendar year month]}
  (render-calendar (first (filter #(= calendar (:path %)) calendars))
                   (if (= nil year) 0 year)
                   (if (= nil month) 1 month)))

