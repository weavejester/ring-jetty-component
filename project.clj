(defproject ring-jetty-component "0.3.0"
  :description "A component for the standard Ring Jetty adapter"
  :url "https://github.com/weavejester/ring-jetty-component"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [com.stuartsierra/component "0.3.1"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [suspendable "0.1.1"]]
  :aliases {"test-all" ["with-profile" "default:+1.8" "test"]}
  :profiles
  {:dev {:dependencies [[clj-http "2.1.0"]]}
   :1.8 {:dependencies [[org.clojure/clojure "1.8.0"]]}})
