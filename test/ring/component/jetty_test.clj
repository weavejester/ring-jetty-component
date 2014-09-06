(ns ring.component.jetty-test
  (:import java.net.ConnectException)
  (:require [clojure.test :refer :all]
            [ring.component.jetty :refer [jetty-server]]
            [com.stuartsierra.component :as component]
            [clj-http.client :as http]))

(deftest test-jetty-server
  (let [response {:status 200 :headers {} :body "test"}
        handler  (constantly response)]
    (testing "preconditions"
      (is (thrown? AssertionError (jetty-server {})))
      (is (thrown? AssertionError (jetty-server {:handler handler})))
      (is (thrown? AssertionError (jetty-server {:port 3400}))))
    (testing "server starts"
      (let [server (component/start (jetty-server {:handler handler, :port 3400}))]
        (try
          (Thread/sleep 100)
          (let [response (http/get "http://127.0.0.1:3400/")]
            (is (= (:status response) 200))
            (is (= (:body response) "test")))
          (finally
            (component/stop server)))))
    (testing "server stops"
      (Thread/sleep 100)
      (is (thrown? ConnectException (http/get "http://127.0.0.1:3400/"))))))
