/**
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
package org.apache.camel.component.metrics.messagehistory;

import java.util.Date;

import com.codahale.metrics.Timer;
import org.apache.camel.MessageHistory;
import org.apache.camel.NamedNode;

/**
 * A codahale metrics based {@link MessageHistory}
 */
public class MetricsMessageHistory implements MessageHistory {

    private final String routeId;
    private final NamedNode namedNode;
    private final Timer timer;
    private final Timer.Context context;

    public MetricsMessageHistory(String routeId, NamedNode namedNode, Timer timer) {
        this.routeId = routeId;
        this.namedNode = namedNode;
        this.timer = timer;
        this.context = timer.time();
    }

    @Override
    public String getRouteId() {
        return routeId;
    }

    @Override
    public NamedNode getNode() {
        return namedNode;
    }

    @Override
    public Date getTimestamp() {
        return null;
    }

    @Override
    public long getElapsed() {
        return timer.getCount();
    }

    @Override
    public void nodeProcessingDone() {
        context.stop();
    }
}