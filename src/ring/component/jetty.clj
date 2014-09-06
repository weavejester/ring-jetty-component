(ns ring.component.jetty
  (:import org.eclipse.jetty.server.Server)
  (:require [com.stuartsierra.component :as component]
            [ring.adapter.jetty :as jetty]))

(defrecord JettyServer [handler]
  component/Lifecycle
  (start [component]
    (if (:server component)
      component
      (let [options (-> component (dissoc :handler) (assoc :join? false))]
        (assoc component :server (jetty/run-jetty handler options)))))
  (stop [component]
    (if-let [^Server server (:server component)]
      (do (.stop server)
          (dissoc component :server))
      component)))

(defn jetty-server [options]
  {:pre (ifn? (:handler options))}
  (map->JettyServer options))
