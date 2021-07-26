/*
    Dart执行代理类，执行FlutterEngine实例的executeDartEntrypoint方法，
    启动Dart引擎并执行Dart的入口方法main.dart
 */

package org.axen.flutterknife.flutter;

import androidx.annotation.NonNull;

import io.flutter.FlutterInjector;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.embedding.engine.loader.FlutterLoader;

public class FlutterExecutor {

    private static final String DEFAULT_INITIAL_ROUTE = "/";

    private static final String DEFAULT_DART_ENTRYPOINT = "main";

    /**
     * dart入口方法所在的library名称，默认值为空
     */
    private String library;
    /**
     * dart代码存放文件夹名称
     * 默认值是assets下的${@link FlutterLoader#findAppBundlePath()}文件夹
     */
    private String pathToBundle;
    /**
     * dart入口方法，默认值为main，
     * 可指定入口方法名，但该方法必须在main.dart中有定义
     */
    private String entrypoint = DEFAULT_DART_ENTRYPOINT;
    /**
     * 初始路由名称，默认值为"/"
     */
    private String initialRoute = DEFAULT_INITIAL_ROUTE;

    public FlutterExecutor() {}

    public FlutterExecutor library(String library) {
        this.library = library;
        return this;
    }

    public FlutterExecutor pathToBundle(String pathToBundle) {
        this.pathToBundle = pathToBundle;
        return this;
    }

    public FlutterExecutor entrypoint(String entrypoint) {
        this.entrypoint = entrypoint;
        return this;
    }

    public FlutterExecutor initialRoute(String initialRoute) {
        this.initialRoute = initialRoute;
        return this;
    }

    /**
     * 调用Flutter引擎的executeDartEntrypoint方法执行Dart入口代码，
     * 每个Flutter引擎实例只能执行一次executeDartEntrypoint方法，后续的调用将会无效
     */
    public FlutterExecutor execute(@NonNull FlutterEngine engine) {
        if (this.entrypoint == null || this.entrypoint.isEmpty()) {// 没有指定方法入口，则使用默认方法入口名
            entrypoint = DEFAULT_DART_ENTRYPOINT;
        }
        if (this.pathToBundle == null || this.pathToBundle.isEmpty()) {// 没有指定bundlePath,则获取默认路径
            pathToBundle = FlutterInjector.instance().flutterLoader().findAppBundlePath();
        }
        // 执行Dart入口代码
        DartExecutor executor = engine.getDartExecutor();
        DartExecutor.DartEntrypoint entrypoint;
        if (library == null || library.isEmpty()) {
            entrypoint = new DartExecutor.DartEntrypoint(this.pathToBundle, this.entrypoint);
        } else {
            entrypoint = new DartExecutor.DartEntrypoint(this.pathToBundle, library, this.entrypoint);
        }
        if (!executor.isExecutingDart()) {
            engine.getNavigationChannel().setInitialRoute(
                    this.initialRoute == null || this.initialRoute.isEmpty() ? DEFAULT_INITIAL_ROUTE : this.initialRoute);
            executor.executeDartEntrypoint(entrypoint);
        }
        return this;
    }
}
