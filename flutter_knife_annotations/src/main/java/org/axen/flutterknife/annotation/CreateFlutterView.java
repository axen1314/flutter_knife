package org.axen.flutterknife.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.flutter.embedding.android.RenderMode;
import io.flutter.embedding.android.TransparencyMode;

/**
 * 为注解属性创建一个{@link io.flutter.embedding.android.FlutterView}实例
 * @deprecated 请使用 {@link org.axen.flutterknife.view.FlutterView}
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface CreateFlutterView {
    /**
     * 渲染模式，分三种情况
     * {@link RenderMode#surface}, 使用{@link io.flutter.embedding.android.FlutterSurfaceView}渲染页面
     * {@link RenderMode#texture}, 使用{@link io.flutter.embedding.android.FlutterTextureView}渲染页面
     * {@link RenderMode#surface}, 使用{@link io.flutter.embedding.android.FlutterSurfaceView}渲染页面
     */
    RenderMode renderMode() default RenderMode.surface;

    /**
     * 背景渲染默认，仅在使用{@link RenderMode#surface}的情况下生效
     */
    TransparencyMode transparencyMode() default TransparencyMode.transparent;
}
