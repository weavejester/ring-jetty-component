(ns ring.component.jetty-test
  (:import java.net.ConnectException)
  (:require [clojure.test :refer :all]
            [ring.component.jetty :refer [jetty-server]]
            [com.stuartsierra.component :as component]
            [clj-http.client :as http]))

(deftest test-jetty-server
  (let [response {:status 200 :headers {} :body "test"}
        handler  (constantly response)
        server   (jetty-server {:app {:handler handler}, :port 3400})]
    (testing "server starts"
      (let [server (component/start server)]
        (try
          (let [response (http/get "http://127.0.0.1:3400/")]
            (is (= (:status response) 200))
            (is (= (:body response) "test")))
          (finally
            (component/stop server)))))
    (testing "server stops"
      (is (thrown? ConnectException (http/get "http://127.0.0.1:3400/"))))))
