(ns book.models.db
  (:require [clojure.java.jdbc :as sql])
  (:import java.sql.DriverManager))

(def db {:classname "org.sqlite.JDBC",
         :subprotocol "sqlite",
         :subname "db.sq3"})


(defn create-book-table []
  (sql/with-connection
    db
    (sql/create-table
      :book
      [:id "INTEGER PRIMARY KEY AUTOINCREMENT"]
      [:title "TEXT"]
      [:price "REAL"]
      [:description "TEXT"]
      [:author "TEXT"]
      [:stars "INTEGER"]
      [:creation_time "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"])))


(defn read-books []
  (sql/with-connection
    db
    (sql/with-query-results res
      ["SELECT * FROM book ORDER BY id ASC"]
      (doall res))))


(defn save-book [title price description author stars]
  (sql/with-connection
    db
    (sql/insert-values
      :book
      [:title :price :description :author :stars :creation_time]
      [title price description author stars (new java.util.Date)])))

(defn delete-book [id]
  (sql/with-connection
    db
    (sql/delete-rows
      :book
      ["id=?" id])))

(defn find-book [id]
  (first
    (sql/with-connection
      db
      (sql/with-query-results res
        ["SELECT * FROM book WHERE id= ?" id]
        (doall res)))))

(defn update-book [id title price description author stars]
  (sql/with-connection
    db
    (sql/update-values
      :book
      ["id=?" id]
      {:title title :price price :description description :author author :stars stars})))


