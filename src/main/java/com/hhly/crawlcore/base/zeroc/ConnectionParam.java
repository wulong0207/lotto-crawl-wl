package com.hhly.crawlcore.base.zeroc;

import java.util.Map;

public class ConnectionParam {
    public static String SSL = "ssl";
    public static String TCP = "tcp";
    private Map<String, String> prxNames;
    private String clientCfgClassPath;
    private String severUrl;
    private int timeOut;

    public Map<String, String> getPrxNames() {
        return prxNames;
    }

    public void setPrxNames(Map<String, String> prxNames) {
        this.prxNames = prxNames;
    }

    public String getClientCfgClassPath() {
        return clientCfgClassPath;
    }

    public void setClientCfgClassPath(String clientCfgClassPath) {
        this.clientCfgClassPath = clientCfgClassPath;
    }

    public String getSeverUrl() {
        return severUrl;
    }

    public void setSeverUrl(String severUrl) {
        this.severUrl = severUrl;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

}
