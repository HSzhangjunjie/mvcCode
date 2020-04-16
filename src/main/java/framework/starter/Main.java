package framework.starter;


import framework.beans.BeanFactory;
import framework.core.ClassScanner;
import framework.web.handler.HandlerManager;
import framework.web.server.TomcatServer;

import java.util.List;

/**
 * description: 启动程序入口
 * create time: 2:39 2020/4/17
 * @author ZhangJunjie
 */
public class Main {

    public static void run(Class<?> cls, String[] args) {
        System.out.println("Hello World!");
        //跑起来Tomcat逻辑
        TomcatServer tomcatServer = new TomcatServer(args);
        try {
            //启动tomcat
            tomcatServer.startServer();
            //扫描启动类下所有的.class文件
            System.out.println(cls.getPackage().getName());
            List<Class<?>> classList = ClassScanner.scanClass(cls.getPackage().getName());
            //初始化bean工厂
            BeanFactory.initBean(classList);
            //解析所有.class文件，获得mappingHandler集合
            HandlerManager.resolveMappingHandler(classList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
