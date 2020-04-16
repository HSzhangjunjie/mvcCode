package framework.beans;

import java.lang.annotation.*;

/**
 * description:自动注入注解
 * create time: 20:43 2020/4/16
 * @author ZhangJunjie
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoWired {

}
