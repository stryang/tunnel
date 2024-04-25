package com.tunnel.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author yq
 * @since 2024/4/16
 */
@Slf4j
public class ClassUtils {

    private static final Map<Class<?>, Object> CLASS_INSTANCE = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T getInstance(Class<T> clazz) {
        return (T) CLASS_INSTANCE.computeIfAbsent(clazz, ClassUtils::newInstance);
    }

    static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static List<Class<?>> findAnnotated(String packageName, Class<? extends Annotation> annotationClass) {
        try {
            return findAnnotatedInterfaces(packageName, annotationClass);
        } catch (IOException | ClassNotFoundException e) {
            return Collections.emptyList();
        }
    }

    public static List<Class<?>> findAnnotatedInterfaces(String packageName, Class<? extends Annotation> annotationClass) throws IOException, ClassNotFoundException {
        List<Class<?>> annotatedInterfaces = new ArrayList<>();

        // 获取类加载器
        ClassLoader classLoader = annotationClass.getClassLoader();

        // 将包名称转换为文件路径
        String packagePath = packageName.replace('.', '/');

        // 获取包的资源 URL
        URL resource = classLoader.getResource(packagePath);
        if (resource == null) {
            System.err.println("Package not found: " + packageName);
            return annotatedInterfaces;
        }

        // 处理文件系统路径
        File directory = new File(URLDecoder.decode(resource.getFile(), "UTF-8"));
        if (directory.exists() && directory.isDirectory()) {
            // 递归搜索目录下的类文件
            findAnnotatedClassesInDirectory(directory, packageName, annotationClass, annotatedInterfaces);
        } else {
            // 处理 JAR 文件中的类文件
            JarFile jarFile = new JarFile(new File(URLDecoder.decode(resource.getFile(), "UTF-8")));
            findAnnotatedClassesInJarFile(jarFile, packageName, annotationClass, annotatedInterfaces);
        }

        return annotatedInterfaces;
    }

    private static void findAnnotatedClassesInDirectory(File directory, String packageName, Class<? extends Annotation> annotationClass, List<Class<?>> annotatedInterfaces) throws ClassNotFoundException {
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                // 递归搜索子目录
                findAnnotatedClassesInDirectory(file, packageName + "." + file.getName(), annotationClass, annotatedInterfaces);
            } else if (file.getName().endsWith(".class")) {
                // 查找类文件
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                Class<?> clazz = Class.forName(className);
                // 检查是否是接口并被注解标记
                if (clazz.isInterface() && clazz.isAnnotationPresent(annotationClass)) {
                    annotatedInterfaces.add(clazz);
                }
            }
        }
    }

    private static void findAnnotatedClassesInJarFile(JarFile jarFile, String packageName, Class<? extends Annotation> annotationClass, List<Class<?>> annotatedInterfaces) throws ClassNotFoundException, IOException {
        // 获取包路径
        String packagePath = packageName.replace('.', '/');
        for (JarEntry entry : Collections.list(jarFile.entries())) {
            String entryName = entry.getName();
            if (entryName.startsWith(packagePath) && entryName.endsWith(".class")) {
                // 将文件路径转换为类名
                String className = entryName.replace('/', '.').substring(0, entryName.length() - 6);
                Class<?> clazz = Class.forName(className);
                // 检查是否是接口并被注解标记
                if (clazz.isInterface() && clazz.isAnnotationPresent(annotationClass)) {
                    annotatedInterfaces.add(clazz);
                }
            }
        }
    }

}
