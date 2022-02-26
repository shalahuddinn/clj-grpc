(defproject clj-grpc "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :main ^:skip-aot clj-grpc.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}}

  :resource-paths ["resources/HelloWorldProto.jar"]
  :repositories {"local" ~(str (.toURI (java.io.File. "maven_repository")))}

  :dependencies [[id.example/HelloWorldProto "0.1.1"]
                 [org.clojure/clojure "1.10.3"]
                 [com.google.protobuf/protobuf-java "3.6.0"]
                 [javax.annotation/javax.annotation-api "1.2"]
                 [io.netty/netty-codec-http2 "4.1.25.Final"]
                 [io.grpc/grpc-core "1.13.1"]
                 [io.grpc/grpc-netty "1.13.1"
                  :exclusions [io.grpc/grpc-core
                               io.netty/netty-codec-http2]]
                 [io.grpc/grpc-protobuf "1.13.1"]
                 [io.grpc/grpc-stub "1.13.1"]]
  )
