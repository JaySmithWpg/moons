(ns moons.views.illeiya
  (:require [moons.views.common :as common])
  (:use [noir.core :only [defpage defpartial]]
        [hiccup.element :only [image]]
        [hiccup.core]
        [hiccup.form]))

(def days-in-year 360)
(def days-in-month 30)
(def months-in-year 12)
(def illeiya-cycle 10)
(def mneme-cycle 28)

(defn get-day [day-since-epoch]
  (let [illeiya-phase (mod day-since-epoch illeiya-cycle)
        mneme-phase (mod day-since-epoch mneme-cycle)
        day-of-month (inc (mod day-since-epoch days-in-month))]
    (html [:span
           [:h3 day-of-month]
           (image (format "/img/Illeiya%d.png" illeiya-phase))
           (image (format "/img/Mneme%d.png" mneme-phase))])))

(defpartial nav-controls [year month]
  (label "year" "Year: ")
  (drop-down "year"
             (range 0 3001)
             year)
  (label "month" "Month: ")
  (drop-down "month"
             (range 1 (inc months-in-year))
             month))

(defpartial render-illeiya [year month]
  (let [year (Integer. year)
        month (Integer. month)
        start-date (+ (* year days-in-year)
                      (* (dec month) days-in-month))]
    (common/layout
      [:div
       [:h1 "Illeiyan Calendar"]
       [:h2 "Year " year " Month " month]]
      [:div
       (form-to [:get "/Illeiya"]
               (nav-controls year month)
               (submit-button "Go"))] 
      [:div
       (map get-day (range start-date
                           (+ start-date days-in-month)))])))

(defpage "/Illeiya" {:keys [year month]}
  (render-illeiya (if (= nil year) 0 year)
                  (if (= nil month) 1 month)))

