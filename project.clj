(defproject ring-jetty-component "0.2.0"
  :description "A component for the standard Ring Jetty adapter"
  :url "https://github.com/weavejester/ring-jetty-component"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.stuartsierra/component "0.2.2"]
                 [ring/ring-jetty-adapter "1.3.1"]]
  :aliases {"test-all" ["do" ["test"] ["with-profile" "+1.6" "test"]]}
  :profiles
  {:dev {:dependencies [[clj-http "1.0.0"]]}
   :1.6 {:dependencies [[org.clojure/clojure "1.6.0"]]}})
