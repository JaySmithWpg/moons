(ns moons.views.calendar
  (:require [moons.views.common :as common])
  (:use [noir.core :only [defpage defpartial]]
        [hiccup.element :only [image]]
        [hiccup.core]
        [hiccup.form]))

(defrecord Calendar [path title days-in-month months-in-year])
(def calendars (list (Calendar. "Illeiya" "Illeiyan Calendar" 30 12)
                     (Calendar. "Mneme" "Mneme Calendar" 28 13)
                     (Calendar. "Waltz" "Moon Waltz" 140 2)))

(defrecord Moon [image-path cycle-length])
(def moons (list (Moon. "/img/Illeiya%d.png" 10)
                 (Moon. "/img/Mneme%d.png" 28)))

(defn moon-image [moon day-since-epoch]
  (image (format (:image-path moon)
                 (mod day-since-epoch (:cycle-length moon)))))

(defn get-day [day-since-epoch days-in-month]
  (let [day-of-month (inc (mod day-since-epoch days-in-month))]
    (html [:span
           [:h3 day-of-month]
           (map #(moon-image % day-since-epoch) moons)])))

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
       (map #(get-day % days-in-month)
            (range start-date (+ start-date days-in-month)))])))

(defpage "/:calendar" {:keys [calendar year month]}
  (render-calendar (first (filter #(= calendar (:path %)) calendars))
                   (if (= nil year) 0 year)
                   (if (= nil month) 1 month)))

