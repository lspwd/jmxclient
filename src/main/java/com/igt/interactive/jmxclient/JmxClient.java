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

public class JmxClient {

    String mbeanname;
    String mbeanattr;
    String mbeanvalue;

    public JmxClient(String mbeanname, String mbeanattr, String mbeanvalue ) {

        this.mbeanattr = mbeanattr;
        this.mbeanname = mbeanname;
        this.mbeanvalue = mbeanvalue;
    }

    public String getMbeanname() {
        return mbeanname;
    }

    public String getMbeanattr() {
        return mbeanattr;
    }

    public String getMbeanvalue() {
        return mbeanvalue;
    }



}
