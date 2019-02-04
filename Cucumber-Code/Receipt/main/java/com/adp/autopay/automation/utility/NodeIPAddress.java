package com.adp.autopay.automation.utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;


@SuppressWarnings("deprecation")
public class NodeIPAddress {

  @SuppressWarnings("resource")
@Test
//  public void testRemoteIP() throws MalformedURLException {
//    new DesiredCapabilities();
//    DesiredCapabilities cap = DesiredCapabilities.firefox();
//    RemoteWebDriver remoteDriver = new RemoteWebDriver(new URL("http://CDLVDIW700875:4444/wd/hub"), cap);
//        // 192.168.96.22
//        
//    System.out.println(getIPOfNode(remoteDriver));
//  }

  public static String getIPOfNode(RemoteWebDriver remoteDriver) {
    String hostFound = null;
    try {
      HttpCommandExecutor ce = (HttpCommandExecutor) remoteDriver.getCommandExecutor();
      String hostName = ce.getAddressOfRemoteServer().getHost();
      int port = ce.getAddressOfRemoteServer().getPort();
      HttpHost host = new HttpHost(hostName, port);
      DefaultHttpClient client = new DefaultHttpClient();
      URL sessionURL = new URL("http://" + hostName + ":" + port
        + "/grid/api/testsession?session=" + remoteDriver.getSessionId());
      BasicHttpEntityEnclosingRequest r = new BasicHttpEntityEnclosingRequest(
          "POST", sessionURL.toExternalForm());
      HttpResponse response = client.execute(host, r);
      JSONObject object = extractObject(response);
      URL myURL = new URL(object.getString("proxyId"));
      if ((myURL.getHost() != null) && (myURL.getPort() != -1)) {
        hostFound = myURL.getHost();
      }
    } catch (Exception e) {
      System.err.println(e);
    }
    return hostFound;
  }

  private static JSONObject extractObject(HttpResponse resp) throws IOException, JSONException {
    InputStream contents = resp.getEntity().getContent();
    StringWriter writer = new StringWriter();
    IOUtils.copy(contents, writer, "UTF8");
    JSONObject objToReturn = new JSONObject(writer.toString());
    return objToReturn;
  }
}
