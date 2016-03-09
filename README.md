# Ring-Jetty-Component

A [component][1] for the standard [Ring][2] Jetty adapter, for use in
applications that use Stuart Sierra's [reloaded workflow][3].

[1]: https://github.com/stuartsierra/component
[2]: https://github.com/ring-clojure/ring
[3]: http://thinkrelevance.com/blog/2013/06/04/clojure-workflow-reloaded

## Installation

Add the following dependency to your `project.clj`:

    [ring-jetty-component "0.3.1"]

## Usage

Require the library, and the Component library:

```clojure
(require '[ring.component.jetty :refer [jetty-server]]
         '[com.stuartsierra.component :as component])
```

Then create a server component:

```clojure
(defn handler [request]
  {:status  200
   :headers {"Content-Type" "text/plain"}
   :body    "Hello World"})

(def app
  {:handler handler})

(def http-server
  (jetty-server {:app app, :port 3000}))
```

The `:app` option can either be a bare map, or another component
record. It must contain the key `:handler`.

All other options are passed to the ring to the Ring Jetty adapter,
except for `:join?`, which is always false. This guarantees the
component doesn't block the running thread.

This server can be started using `component/start`:

```clojure
(alter-var-root #'http-server component/start)
```

And stopped with `component/stop`:

```clojure
(alter-var-root #'http-server component/stop)
```

As with all components, the return value matters. For more information
on how to use components, and how they're useful, refer to Stuart
Sierra's [Component library][1].

## License

Copyright © 2016 James Reeves

Distributed under the MIT License, the same as Ring.
