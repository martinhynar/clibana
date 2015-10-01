(defproject
  clibana "0.1.6"

  :description "A Clojure based generator for Kibana 4 objects."

  :url "http://martinhynar.cz/clibana"

  :scm {:name "git"
        :url  "https://github.com/martinhynar/clibana"}

  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/data.json "0.2.6"]
                 [com.cemerick/url "0.1.1"]
                 [org.clojure/clojurescript "1.7.122"]]

  :plugins [[com.keminglabs/cljx "0.6.0"]
            [lein-cljsbuild "1.1.0"]
            [lein-ancient "0.6.7"]
            [com.cemerick/clojurescript.test "0.3.3"]
            [lein-pdo "0.1.1"]
            [codox "0.8.10"]]

  :jar-exclusions [#"\.cljx|\.svn|\.swp|\.swo|\.DS_Store|play_with_*"]

  :prep-tasks [["cljx" "once"] "javac" "compile"]
  :source-paths ["target/generated/src" "src/cljx"]
  :test-paths ["target/generated/test"]

  :cljx {:builds [{:source-paths ["src/cljx"]
                   :output-path  "target/generated/src"
                   :rules        :clj}
                  {:source-paths ["src/cljx"]
                   :output-path  "target/generated/src"
                   :rules        :cljs}
                  {:source-paths ["test/cljx"]
                   :output-path  "target/generated/test"
                   :rules        :clj}
                  {:source-paths ["test/cljx"]
                   :output-path  "target/generated/test"
                   :rules        :cljs}]}

  :cljsbuild {:builds        [{:jar          true
                               :source-paths ["target/generated/src" "target/generated/test"]
                               :compiler     {:output-to     "target/cljs/clibana.js"
                                              :optimizations :simple}}]
              :test-commands {"unit-tests" ["node" :node-runner
                                            "target/cljs/clibana.js"]}}

  :profiles {
             :dev  {:dependencies [[clojurewerkz/elastisch "2.1.0"]]
                    :aliases      {"once"       ["do" "cljx" "once," "cljsbuild" "once"]
                                   "auto"       ["pdo" "cljx" "auto," "cljsbuild" "auto"]
                                   "test-all"   ["with-profile" "test" "do" "clean," "cljx" "once," "cljsbuild" "test," "test"]
                                   "local-site" ["do" "with-profile" "site" "doc"]
                                   }
                    }
             :jar  {:aot :all}
             :site {:codox {:output-dir "../clibana-gh-pages/api" :exclude [clibana.internal.common]}}
             }
  :repl-options {:init-ns play-with-clibana
                 :port    40000})
