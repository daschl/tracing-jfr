# JFR Request Tracer for Couchbase Java SDK 3

Needs Java 11!

## Usage Example


```java
// Load the Tracer
ClusterEnvironment env = ClusterEnvironment.builder()
  .requestTracer(new JfrRequestTracer())
  .build();

// Use the env and then do everything as normal
Cluster cluster = Cluster.connect(
  "127.0.0.1",
  clusterOptions("Administrator", "password").environment(env)
);
```

Then look at the custom events in the JMC/JFR recording, you'll find Couchbase Requests in there in the Couchbase section!