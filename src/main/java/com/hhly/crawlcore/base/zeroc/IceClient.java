package com.hhly.crawlcore.base.zeroc;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hhly.comm.Const;
import com.hhly.utils.YbfStr;

import Ice.Communicator;
import Ice.InitializationData;
import Ice.ObjectPrx;
import Ice.Util;


public class IceClient {
    protected Logger logger = LoggerFactory.getLogger(IceClient.class);

    private Map<String, ObjectPrx> objPrxyMap;
    private boolean available = false;

    /**
     * 初始化ice连接
     *
     * @param connectionParam
     */
    private Communicator setCommunicator(ConnectionParam connectionParam, String objKey) {
        Communicator communicator = null;
        try {
            InitializationData initData = new InitializationData();
            initData.properties = Util.createProperties();
            initData.properties.load(connectionParam.getClientCfgClassPath());

            //随机排序再拼接
            String surl = connectionParam.getSeverUrl().toLowerCase();
            String[] arr = surl.split(Const.COLON);
            List<String> list = Arrays.asList(arr);
            Collections.shuffle(list);
            surl = YbfStr.join(list.toArray(), Const.COLON);

            StringBuilder sbld = new StringBuilder();
            sbld.append(objKey)
                    .append(":")
                    .append(surl);

            initData.properties.setProperty(objKey + ".Proxy", sbld.toString());
            //初始化ICE运行时
            communicator = Util.initialize(initData);
        } catch (Exception e) {
            logger.error("ice server initialize error:" + e.getMessage());
            throw new RuntimeException(e);
        }
        return communicator;
    }


    /**
     * 创建代理对象
     *
     * @param connectionParam 配置对象
     * @return
     */
    private Map<String, Object> buildProxy(ConnectionParam connectionParam, String objKey) {
        Map<String, Object> retMap = new HashMap<>();
        ObjectPrx objPrx = null;
        Communicator communicator = setCommunicator(connectionParam, objKey);
        try {
            objPrx = communicator.propertyToProxy(objKey + ".Proxy")
                    .ice_twoway()
                    .ice_timeout(connectionParam.getTimeOut());
        } catch (Exception e) {
            logger.error("execute proxy cause error:" + e.getMessage());
            throw new RuntimeException(e);
        }
        retMap.put("proxy", objPrx);
        retMap.put("communicator", communicator);
        return retMap;
    }

    /**
     * 初始化客户端，在创建客户端对象后调用
     *
     * @param connectionParam 封装链接参数
     * @param prxHelperMap    key=ObjKey 对象的键，必须跟服务端一样, value=生成客户端代理对象的帮助类，由Silce自动生成的名称是XXXPrxHelper的类
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws java.lang.reflect.InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public void initClientPrxs(ConnectionParam connectionParam, Map<String, Class> prxHelperMap) throws Exception {
        this.objPrxyMap = new HashMap<String, ObjectPrx>();
        Iterator<Entry<String, Class>> iter = prxHelperMap.entrySet().iterator();
        Map<String, Object> retMap = null;

        Entry<String, Class> entry = null;
        Method method = null;
        ObjectPrx objectPrx = null;
        while (iter.hasNext()) {
            entry = iter.next();
            method = entry.getValue().getDeclaredMethod("checkedCast", ObjectPrx.class);
            try {
                retMap = buildProxy(connectionParam, entry.getKey());
                objectPrx = (ObjectPrx) method.invoke(entry.getValue(), retMap.get("proxy"));
            } catch (Exception e) {
                logger.error("Create Ice Client Proxy Error :" + e.toString());
                Communicator communicator =  ((Communicator) (retMap != null ? retMap.get("communicator") : null));
                if(communicator != null){
                	communicator.shutdown();
                	communicator.destroy();
                }
                throw (e);
            }
            this.objPrxyMap.put(entry.getKey(), objectPrx);
        }
    }

    /**
     * 初始化客户端
     *
     * @param connectionParam
     * @throws Exception
     */
    public void initClientPrxs(ConnectionParam connectionParam) throws Exception {
        Map<String, Class> prxHelperMap = new HashMap<String, Class>();
        Set<Entry<String, String>> entrySet = connectionParam.getPrxNames().entrySet();
        for (Entry<String, String> entry : entrySet) {
            prxHelperMap.put(entry.getKey(), Class.forName(entry.getValue()));
        }
        initClientPrxs(connectionParam, prxHelperMap);
    }

    /**
     * 从客户端中获取代理对象，需要在客户端初始化之后才可以调用
     *
     * @param objKey 对象键要跟服务端完全相同
     * @return
     */
    public ObjectPrx getPrxByKey(String objKey) {
        return this.objPrxyMap.get(objKey);
    }

    /**
     * 销毁一个代理对象
     *
     * @param objPrx 要销毁的客户端代理对象
     */
    public void destoryProxy(ObjectPrx objPrx) {
        if (objPrx != null) {
            try {
                objPrx.ice_getCommunicator().shutdown();
                objPrx.ice_getCommunicator().destroy();
            } catch (Exception e) {
                logger.error("close communicator error" + e.getMessage());
            }
        }
    }

    /**
     * 销毁该Client中所有代理对象
     */
    public void destoryProxy() {
        Iterator<Entry<String, ObjectPrx>> iter = objPrxyMap.entrySet().iterator();

        Entry<String, ObjectPrx> entry = null;
        while (iter.hasNext()) {
            entry = iter.next();
            destoryProxy(entry.getValue());
        }
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

}
