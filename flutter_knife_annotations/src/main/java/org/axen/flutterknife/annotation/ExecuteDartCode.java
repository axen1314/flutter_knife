package org.axen.flutterknife.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExecuteDartCode {

    /**
     * FlutterEngine缓存ID，默认值为空
     * 不传空将通过FlutterEngineCache获取缓存实例
     */
    String engineId() default "";

    /**
     * Flutter初始化路由，传空则默认使用“/”为初始化值
     */
    @Deprecated
    String initialRoute() default "";

    /**
     * Dart入口方法所在包名，默认为空
     */
    String library() default "";

    /**
     * Dart入口方法名，传空则默认使用“main”为入口方法名
     */
    String entrypoint() default "";

    /**
     * Dart代码包所在路径，
     * 传空则会被设为{@link io.flutter.embedding.engine.loader.FlutterApplicationInfo#flutterAssetsDir}的值
     */
    String pathToBundle() default "";
}
