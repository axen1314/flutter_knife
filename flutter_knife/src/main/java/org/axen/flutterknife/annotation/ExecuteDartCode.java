package org.axen.flutterknife.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExecuteDartCode {
    String engineId() default "";
    String initialRoute() default "";
    String library() default "";
    String entrypoint() default "";
    String pathToBundle() default "";
}
