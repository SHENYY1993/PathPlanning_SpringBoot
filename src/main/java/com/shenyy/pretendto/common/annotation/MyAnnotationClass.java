package com.shenyy.pretendto.common.annotation;

import com.shenyy.pretendto.core.biz.impl.BookBizImpl;
import com.shenyy.pretendto.core.model.table.EBook;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@MyAnnotation("My Annotation")
public class MyAnnotationClass {

    public static void main(String[] args) {
        for (Annotation annotation :
                MyAnnotationClass.class.getAnnotations()) {
            System.out.println(annotation.annotationType());
            for (Method method :
                    annotation.annotationType().getMethods()) {
                    System.out.println(method.getName());
            }
        }

        System.out.println("=================");
        Class reflectClass = MyAnnotationClass.class;
        for (Method method :
                reflectClass.getMethods()) {
            System.out.println(method.getName());
        }

        System.out.println("=================");
        try {
            System.out.println(Class.class.newInstance());
//            System.out.println(MyAnnotationClass.class.getClass().newInstance().getClass()); //error: java.lang.IllegalAccessException: Can not call newInstance() on the Class for java.lang.Class
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println("=================");
        for (Method method :
                EBook.class.getMethods()) {
            System.out.println(method.getName());
        }
        System.out.println("=================");
        for (Annotation annotation :
                EBook.class.getAnnotations()) {
            System.out.println(annotation.annotationType());
        }
        System.out.println("=================");
        for (Annotation annotation :
                BookBizImpl.class.getAnnotations()) {
            System.out.println(annotation.annotationType());
        }
    }
}
