(ns ring.component.jetty
  (:import org.eclipse.jetty.server.Server)
  (:require [com.stuartsierra.component :as component]
            [suspendable.core :as suspendable]
            [ring.adapter.jetty :as jetty]))

(defrecord JettyServer [app]
  component/Lifecycle
  (start [component]
    (if (:server component)
      component
      (let [options (-> component (dissoc :app) (assoc :join? false))
            handler (atom (delay (:handler app)))
            server  (jetty/run-jetty (fn [req] (@@handler req)) options)]
        (assoc component
               :server  server
               :handler handler))))
  (stop [component]
    (if-let [^Server server (:server component)]
      (do (.stop server)
          (.join server)
          (dissoc component :server :handler))
      component))
  suspendable/Suspendable
  (suspend [component]
    (if-let [handler (:handler component)]
      (do (reset! handler (promise))
          (assoc component :suspended? true))
      component))
  (resume [component old-component]
    (if (and (:suspended? old-component)
             (= (dissoc component :suspended? :server :handler :app)
                (dissoc old-component :suspended? :server :handler :app)))
      (let [handler (:handler old-component)]
        (deliver @handler (:handler app))
        (-> component
            (assoc :server (:server old-component), :handler handler)
            (dissoc :suspended?)))
      (do (when old-component (component/stop old-component))
          (component/start component)))))

(defn jetty-server
  "Create a Jetty server component from a map of options. The component expects
  an :app key that contains a map or record with a :handler key. This allows
  the Ring handler to be supplied in a dependent component.

  All other options are passed to the Jetty adapter, except for :join?, which
  is always false to ensure that starting the component doesn't block the
  current thread."
  [options]
  (map->JettyServer options))
