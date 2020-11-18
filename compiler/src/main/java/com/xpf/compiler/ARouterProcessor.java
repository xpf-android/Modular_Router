package com.xpf.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.xpf.annotation.BindPath;
import com.xpf.compiler.utils.Constants;
import com.xpf.compiler.utils.EmptyUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

// AutoService则是固定的写法，加个注解即可
// 通过auto-service中的@AutoService可以自动生成AutoService注解处理器，用来注册
// 用来生成 META-INF/services/javax.annotation.processing.Processor 文件
@AutoService(Processor.class)
// 允许/支持的注解类型，让注解处理器处理
@SupportedAnnotationTypes({Constants.ROUTER_ANNOTATION_TYPE})
// 指定JDK编译版本
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ARouterProcessor extends AbstractProcessor {

    // 操作Element工具类 (类、函数、属性都是Element)
    private Elements elementUtils;

    // type(类信息)工具类，包含用于操作TypeMirror的工具方法
    private Types typeUtils;

    // Messager用来报告错误，警告和其他提示信息
    private Messager messager;

    // 文件生成器 类/资源，Filter用来创建新的源文件，class文件以及辅助文件
    private Filer filer;

    Map<String, String> tempMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        typeUtils = processingEnv.getTypeUtils();
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
    }

    /**
     * PackageElement 包节点
     * TypeElement 类节点
     * ExecutableElement 方法节点
     * VariableElement 属性节点
     *
     * @param set
     * @param roundEnv
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {

        //一旦类上面使用了@ARouter注解
        if (!EmptyUtils.isEmpty(set)) {
            //获取所有被@ARouter注解的元素集合
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(BindPath.class);
            if (!EmptyUtils.isEmpty(elements)) {
                //解析元素
                try {
                    parseElements(elements);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //坑，必须写返回值，表示处理@ARouter注解完成
            return true;
        }

        return false;
    }

    private void parseElements(Set<? extends Element> elements) throws IOException {
        for (Element element : elements) {
            //获取被注解元素的类节点
            TypeElement typeElement = (TypeElement) element;
            //ARouter注解中的path属性的值
            String key = typeElement.getAnnotation(BindPath.class).path();
            //获取全类名
            String className = typeElement.getQualifiedName().toString();
            tempMap.put(key, className +".class");
        }
        
        //生成文件
        if (tempMap.size() > 0) {
            createFile();
        }
    }

    /**
     * public class ActivityUtil implements IRouter {
     *
     *     @Override
     *     public void putActivity() {
     *         ARouter.getInstance().addActivity("/personal/Personal_MainActivity", Personal_MainActivity.class);
     *     }
     * }
     */
    private void createFile() throws IOException {

        MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder(Constants.METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);

        Iterator<String> iterator = tempMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String className = tempMap.get(key);
            methodSpecBuilder.addStatement(
                    "com.xpf.router.ARouter.getInstance().addActivity(\"" +
                            key + "\"," + className + ")"
            );
        }
        MethodSpec methodSpec = methodSpecBuilder.build();

        //获取到接口的类
        ClassName iRouter = ClassName.get("com.xpf.router", "IRouter");

        TypeSpec typeSpec = TypeSpec.classBuilder("ActivityUtil" + System.currentTimeMillis())
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(iRouter)
                .addMethod(methodSpec)
                .build();

        JavaFile.builder("com.my.apt", typeSpec)
                .build()
                .writeTo(filer);

    }
}
