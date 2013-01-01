(ns moons.views.common
  (:use [noir.core :only [defpartial]]
        [hiccup.page :only [include-css html5]]))

(defpartial layout [& content]
            (html5
              [:head
               [:title "Moon Calendars"]
               (include-css "/css/reset.css")
               (include-css "/css/moons.css")]
              [:body
               [:div#wrapper
                content]]))
