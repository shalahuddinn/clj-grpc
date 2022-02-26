(ns clj-grpc.service
  (:gen-class
    :name clj-grpc.service.HelloWorldServiceImpl
    :extends id.example.HelloWorldProto.HelloWorldServiceGrpc$HelloWorldServiceImplBase)
  (:import (io.grpc.stub StreamObserver)
           (id.example.HelloWorldProto.Hello HelloRequest HelloResponse)))

(defn -hello [this ^HelloRequest req ^StreamObserver res]
  (let [name (.getText req)]
    (doto res
      (.onNext (-> (HelloResponse/newBuilder)
                   (.setText (str "Hello, " name))
                   (.build)))
      (.onCompleted))))