package framework.web.handler;


import framework.web.mvc.RequestParam;
import framework.web.mvc.Controller;
import framework.web.mvc.RequestMapping;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class HandlerManager {

    public static List<MappingHandler> mappingHandlerList = new ArrayList<>();

    /**
     * 处理类文件集合，挑出MappingHandler
     */
    public static void resolveMappingHandler(List<Class<?>> classList) {
        System.out.println("测试有多少个.class文件：" + classList.size());
        for (Class<?> cls : classList) {
            //MappingHandler会在controller里面
            if (cls.isAnnotationPresent(Controller.class)) {
                //继续从controller中分离出一个个MappingHandler
                parseHandlerFromController(cls);
            }
        }
    }

    /**
     * description:MVC核心注解注册逻辑
     * create time: 2:40 2020/4/17
     */
    private static void parseHandlerFromController(Class<?> cls) {
        //先获取该controller中所有的方法
        Method[] methods = cls.getDeclaredMethods();
        //从中挑选出被RequestMapping注解的方法进行封装
        for (Method method : methods) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }
            //拿到RequestMapping定义的uri
            String uri = method.getDeclaredAnnotation(RequestMapping.class).value();
            //保存方法参数的集合
            List<String> paramNameList = new ArrayList<>();
            for (Parameter parameter : method.getParameters()) {
                //把有被RequestParam注解的参数添加入集合
                if (parameter.isAnnotationPresent(RequestParam.class)) {
                    paramNameList.add(parameter.getDeclaredAnnotation(RequestParam.class).value());
                }
            }
            //把参数集合转为数组，用于反射
            String[] params = paramNameList.toArray(new String[paramNameList.size()]);
            //反射生成MappingHandler
            MappingHandler mappingHandler = new MappingHandler(uri, method, cls, params);
            //把mappingHandler装入集合中
            mappingHandlerList.add(mappingHandler);
        }
    }
}
