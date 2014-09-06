# Ring-Jetty-Component

A [component][1] for the standard [Ring][2] Jetty adapter, for use in
applications that use Stuart Sierra's [reloaded workflow][3].

[1]: https://github.com/stuartsierra/component
[2]: https://github.com/ring-clojure/ring
[3]: http://thinkrelevance.com/blog/2013/06/04/clojure-workflow-reloaded

## Installation

Add the following dependency to your `project.clj`:

    [ring-jetty-component "0.1.0-SNAPSHOT"]

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

(def http-server
  (jetty-server {:handler handler, :port 3000}))
```

The `:handler` and `:port` options are mandatory. All other options
are passed back to the Ring Jetty adapter, except for `:join?`, which
is always false. This guarantees the component doesn't block the
running thread.

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

Copyright © 2014 James Reeves

Distributed under the MIT License, the same as Ring.
