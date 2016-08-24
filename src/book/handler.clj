(ns book.handler
  (:require [compojure.core :refer [defroutes routes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [book.routes.home :refer [home-routes]]
            [book.models.db :as db]))

(defn init []
  (if-not (.exists (java.io.File. "./db.sq3"))
    (db/create-book-table)))

(defn destroy []
  (println "bookapp is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Page Not Found"))

(def app
  (-> (routes home-routes app-routes)
      (handler/site)
      (wrap-base-url)))
