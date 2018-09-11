package com.hhly.crawlcore.sport.entity;

/**
 * @author lgs on
 * @version 1.0
 * @desc 动态代理ip实体
 * @date 2017/5/19.
 * @company 益彩网络科技有限公司
 */
public class DynamicIp {
    private String ip;
    private Integer port;
    private Integer tel;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getTel() {
        return tel;
    }

    public void setTel(Integer tel) {
        this.tel = tel;
    }
}
