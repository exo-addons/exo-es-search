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
package org.exoplatform.addons.es.client;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

/**
 * Created by The eXo Platform SAS Author : Thibault Clement
 * tclement@exoplatform.com 10/16/15
 */
public abstract class ElasticClient {

  private static final String ES_INDEX_CLIENT_DEFAULT = "http://127.0.0.1:9200";

  private static final Log    LOG                     = ExoLogger.getExoLogger(ElasticClient.class);

  protected String            urlClient;

  protected HttpClient        client;

  public ElasticClient() {
    this.client = getHttpClient();
    this.urlClient = ES_INDEX_CLIENT_DEFAULT;
  }

  ElasticResponse sendHttpPostRequest(String url, String content) {
    ElasticResponse response;

    try {
      HttpPost httpTypeRequest = new HttpPost(url);
      httpTypeRequest.setEntity(new StringEntity(content, "UTF-8"));
      response = handleHttpResponse(client.execute(httpTypeRequest));
      LOG.debug("Sent request to ES:\n Method = POST \nURI =  {} \nContent = {}", url, content);
    } catch (IOException e) {
      throw new ElasticClientException(e);
    }
    return response;
  }

  ElasticResponse sendHttpDeleteRequest(String url) {
    ElasticResponse response;

    try {
      HttpDelete httpDeleteRequest = new HttpDelete(url);
      response = handleHttpResponse(client.execute(httpDeleteRequest));

      LOG.debug("Sent request to ES:\n Method = DELETE \nURI =  {}", url);

    } catch (IOException e) {
      throw new ElasticClientException(e);
    }
    return response;
  }

  /**
   * Handle Http response receive from ES Log an INFO if the return status code
   * is 2xx Log an ERROR if the return code is different from 2xx
   *
   * @param httpResponse The Http Response to handle
   */
  private ElasticResponse handleHttpResponse(HttpResponse httpResponse) throws IOException {
    String response;

    InputStream is = null;
    try {
      is = httpResponse.getEntity().getContent();
      response = IOUtils.toString(is, "UTF-8");
    } finally {
      if (is!=null) {
        is.close();
      }
    }

    if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
      throw new ElasticClientAuthenticationException();
    }
    if (ElasticIndexingAuditTrail.isError(httpResponse.getStatusLine().getStatusCode())) {
      LOG.error("Error when trying to send request to ES. The reason is: {}", response);
    } else {
      LOG.debug("Success request to ES: {}", response);
    }
    return new ElasticResponse(response, httpResponse.getStatusLine().getStatusCode());
  }

  private HttpClient getHttpClient() {
    // Check if Basic Authentication need to be used
    if (StringUtils.isNotBlank(getEsUsernameProperty())) {
      DefaultHttpClient httpClient = new DefaultHttpClient();
      httpClient.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
                                                         new UsernamePasswordCredentials(getEsUsernameProperty(),
                                                                                         getEsPasswordProperty()));
      LOG.debug("Basic authentication for ES activated with username = {} and password = {}",
                getEsUsernameProperty(),
                getEsPasswordProperty());
      return httpClient;
    } else {
      LOG.debug("Basic authentication for ES not activated");
      return new DefaultHttpClient();
    }
  }

  protected abstract String getEsUsernameProperty();

  protected abstract String getEsPasswordProperty();

}
