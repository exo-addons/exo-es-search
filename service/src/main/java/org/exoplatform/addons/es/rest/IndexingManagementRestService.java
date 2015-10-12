/* 
* Copyright (C) 2003-2015 eXo Platform SAS.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see http://www.gnu.org/licenses/ .
*/
package org.exoplatform.addons.es.rest;

import org.exoplatform.addons.es.index.IndexingOperationProcessor;
import org.exoplatform.addons.es.index.IndexingService;
import org.exoplatform.addons.es.index.IndexingServiceConnector;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by The eXo Platform SAS
 * Author : Thibault Clement
 * tclement@exoplatform.com
 * 10/6/15
 */
@Path("/indexingManagement")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("administrators")
public class IndexingManagementRestService implements ResourceContainer {

  private final static Log LOG = ExoLogger.getLogger(IndexingManagementRestService.class);

  private IndexingService indexingService;
  private IndexingOperationProcessor indexingOperationProcessor;

  public IndexingManagementRestService(IndexingService indexingService, IndexingOperationProcessor indexingOperationProcessor) {
    this.indexingService = indexingService;
    this.indexingOperationProcessor = indexingOperationProcessor;
  }

  @GET
  @Path("/connector")
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed("administrators")
  public Response getConnectors() {
    LOG.info("Call getConnectors via REST");
    List<IndexingServiceConnector> connectors = new ArrayList<>(indexingOperationProcessor.getConnectors().values());
    return Response.ok(connectors, MediaType.APPLICATION_JSON).build();
  }

  @GET
  @Path("/connector/{connectorType}/_reindex")
  @RolesAllowed("administrators")
  public Response reindexConnector(@PathParam("connectorType") String connectorType) {
    LOG.info("Call reindexConnector via REST");
    indexingService.reindexAll(connectorType);
    return Response.ok("ok", MediaType.TEXT_PLAIN).build();
  }

  @GET
  @Path("/connector/{connectorType}/_disable")
  @RolesAllowed("administrators")
  public Response disable(@PathParam("connectorType") String connectorType) {
    LOG.info("Call disable via REST");
    //TODO implement a disable connector method in IndexingService
    return Response.ok("ok", MediaType.TEXT_PLAIN).build();
  }

  @GET
  @Path("/connector/{connectorType}/_enable")
  @RolesAllowed("administrators")
  public Response enable(@PathParam("connectorType") String connectorType) {
    LOG.info("Call disable via REST");
    //TODO implement a enable connector method in IndexingService
    return Response.ok("ok", MediaType.TEXT_PLAIN).build();
  }

}

