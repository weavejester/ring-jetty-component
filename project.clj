(defproject ring-jetty-component "0.3.1"
  :description "A component for the standard Ring Jetty adapter"
  :url "https://github.com/weavejester/ring-jetty-component"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [com.stuartsierra/component "0.4.0"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [suspendable "0.1.1"]]
  :aliases {"test-all" ["with-profile" "default:+1.8:+1.9:+1.10" "test"]}
  :profiles
  {:dev {:dependencies [[clj-http "2.1.0"]]}
   :1.8 {:dependencies [[org.clojure/clojure "1.8.0"]]}
   :1.9 {:dependencies [[org.clojure/clojure "1.9.0"]]}
   :1.10 {:dependencies [[org.clojure/clojure "1.10.0"]]}})
