package com.igt.interactive.jmxclient;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;

@RestController
public class JxmClientController {

    /*

    Example default usage

    String mbean = "org.apache.camel:context=esteAdapterCamelContext,type=routes,name=\"esteRoute\"";
    String attribute = "ExchangesCompleted";
    String ip = "156.24.66.96";
    String port = "9004";

    */

    @RequestMapping("/checkjmx")
    public JmxClient checkjmx(

            @RequestParam(value = "mbean", defaultValue = "org.apache.camel:context=esteAdapterCamelContext,type=routes,name=\"esteRoute\"") String mbean,
            @RequestParam(value = "attribute", defaultValue = "ExchangesCompleted") String attribute,
            @RequestParam(value = "ip", defaultValue = "156.24.66.96") String ip,
            @RequestParam(value = "port", defaultValue = "9004") String port) {

        // establish JMX Connection
        // Fetch The Value From Mbean
        // Construct The response object and return

        JMXConnector jmxConnector = null;

        try {
            jmxConnector = JxmClientController.jmxConn(ip, port);
            MBeanServerConnection mbserver = JxmClientController.getConn(jmxConnector);
            Object value = JxmClientController.getAttr(mbean, mbserver, attribute);
            // System.out.println("The Value: " +value.toString());
            return new JmxClient(mbean, attribute, value.toString());

        }

        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (jmxConnector != null) {
                try {
                    jmxConnector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new JmxClient("Exception Has Been Caught", "Exception Has Been Caught", "Exception Has Been Caught");
    }

    private static Object getAttr(String objname, MBeanServerConnection mbsc, String attributename ) throws Exception {
        try {

            // System.out.println(objname);
            // System.out.println(objname);
            // System.out.println(objname);

            ObjectName name = new ObjectName(objname);
            return  ( Object ) mbsc.getAttribute(name, attributename);
        }
        catch(Exception e){
            throw e;
        }

    }
    private  static JMXConnector jmxConn(String ip, String port) throws Exception {
        try {
            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + ip + ":" + port + "/jmxrmi");
            JMXConnector jmxcon = JMXConnectorFactory.newJMXConnector(url, null);
            jmxcon.connect();
            return jmxcon;
        } catch (Exception e) {
            throw e;
        }
    }

    private static MBeanServerConnection getConn( JMXConnector jconn ) throws Exception {

        try {
            MBeanServerConnection mbcon = jconn.getMBeanServerConnection();
            return mbcon;
        }
        catch (Exception e) {
            throw e;
        }
    }
 }
