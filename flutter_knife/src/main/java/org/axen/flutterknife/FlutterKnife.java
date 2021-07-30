package org.axen.flutterknife;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import org.axen.flutterknife.plugin.BindFlutterViewAnnotationPlugin;
import org.axen.flutterknife.plugin.CreateFlutterViewAnnotationPlugin;
import org.axen.flutterknife.plugin.ViewAnnotationPlugin;
import org.axen.flutterknife.annotation.ExecuteDartCode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import io.flutter.FlutterInjector;
import io.flutter.embedding.android.FlutterView;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.FlutterEngineGroup;
import io.flutter.embedding.engine.dart.DartExecutor;

public class FlutterKnife {
    private final List<ViewAnnotationPlugin> viewAnnotationPluginList;
    private FlutterEngineGroup engineGroup;

    private FlutterKnife() {
        viewAnnotationPluginList = new ArrayList<>();
        viewAnnotationPluginList.add(new BindFlutterViewAnnotationPlugin());// BindFlutterView
        viewAnnotationPluginList.add(new CreateFlutterViewAnnotationPlugin());// CreateFlutterView
    }

    public static FlutterKnife getInstance() { return Holder.INSTANCE; }

    public void bind(@NonNull Context context, @NonNull LifecycleOwner owner) {
        bind(context, owner, null);
    }

    /**
     * 初始化Activity内带有{@link org.axen.flutterknife.annotation.BindFlutterView}
     * 或{@link org.axen.flutterknife.annotation.CreateFlutterView}注解的FlutterView属性
     * 为带有{@link ExecuteDartCode}注解的FlutterView属性绑定一个FlutterEngine实例，并执行Dart入口代码
     *
     * @param context FlutterView所在的上下文环境
     * @param owner 组件生命周期回调
     * @param callback {@link EngineCallback}实例，回调创建的FlutterEngine实例
     */
    public void bind(@NonNull Context context, @NonNull LifecycleOwner owner, @Nullable EngineCallback callback) {
        try {
            Class<? extends Context> clazz = context.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for(Field field : fields) {
                FlutterView view = bindView(context, field);
                if (view != null) {// 根据ExecuteDartCode注解信息实例化FlutterEngine并执行Dart代码
                    ExecuteDartCode code = field.getAnnotation(ExecuteDartCode.class);
                    if (code != null) executeDartCode(context, owner, view, code, callback);
                }

            }
        } catch (IllegalAccessException ignored) {}
    }

    @Deprecated
    public void bind(@NonNull Activity activity) {
        bind(activity, (LifecycleOwner) activity);
    }

    /**
     * 初始化Activity内带有{@link org.axen.flutterknife.annotation.BindFlutterView}
     * 或{@link org.axen.flutterknife.annotation.CreateFlutterView}注解的FlutterView属性
     * 为带有{@link ExecuteDartCode}注解的FlutterView属性绑定一个FlutterEngine实例，并执行Dart入口代码
     *
     * @param activity FlutterView所在的上下文环境
     * @param callback {@link EngineCallback}实例，回调创建的FlutterEngine实例
     * @deprecated 该方法存在局限性，未来将被移除，请使用 {@link FlutterKnife#bind(Context, LifecycleOwner, EngineCallback)}代替
     */
    @Deprecated
    public void bind(@NonNull Activity activity, @Nullable EngineCallback callback) {
       bind(activity, (LifecycleOwner) activity, callback);
    }

    @VisibleForTesting
    public FlutterView bindView(Context context, Field field) throws IllegalAccessException {
        ensureFieldAccessible(field);
        return bindView(context, field, (FlutterView) field.get(context));
    }

    @VisibleForTesting
    public FlutterView bindView(Context context, String fieldName, FlutterView view) throws IllegalAccessException, NoSuchFieldException {
        Field field = context.getClass().getDeclaredField(fieldName);
        return bindView(context, field, view);
    }

    @VisibleForTesting
    public FlutterView bindView(Context context, Field field, FlutterView view) throws IllegalAccessException {
        ensureFieldAccessible(field);
        if (field.get(context) == null) {
            Class<FlutterView> ancestorClazz = FlutterView.class;
            if (ancestorClazz.isAssignableFrom(field.getType())) {
                if (view == null) {
                    int index = 0;
                    while (view == null && index < viewAnnotationPluginList.size()) {
                        ViewAnnotationPlugin plugin = viewAnnotationPluginList.get(index++);
                        view = plugin.convert(context, field);
                    }
                }
                if (view != null) field.set(context, view);
            }
        }
        return view;
    }

    /**
     * 初始化FlutterEngine并执行Dart入口代码
     * FlutterKnife会注册一个{@link androidx.lifecycle.LifecycleObserver}进行声明周期监听
     *
     * @param context FlutterView所在的上下文环境
     * @param view 需要绑定FlutterEngine的FlutterView
     * @param code {@link ExecuteDartCode}注解实例，包含了Dart入口代码的相关信息
     * @param callback {@link EngineCallback}实例，回调创建的FlutterEngine实例
     */
    private void executeDartCode(@NonNull Context context,
                                 @NonNull LifecycleOwner owner,
                                 @NonNull FlutterView view,
                                 @NonNull ExecuteDartCode code,
                                 @Nullable EngineCallback callback) {
        executeDartCode(context, owner, view, code.engineId(), code.pathToBundle(), code.library(), code.entrypoint(), callback);
    }

