package com.xpf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



//SOURCE--->  .java   //源码期
//CLASS--->   .class  //编译期
//RUNTIME---> .dex    //运行期(一直有效)
@Target(ElementType.TYPE) //声明注解的作用域
@Retention(RetentionPolicy.CLASS) //声明注解的生命周期 存在时间
public @interface BindPath {
    String path();
}
