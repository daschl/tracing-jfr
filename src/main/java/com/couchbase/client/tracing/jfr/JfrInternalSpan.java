/*
 * Copyright (c) 2019 Couchbase, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.couchbase.client.tracing.jfr;

import com.couchbase.client.core.cnc.InternalSpan;
import com.couchbase.client.core.msg.RequestContext;
import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Name;
import jdk.jfr.Timespan;

@Name("com.couchbase.client.Request")
@Category("Couchbase")
@Label("Couchbase Request")
@Description("Represents a Couchbase Request in the SDK, managed through the RequestTracer")
public class JfrInternalSpan extends Event implements InternalSpan {

  @Label("Encoding Duration")
  @Timespan(Timespan.NANOSECONDS)
  @Description("The time it took to encode the request payload, if at all needed")
  private volatile long encodingDuration;

  @Label("Dispatch Duration")
  @Timespan(Timespan.NANOSECONDS)
  @Description("The time the request spent on the network and the OS outside of the SDKs control")
  private volatile long dispatchDuration;

  @Label("Operation Name")
  @Description("The name of the operation")
  private final String operationName;

  @Label("Start Thread")
  @Description("The thread initiating the request")
  private final Thread startThread;

  @Label("End Thread")
  @Description("The thread completing the request")
  private volatile Thread endThread;

  @Label("Remote Dispatch Socket")
  @Description("The remote dispatch hostname and port used last")
  private volatile String remoteDispatchSocket;

  @Label("Local Dispatch Socket")
  @Description("The local dispatch hostname and port used last")
  private volatile String localDispatchSocket;

  private volatile RequestContext ctx;

  JfrInternalSpan(final String operationName) {
    this.operationName = operationName;
    this.startThread = Thread.currentThread();
    begin();
  }

  public void finish() {
    if (shouldCommit()) {
      encodingDuration = ctx.encodeLatency();
      dispatchDuration = ctx.dispatchLatency();
      remoteDispatchSocket = ctx.lastDispatchedTo().toString();
      localDispatchSocket = ctx.lastDispatchedFrom().toString();
      endThread = Thread.currentThread();
      end();
      commit();
    }
  }

  public void requestContext(RequestContext ctx) {
    this.ctx = ctx;
  }

  public RequestContext requestContext() {
    return ctx;
  }

  public void startPayloadEncoding() { }

  public void stopPayloadEncoding() { }

  public void startDispatch() { }

  public void stopDispatch() { }

}
