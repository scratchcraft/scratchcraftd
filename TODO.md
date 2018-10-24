## tasks

* Replace jetty websockets with [netty-socketio](https://github.com/mrniko/netty-socketio).
  * Can we create a standard web socket server? Can we test via the `ws` command?
  * Haven't tried netty-socketio yet.
  * Not sure how to include JAR dependencies. Receive NoClassDefFoundError at run-time after
    embedding within mod.
* - (test) Successfully embed dependencies and start mod server.
* - (test) Send web socket message to endpoint (`ws`).
* Player Commands:
  - x Move forward
  - x Move backward
  - Move left
  - Move right
  - x Jump (?)
  - x Place block
