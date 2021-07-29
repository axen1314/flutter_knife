package org.axen.flutterknife.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用 {@link android.app.Activity#findViewById(int)}与注解属性绑定
 * @deprecated Gradle 7.0以后将不推荐使用Resource ID绑定
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface BindFlutterView {
    int value();
}
