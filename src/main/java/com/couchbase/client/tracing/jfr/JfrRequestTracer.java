package com.couchbase.client.tracing.jfr;

import com.couchbase.client.core.cnc.RequestSpan;
import com.couchbase.client.core.cnc.RequestTracer;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class JfrRequestTracer implements RequestTracer {

  public JfrInternalSpan span(String operationName, RequestSpan parent) {
    return new JfrInternalSpan(operationName);
  }

  public Mono<Void> start() {
    return Mono.empty();
  }

  public Mono<Void> stop(Duration timeout) {
    return Mono.empty();
  }

}
