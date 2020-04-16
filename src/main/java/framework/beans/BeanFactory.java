package framework.beans;

import framework.web.mvc.Controller;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description:初始化bean、获得bean
 * create time: 20:45 2020/4/16
 * @author ZhangJunjie
 */
public class BeanFactory {

    /**
     * description:保存Bean实例的映射集合(IOC容器)
     * create time: 20:44 2020/4/16
     */
    private static Map<Class<?>, Object> classToBean = new ConcurrentHashMap<>();

    /**
     * 根据class类型获取bean
     */
    public static Object getBean(Class<?> cls){
        return classToBean.get(cls);
    }

    /**
     * 初始化bean工厂
     */
    public static void initBean(List<Class<?>> classList) throws Exception {
        //先创建一个.class文件集合的副本
        List<Class<?>> toCreate = new ArrayList<>(classList);
        //循环创建bean实例
        while(toCreate.size() != 0){
            //记录开始时集合大小，如果一轮结束后大小没有变证明有相互依赖
            int remainSize = toCreate.size();
            //遍历创建bean，如果失败就先跳过，等下一轮再创建
            for(int i = 0; i < toCreate.size(); i++){
                if(finishCreate(toCreate.get(i))){
                    toCreate.remove(i);
                }
            }
            //有相互依赖的情况先抛出异常
            if(toCreate.size() == remainSize){
                throw new Exception("cycle dependency!");
            }
        }
    }

    private static boolean finishCreate(Class<?> cls) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        //创建的bean实例仅包括Bean和Controller注释的类
        if(!cls.isAnnotationPresent(Bean.class) && !cls.isAnnotationPresent(Controller.class)){
            return true;
        }
        //先创建实例对象
        Object bean = cls.getConstructor().newInstance();
        //看看实例对象是否需要执行依赖注入，注入其他bean
        for(Field field : cls.getDeclaredFields()){
            if(field.isAnnotationPresent(AutoWired.class)){
                Class<?> fieldType = field.getType();
                Object reliantBean = BeanFactory.getBean(fieldType);
                //如果要注入的bean还未被创建就先跳过
                if(reliantBean == null){
                    return false;
                }
                field.setAccessible(true);
                field.set(bean, reliantBean);
            }
        }
        classToBean.put(cls, bean);
        return true;
    }
}
