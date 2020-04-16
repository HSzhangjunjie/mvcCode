package framework.core;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassScanner {

    public static List<Class<?>> scanClass(String packageName) throws IOException, ClassNotFoundException {
        //用于保存结果的容器
        List<Class<?>> classList = new ArrayList<>();
        //把文件名改为文件路径
        String path = packageName.replace(".", "/");
        //获取默认的类加载器
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        //通过文件路径获取该文件夹下所有资源的URL
        Enumeration<URL> resources = classLoader.getResources(path);


        while (resources.hasMoreElements()) {
            //拿到下一个资源
            URL resource = resources.nextElement();
            //先判断是否是jar包，因为默认.class文件会被打包为jar包
            if (resource.getProtocol().contains("jar")) {
                //把URL强转为jar包链接
                JarURLConnection urlConnection = (JarURLConnection) resource.openConnection();
                //根据jar包获取jar包的路径名
                String jarFilePath = urlConnection.getJarFile().getName();
                //把jar包下所有的类添加的保存结果的容器中
                classList.addAll(getClassFromJar(jarFilePath, path));
            } else {
                String str = "e:/ideaproject/maventest/mvcCode/build/libs/test.jar";
                URL url = new File(str).toURI().toURL();
                String jarStr = "jar:" + url.toExternalForm() + "!/";
                resource = new URL(jarStr);
                //把URL强转为jar包链接
                JarURLConnection urlConnection = (JarURLConnection) resource.openConnection();
                //根据jar包获取jar包的路径名
                String jarFilePath = urlConnection.getJarFile().getName();
                //把jar包下所有的类添加的保存结果的容器中
                classList.addAll(getClassFromJar(jarFilePath, path));
            }
        }
        return classList;
    }

    /**
     * 获取jar包中所有路径符合的类文件
     */
    private static List<Class<?>> getClassFromJar(String jarFilePath, String path) throws IOException, ClassNotFoundException {
        //保存结果的集合
        List<Class<?>> classes = new ArrayList<>();
        //创建对应jar包的句柄
        JarFile jarFile = new JarFile(jarFilePath);
        //拿到jar包中所有的文件
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            //拿到一个文件
            JarEntry jarEntry = jarEntries.nextElement();
            //拿到文件名，大概是这样：com/xxxx/xxxxx/xxxx.class
            String entryName = jarEntry.getName();
            //判断是否是类文件
            if (entryName.startsWith(path) && entryName.endsWith(".class")) {
                String classFullName = entryName.replace("/", ".")
                        .substring(0, entryName.length() - 6);
                classes.add(Class.forName(classFullName));
            }
        }
        return classes;
    }
}
