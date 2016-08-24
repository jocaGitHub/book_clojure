(ns book.routes.home
  (:require [compojure.core :refer :all]
            [book.views.layout :as layout]
            [clojure.string :as str]
            [hiccup.form :refer :all]
            [hiccup.core :refer [h]]
            [ring.util.response :as ring]
            [book.models.db :as db]))


(defroutes home-routes
  (GET "/" [] (indexpage))
  (GET "/add" [] (insert_update))
  (GET "/add" [title price description author stars error id] (insert_update title price description author stars error id))
  (GET "/show" [] (show))
  (POST "/save" [title price desription author stars id] (save-book title price desription author stars id))
  (GET "/delete/:id" [id] (delete-book id))
  (GET "/update/:id"[id] (show-book (db/find-book id))))


(defn indexpage []
  (layout/common
    [:h2 "Welcome to my bookstore"]
    [:br]
    [:a {:href "/add"} "Add new" ]
    [:br]
    [:a {:href "/show"} "Show books" ]))

(defn show []
  (layout/common
    [:h1 "Books"]
    (show-books)
    [:a {:href "/" :class "back"} "Home"]))

(defn show-books []
  [:table {:border 1}
   [:thead
    [:tr
     [:th "Id"]
     [:th "Title"]
     [:th "Price"]
     [:th {:width 250} "Description"]
     [:th "Author"]
     [:th "Rating"]
     [:th "Creation time"]
     [:th "Delete"]
     [:th "Update"]]]
   (into [:tbody]
         (for [book (db/read-books)]
           [:tr
            [:td (:id book)]
            [:td (:title book)]
            [:td (:price book)]
            [:td (:description book)]
            [:td (:author book)]
            [:td (:stars book)]
            [:td (format-time (:creation_time book))]
            [:td [:a {:href (str "/delete/" (h (:id book)))} "delete"]]
            [:td [:a {:href (str "/update/" (h (:id book)))} "update"]]]))])

(defn insert_update [& [title price description author stars error id]]
  (layout/common
  [:h2 (if (nil? id) "Add new book" "Updating book")]
  (form-to {:id "frm_insert"}
    [:post "/save"]
           (if (not (nil? id))
             [:p "Id:"])
           (if (not (nil? id))
               (text-field {:readonly true} "id" id))
           [:p "Title:"]
           (text-field "title" title)
           [:p "Price:" ]
           (text-field {:id "price"} "price" price)
           [:p "Description:"]
           (text-area {:rows 5 :cols 30} "desription" description)
           [:p "Author:"]
           (text-field "author" author)
           [:p "Stars:"]
           (drop-down "stars" [1 2 3 4 5] stars)
           [:br] [:br]
           (submit-button {:onclick " return javascript:validateInsertForm()"} (if (nil? id)"Insert" "Update"))
           [:hr]
           [:p {:style "color:red;"} error])
    [:a {:href "/" :class "back"} "Home"]))


(defn save-book [title price description author stars & [id]]
  (cond
    (empty? title)
    (insert_update  title price description author stars "Enter title" id)
    (nil? (parse-number price))
    (insert_update  title price description author stars "Price must be a number!" id)
    (<= (parse-number price) 0)
    (insert_update  title price description author stars "Price must be a positive number!" id)
    (empty? description)
    (insert_update  title price description author stars "Enter description!" id)
    (empty? author)
    (insert_update  title price description author stars "Enter author" id)
    (empty? stars)
    (insert_update  title price description author stars "Enter stars" id)
    :else
  (do
    (if (nil? id)
      (db/save-book title price description author stars)
      (db/update-book id title price description author stars))
  (ring/redirect "/show"))))


(defn parse-number [s]
  (if (re-find #"^-?\d+\.?\d*$" s)
    (read-string s)))

(defn format-time [timestamp]
  (-> "dd/MM/yyyy"
      (java.text.SimpleDateFormat.)
      (.format timestamp)))

(defn delete-book [id]
  (when-not (str/blank? id)
    (db/delete-book id))
  (ring/redirect "/show"))

(defn show-book [book]
  (insert_update (:title book) (:price book) (:description book) (:author book) (:stars book) nil (:id book)))



