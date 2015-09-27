(ns ring.component.jetty-test
  (:import java.net.ConnectException)
  (:require [clojure.test :refer :all]
            [ring.component.jetty :refer [jetty-server]]
            [com.stuartsierra.component :as component]
            [suspendable.core :as suspendable]
            [clj-http.client :as http]))

(deftest test-jetty-server-component
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

(deftest test-jetty-server-suspendable
  (let [response1 {:status 200 :headers {} :body "foo"}
        response2 {:status 200 :headers {} :body "bar"}
        server1   (jetty-server {:app {:handler (constantly response1)}, :port 3400})
        server2   (jetty-server {:app {:handler (constantly response2)}, :port 3400})]
    (testing "suspend and resume"
      (let [server1  (-> server1 component/start suspendable/suspend)
            response (future (http/get "http://127.0.0.1:3400/"))
            server2  (suspendable/resume server2 server1)]
        (try
          (is (identical? (:handler server1) (:handler server2)))
          (is (identical? (:server server1) (:server server2)))
          (is (= (:status @response) 200))
          (is (= (:body @response) "bar"))
          (finally
            (component/stop server1)
            (component/stop server2)))))
    (testing "suspend and resume with different config"
      (let [server1  (-> server1 component/start suspendable/suspend)
            server2  (suspendable/resume (assoc server2 :port 3401) server1)]
        (try
          (is (.isStopped (:server server1)))
          (let [response (http/get "http://127.0.0.1:3401/")]
            (is (= (:status response) 200))
            (is (= (:body response) "bar")))
          (finally
            (component/stop server1)
            (component/stop server2)))))
    (testing "resume from stopped"
      (let [server2 (suspendable/resume server2 server1)]
        (try
          (let [response (http/get "http://127.0.0.1:3400/")]
            (is (= (:status response) 200))
            (is (= (:body response) "bar")))
          (finally
            (component/stop server2)))))
    (testing "resume from nil"
      (let [server2 (suspendable/resume server2 nil)]
        (try
          (let [response (http/get "http://127.0.0.1:3400/")]
            (is (= (:status response) 200))
            (is (= (:body response) "bar")))
          (finally
            (component/stop server2)))))))
