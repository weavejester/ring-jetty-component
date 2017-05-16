(ns ring.component.dependencies-test
  (:require [clojure.test :refer :all]
            [ring.component.jetty :refer [jetty-server]]
            [com.stuartsierra.component :as component]
            [clj-http.client :as http]))

(defn handler-fn [req]
  {:status 200
   :headers {}
   :body (-> req :component :fake-db :data)})

(defrecord FakeDB [data]
  component/Lifecycle
  (start [this] this)
  (stop [this] this))

(deftest component-dependencies-access
  (let [test-system (component/system-map
                     :fake-db (->FakeDB "some data")
                     :http (component/using
                            (jetty-server {:app {:handler handler-fn} :port 3401})
                            [:fake-db]))
        system (component/start test-system)]
    (try
      (let [response (http/get "http://127.0.0.1:3401")]
        (is (= (:body response) "some data")))
      (catch Exception e
        (component/stop system))
      (finally
        (component/stop system)))))
