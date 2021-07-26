package org.axen.flutterknife.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.flutter.embedding.android.RenderMode;
import io.flutter.embedding.android.TransparencyMode;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CreateFlutterView {
    RenderMode renderMode() default RenderMode.texture;
    TransparencyMode transparencyMode() default TransparencyMode.transparent;
}
