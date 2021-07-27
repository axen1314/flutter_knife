package org.axen.flutterknife;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import org.axen.flutterknife.adapter.BindFlutterViewAdapter;
import org.axen.flutterknife.adapter.CreateFlutterViewAdapter;
import org.axen.flutterknife.adapter.IAnnotationToFlutterViewAdapter;
import org.axen.flutterknife.annotation.ExecuteDartCode;
import org.axen.flutterknife.flutter.FlutterExecutor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import io.flutter.embedding.android.FlutterView;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;

public class FlutterKnife {
    private final List<IAnnotationToFlutterViewAdapter> annotationAdapterList;
    private FlutterExecutor flutterExecutor;

    private FlutterKnife() {
        annotationAdapterList = new ArrayList<>();
        annotationAdapterList.add(new BindFlutterViewAdapter());// BindFlutterView
        annotationAdapterList.add(new CreateFlutterViewAdapter());// CreateFlutterView
    }

    public static FlutterKnife getInstance() { return Holder.INSTANCE; }

    public void bind(@NonNull Activity activity) {
        bind(activity, null);
    }

    /**
     * 初始化Activity内带有{@link org.axen.flutterknife.annotation.BindFlutterView}
     * 或{@link org.axen.flutterknife.annotation.CreateFlutterView}注解的FlutterView属性
     * 为带有{@link ExecuteDartCode}注解的FlutterView属性绑定一个FlutterEngine实例，并执行Dart入口代码
     *
     * 注意：FlutterKnife会注册一个{@link androidx.lifecycle.LifecycleObserver}进行声明周期监听
     * 因此使用FlutterKnife进行FlutterView绑定的Activity必须实现{@link LifecycleOwner}
     * 为了保证兼容，FlutterKnife提供了{@link org.axen.flutterknife.ui.FlutterKnifeActivity}，
     * 实现了{@link LifecycleOwner}接口并实现其生命周期回调
     *
     * @param activity FlutterView所在的上下文环境
     * @param callback {@link EngineCallback}实例，回调创建的FlutterEngine实例
     */
    public void bind(@NonNull Activity activity, @Nullable EngineCallback callback) {
        try {
            Class<? extends Activity> clazz = activity.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for(Field field : fields) {
                Class<?> fieldClass = field.getType();
                if (FlutterView.class.isAssignableFrom(fieldClass)) {
                    field.setAccessible(true);
                    FlutterView view = (FlutterView) field.get(activity);
                    if (view == null) {
                        // 根据BindFlutterView或CreateFlutterView注解信息创建FlutterView
                        for(IAnnotationToFlutterViewAdapter adapter : annotationAdapterList) {
                            view = adapter.convert(activity, field);
                            if (view != null) {
                                field.set(activity, view);
                                break;
                            }
                        }
                    }
                    if (view != null) {// 根据ExecuteDartCode注解信息实例化FlutterEngine并执行Dart代码
                        ExecuteDartCode code = field.getAnnotation(ExecuteDartCode.class);
                        if (code != null) executeDartCode(activity, view, code, callback);
                    }
                }

            }
        } catch (IllegalAccessException ignored) {}
    }

    /**
     * 初始化FlutterEngine并执行Dart入口代码
     *
     * 注意：FlutterKnife会注册一个{@link androidx.lifecycle.LifecycleObserver}进行声明周期监听
     * 因此使用FlutterKnife进行FlutterView绑定的Activity必须实现{@link LifecycleOwner}
     * 为了保证兼容，FlutterKnife提供了{@link org.axen.flutterknife.ui.FlutterKnifeActivity}，
     * 实现了{@link LifecycleOwner}接口并实现其生命周期回调
     *
     * @param activity FlutterView所在的上下文环境
     * @param view 需要绑定FlutterEngine的FlutterView
     * @param code {@link ExecuteDartCode}注解实例，包含了Dart入口代码的相关信息
     * @param callback {@link EngineCallback}实例，回调创建的FlutterEngine实例
     */
    private void executeDartCode(@NonNull Activity activity,
                                 @NonNull FlutterView view,
                                 @NonNull ExecuteDartCode code,
                                 @Nullable EngineCallback callback) {
        String engineId = code.engineId();
        FlutterEngineCache cache = FlutterEngineCache.getInstance();
        // 如果有engineId，则直接使用缓存的FlutterEngine实例，
        // 否则实例化一个新的FlutterEngine实例
        final FlutterEngine engine = engineId.isEmpty() || !cache.contains(engineId)
                ? new FlutterEngine(activity) : cache.get(engineId);
        if (engine != null) {
            view.attachToFlutterEngine(engine);
            // FlutterEngine实例创建回调
            if (callback != null) callback.onEngineCreate(view, engine);
            if (flutterExecutor == null) flutterExecutor = new FlutterExecutor();
            // 注册生命周期监听函数
            Lifecycle lifecycle = ((LifecycleOwner)activity).getLifecycle();
            lifecycle.addObserver(new DefaultLifecycleObserver() {
                @Override
                public void onCreate(@NonNull LifecycleOwner owner) {}

                @Override
                public void onStart(@NonNull LifecycleOwner owner) {
                    // 开始执行Dart代码
                    if (!engine.getDartExecutor().isExecutingDart()){
                        flutterExecutor
                                .pathToBundle(code.pathToBundle())
                                .initialRoute(code.initialRoute())
                                .library(code.library())
                                .entrypoint(code.entrypoint())
                                .execute(engine);
                    }
                }

                @Override
                public void onResume(@NonNull LifecycleOwner owner) {
                    engine.getLifecycleChannel().appIsResumed();
                }

                @Override
                public void onPause(@NonNull LifecycleOwner owner) {
                    engine.getLifecycleChannel().appIsInactive();
                }

                @Override
                public void onStop(@NonNull LifecycleOwner owner) {
                    engine.getLifecycleChannel().appIsPaused();
                }

                @Override
                public void onDestroy(@NonNull LifecycleOwner owner) {
                    // 释放资源
                    view.detachFromFlutterEngine();
                    engine.getLifecycleChannel().appIsDetached();
                    lifecycle.removeObserver(this);
                }
            });
        }

    }

    private static final class Holder {
        private static final FlutterKnife INSTANCE = new FlutterKnife();
    }

    public interface EngineCallback {
        void onEngineCreate(FlutterView view, FlutterEngine engine);
    }
}
