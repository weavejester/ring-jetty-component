(defproject ring-jetty-component "0.1.0-SNAPSHOT"
  :description "A component for the standard Ring Jetty adapter"
  :url "https://github.com/weavejester/ring-jetty-component"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.stuartsierra/component "0.2.1"]
                 [ring/ring-jetty-adapter "1.3.1"]]
  :profiles
  {:dev {:dependencies [[clj-http "1.0.0"]]}})
