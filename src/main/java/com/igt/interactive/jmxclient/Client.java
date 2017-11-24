package com.igt.interactive.jmxclient;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {

    private static Object getAttr( String objname, MBeanServerConnection mbsc, String attributename ) throws Exception {
        try {
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


    public static void main(String[] args) {

        JMXConnector jmxConnector = null;

        try {

            File file = new File("src/main/resources/address.properties");
            FileInputStream fis = new FileInputStream(file);
            Properties p = new Properties();
            p.load(fis);
            fis.close();
            String ipaddress = p.getProperty("ipaddress");
            String port = p.getProperty("port");
            System.out.println(ipaddress + " " + port);
            List<HashMap> list = new ArrayList<HashMap>();
            Integer intIndex = 1;

            Enumeration en = p.propertyNames();
            while (en.hasMoreElements()) {
                String element = (String) en.nextElement();
                if (element.startsWith("mbean")) {
                    HashMap<String, String> map = new HashMap();
                    String regex = "mbean.(\\d+).(\\w+)";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(element);
                    if (matcher.find() && Integer.parseInt(matcher.group(1)) == intIndex ) {
                        String index = matcher.group(1);
                        String prop_name = p.getProperty("mbean." + index + ".name");
                        String prop_attribute = p.getProperty("mbean." + index + ".attribute");
                        map.put("name", prop_name);
                        map.put("attribute", prop_attribute);
                        list.add(map);
                        intIndex++;
                    }
                    else
                        continue;
                    }
                }

                jmxConnector = Client.jmxConn(ipaddress, port);

                for ( HashMap m : list ) {
                        if ( m.containsKey("name") ) {
                            Object attr = Client.getAttr(( String )m.get("name"), Client.getConn(jmxConnector), ( String ) m.get("attribute") );
                            String out = "Mbean: " +m.get("name") +"\n";
                            out += "Attribute: " +m.get("attribute") + " ";
                            out += "has a value of: " +attr.toString();
                            System.out.println(out);
                        //System.out.println("Chiave della mappa: " +key.toString() +" con valore: " + m.get(key) );

                    }
                }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if ( jmxConnector != null ) {
                try {
                    jmxConnector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
