package framework.web.servlet;

import framework.web.handler.HandlerManager;
import framework.web.handler.MappingHandler;

import javax.servlet.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * description: MVC核心业务
 * create time: 2:24 2020/4/17
 * @author ZhangJunjie
 */
public class DispatcherServlet implements Servlet {

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {

        for(MappingHandler mappingHandler : HandlerManager.mappingHandlerList){
            try{
                if (mappingHandler.handle(servletRequest, servletResponse)){
                    return;
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
