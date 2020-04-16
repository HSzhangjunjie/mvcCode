package framework.web.mvc;

import java.lang.annotation.*;

/**
 * @author ZhangJunjie
 * create time: 2:24 2020/4/17
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestMapping {
    String value();
}
