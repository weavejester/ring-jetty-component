(ns ring.component.jetty
  (:import org.eclipse.jetty.server.Server)
  (:require [com.stuartsierra.component :as component]
            [ring.adapter.jetty :as jetty]))

(defrecord JettyServer [app]
  component/Lifecycle
  (start [component]
    (if (:server component)
      component
      (let [options (-> component (dissoc :app) (assoc :join? false))]
        (assoc component :server (jetty/run-jetty (:handler app) options)))))
  (stop [component]
    (if-let [^Server server (:server component)]
      (do (.stop server)
          (.join server)
          (dissoc component :server))
      component)))

(defn jetty-server
  "Create a Jetty server component from a map of options. The component expects
  an :app key that contains a map or record with a :handler key. This allows
  the Ring handler to be supplied in a dependent component.

  All other options are passed to the Jetty adapter, except for :join?, which
  is always false to ensure that starting the component doesn't block the
  current thread."
  [options]
  (map->JettyServer options))
