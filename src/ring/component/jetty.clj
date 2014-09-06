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

(defn jetty-server
  "Create a Jetty server component from a map of options. Expects the :handler
  and :port options to be set at a minimum. All other options are passed to the
  Ring Jetty adapter, except for :join?, which is always false."
  [{:keys [handler port] :as options}]
  {:pre [(ifn? handler) (integer? port)]}
  (map->JettyServer options))