    @VisibleForTesting
    public void executeDartCode(@NonNull Context context,
                                @NonNull LifecycleOwner owner,
                                @NonNull FlutterView view,
                                @NonNull String entrypoint,
                                @Nullable EngineCallback callback) {
        executeDartCode(context, owner,view, "", entrypoint, callback);
    }

    @VisibleForTesting
    public void executeDartCode(@NonNull Context context,
                                @NonNull LifecycleOwner owner,
                                @NonNull FlutterView view,
                                @NonNull String engineId,
                                @NonNull String entrypoint,
                                @Nullable EngineCallback callback) {
        executeDartCode(context, owner, view, engineId, "", entrypoint, callback);
    }

    @VisibleForTesting
    public void executeDartCode(@NonNull Context context,
                                @NonNull LifecycleOwner owner,
                                @NonNull FlutterView view,
                                @NonNull String engineId,
                                @NonNull String library,
                                @NonNull String entrypoint,
                                @Nullable EngineCallback callback) {
        String pathToBundle = FlutterInjector.instance().flutterLoader().findAppBundlePath();
        executeDartCode(context, owner, view, engineId, pathToBundle, library, entrypoint, callback);
    }

    @VisibleForTesting
    public void executeDartCode(@NonNull Context context,
                                @NonNull LifecycleOwner owner,
                                @NonNull FlutterView view,
                                @NonNull String engineId,
                                @NonNull String pathToBundle,
                                @NonNull String library,
                                @NonNull String entrypoint,
                                @Nullable EngineCallback callback) {
        executeDartCode(context, owner, view, engineId, pathToBundle, "", library, entrypoint, callback);
    }

    /**
     * 将一个FlutterEngine实例与FlutterView绑定，并执行指定的Dart入口方法
     * @param context 上下文环境
     * @param view FlutterView实例
     * @param engineId FlutterEngine缓存ID，如果不为空将通过{@link FlutterEngineCache#get(String)}获取实例
     * @param pathToBundle Dart代码所在目录
     * @param initialRoute Flutter初始化路由名称
     * @param library Dart入口代码包名
     * @param entrypoint Dart入口方法名
     * @param callback FlutterEngine初始化回调函数
     * @deprecated initialRoute已失效，请使用${@link FlutterKnife#executeDartCode(Context, LifecycleOwner, FlutterView, String, String, String, String, EngineCallback)}
     */
    @VisibleForTesting
    @Deprecated
    public void executeDartCode(@NonNull Context context,
                                @NonNull LifecycleOwner owner,
                                @NonNull FlutterView view,
                                @NonNull String engineId,
                                @NonNull String pathToBundle,
                                @NonNull String initialRoute,
                                @NonNull String library,
                                @NonNull String entrypoint,
                                @Nullable EngineCallback callback) {
        // 如果有engineId，则直接使用缓存的FlutterEngine实例，
        // 否则实例化一个新的FlutterEngine实例
        FlutterEngine engine;
        boolean shouldDestroyEngine = engineId.isEmpty()
                || !FlutterEngineCache.getInstance().contains(engineId);
        if (shouldDestroyEngine) {
            if (engineGroup == null) engineGroup =
                    new FlutterEngineGroup(context.getApplicationContext());
            DartExecutor.DartEntrypoint entry = library.isEmpty()
                    ? new DartExecutor.DartEntrypoint(pathToBundle, entrypoint)
                    : new DartExecutor.DartEntrypoint(pathToBundle, library, entrypoint);
            engine = engineGroup.createAndRunEngine(context, entry);
        } else {
            engine = FlutterEngineCache.getInstance().get(engineId);
        }
        if (engine != null) {
            view.attachToFlutterEngine(engine);
            // FlutterEngine实例创建回调
            if (callback != null) callback.onEngineCreate(view, engine);
            // 注册生命周期监听函数
            Lifecycle lifecycle = owner.getLifecycle();
            lifecycle.addObserver(new LifecycleObserver(engine, view, shouldDestroyEngine));
        }
    }

    private void ensureFieldAccessible(Field field) {
        if (!field.isAccessible()) field.setAccessible(true);
    }

    private static final class Holder {
        private static final FlutterKnife INSTANCE = new FlutterKnife();
    }

    public interface EngineCallback {
        void onEngineCreate(FlutterView view, FlutterEngine engine);
    }

    private static final class LifecycleObserver implements DefaultLifecycleObserver {
        private final FlutterEngine engine;
        private final FlutterView view;
        private final boolean shouldDestroyEngine;

        private LifecycleObserver(FlutterEngine engine, FlutterView view, boolean shouldDestroyEngine) {
            this.shouldDestroyEngine = shouldDestroyEngine;
            this.engine = engine;
            this.view = view;
        }

        @Override
        public void onCreate(@NonNull LifecycleOwner owner) {}

        @Override
        public void onStart(@NonNull LifecycleOwner owner) {}

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
            engine.getLifecycleChannel().appIsDetached();
            view.detachFromFlutterEngine();
            if (shouldDestroyEngine) engine.destroy();
        }
    }
}
