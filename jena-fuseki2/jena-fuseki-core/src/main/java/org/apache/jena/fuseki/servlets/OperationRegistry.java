/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jena.fuseki.servlets;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import org.apache.jena.atlas.logging.Log;
import org.apache.jena.fuseki.Fuseki;
import org.apache.jena.fuseki.server.DataService;
import org.apache.jena.fuseki.server.Operation;
import org.apache.jena.riot.WebContent;

/**
 * Registry of operations.
 *
 * The registry (accessed via the servlet context) provides
 * <ul>
 * <li>Content-type to {@code Operation} via {@link #findByContentType(String)}.
 * <li>{@code Operation} to {@link ActionService} implementation via {@link #findHandler(Operation)}
 * </ul>
 */
public class OperationRegistry {

    // Standard, non-graph-level access control versions.
    private static final ActionService queryServlet    = new SPARQL_QueryDataset();
    private static final ActionService updateServlet   = new SPARQL_Update();
    private static final ActionService uploadServlet   = new SPARQL_Upload();
    private static final ActionService gspServlet_R    = new GSP_R();
    private static final ActionService gspServlet_RW   = new GSP_RW();

    /** Map ContentType (lowercase, no charset) to the {@code Operation} for handling it. */
    private final Map<String, Operation> contentTypeToOperation = new ConcurrentHashMap<>();
    public Map<String, Operation> contentTypeToOperation() { return contentTypeToOperation; }

    /** Map {@link Operation} to servlet handler.
     * {@code Operation}s are the internal symbol identifying an operation,
     * not the name used in the configuration file,
     * which is mapped by {@link DataService#getEndpoint(String)}.
     */
    private final Map<Operation, ActionService> operationToHandler = new ConcurrentHashMap<>();
    public Map<Operation, ActionService> operationToHandler() { return operationToHandler; }

    public OperationRegistry(OperationRegistry other) {
        contentTypeToOperation.putAll(other.contentTypeToOperation);
        operationToHandler.putAll(other.operationToHandler);
    }

    public OperationRegistry(boolean includeStdConfig) {
        if ( includeStdConfig ) {
            register(Operation.Query, WebContent.contentTypeSPARQLQuery, queryServlet);
            register(Operation.Update, WebContent.contentTypeSPARQLUpdate, updateServlet);
            register(Operation.Upload,   null, uploadServlet);
            register(Operation.GSP_R,    null, gspServlet_R);
            register(Operation.GSP_RW,   null, gspServlet_RW);
        }
    }

    /** Find the {@link Operation} for a {@code Content-Type}, or return null. */
    public Operation findByContentType(String contentType) {
        if ( contentType == null )
            return null;
        return contentTypeToOperation.get(contentType);
    }

    /** Find the {@link ActionService} implementation for an {@link Operation}, or return null..*/
    public ActionService findHandler(Operation operation) {
        if ( operation == null )
            return null;
        return operationToHandler.get(operation);
    }

    public boolean isRegistered(Operation operation) {
        return operationToHandler.containsKey(operation);
    }

    /**
     * Register a new {@link Operation}, with its {@code Content-Type} (may be null,
     * meaning no dispatch by content type), and the implementation handler.
     * <p>
     * The application needs to enable an operation on a service endpoint.
     * <p>
     * Replaces any existing registration.
     */
    public void register(Operation operation, String contentType, ActionService action) {
        Objects.requireNonNull(operation);
        Objects.requireNonNull(action);
        if ( contentType != null )
            contentTypeToOperation.put(contentType, operation);
        else
            // Remove any mapping.
            contentTypeToOperation.values().remove(operation);
        operationToHandler.put(operation, action);
    }

    /**
     * Remove the registration for an operation.
     */
    public void unregister(Operation operation) {
        Objects.requireNonNull(operation);
        operationToHandler.remove(operation);
        contentTypeToOperation.values().remove(operation);
    }

    // The server DataAccessPointRegistry is held in the ServletContext for the server.
    public static OperationRegistry get(ServletContext servletContext) {
        OperationRegistry registry = (OperationRegistry)servletContext.getAttribute(Fuseki.attrOperationRegistry);
        if ( registry == null )
            Log.warn(OperationRegistry.class, "No service registry for ServletContext");
        return registry;
    }

    public static void set(ServletContext cxt, OperationRegistry registry) {
        cxt.setAttribute(Fuseki.attrOperationRegistry, registry);
    }
}