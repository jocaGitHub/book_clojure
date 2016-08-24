(ns book.views.layout
  (:require [hiccup.page :refer :all]))

(defn common [& body]
  (html5
    [:head
     [:title "Welcome to bookshop"]
     (include-css "/css/screen.css")
     (include-js "/js/validation.js")]
    [:body body]))
