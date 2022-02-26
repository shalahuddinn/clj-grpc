(ns clj-grpc.core
  (:gen-class)
  (:require [clj-grpc.service])
  (:import (io.grpc ServerBuilder)
           (clj-grpc.service HelloWorldServiceImpl)))


(def SERVER_PORT 50051)

(defn start []
  (let [helloWorldService (new HelloWorldServiceImpl)
        server (-> (ServerBuilder/forPort SERVER_PORT)
                   (.addService helloWorldService)
                   (.build)
                   (.start))]
    (-> (Runtime/getRuntime)
        (.addShutdownHook
          (Thread. (fn []
                     (if (not (nil? server))
                       (.shutdown server))))))
    (if (not (nil? server))
      (.awaitTermination server))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (start))



;public class HelloWorldServer {
;     private static final int PORT = 50051;
;     private Server server;
;
;     public void start() throws IOException {
;                                             server = ServerBuilder.forPort(PORT)
;                                             .addService(new HelloServiceImpl())
;                                             .build()
;                                             .start();
;                                             }
;
;     public void blockUntilShutdown() throws InterruptedException {
;                                                                   if (server == null) {
;                                                                                        return;
;                                                                                        }
;
;                                                                   server.awaitTermination();
;                                                                   }
;
;     public static void main(String[] args)
;     throws InterruptedException, IOException {
;                                               HelloWorldServer server = new HelloWorldServer();
;                                               server.start();
;                                               server.blockUntilShutdown();
;                                               }
;     }