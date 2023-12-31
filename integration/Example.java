/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//
// To run this integrations use:
//
//     kamel run examples/RestDSL.java
//
import org.apache.camel.Exchange;

public class Example extends org.apache.camel.builder.RouteBuilder {
    @Override
    public void configure() throws Exception {
      from("vertx-websocket://my-websocket-path")
      .log(">>> Message received from WebSocket Client : ${body}")
      .transform().simple("${body}${body}")
      // send back to the client, by sending the message to the same endpoint
      // this is needed as by default messages is InOnly
      // and we will by default send back to the current client using the provided connection key
      .to("log:info");

        
    }
}