(defproject book "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.6"]
                 [hiccup "1.0.5"]
                 [hiccup-table "0.2.0"]
                 [ring-server "0.3.1"]
                 ;;JDBC dependencies
                 [org.clojure/java.jdbc "0.2.3"]
                  [org.xerial/sqlite-jdbc "3.7.2"]]
  :plugins [[lein-ring "0.8.12"]]
  :ring {:handler book.handler/app
         :init book.handler/init
         :destroy book.handler/destroy}
  :profiles
  {:uberjar {:aot :all}
   :production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}}
   :dev
   {:dependencies [[ring-mock "0.1.5"] [ring/ring-devel "1.3.1"]]}})

