package org.axen.flutterknife.plugin;

import android.content.Context;

import org.axen.flutterknife.annotation.CreateFlutterView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import io.flutter.embedding.android.FlutterImageView;
import io.flutter.embedding.android.FlutterSurfaceView;
import io.flutter.embedding.android.FlutterTextureView;
import io.flutter.embedding.android.FlutterView;
import io.flutter.embedding.android.TransparencyMode;

public class CreateFlutterViewAnnotationPlugin extends AbstractViewAnnotationPlugin<CreateFlutterView> {
    @Override
    public FlutterView onConvert(Context context, Field field, CreateFlutterView annotation) {
        FlutterView view = null;
        try {
            Class<?> fieldClass = field.getType();
            Constructor<?> constructor;
            switch (annotation.renderMode()) {
                case surface:
                    FlutterSurfaceView surfaceView = new FlutterSurfaceView(
                            context, annotation.transparencyMode() == TransparencyMode.transparent);
                    constructor = fieldClass.getConstructor(Context.class, FlutterSurfaceView.class);
                    view = (FlutterView) constructor.newInstance(context, surfaceView);
                    break;
                case texture:
                    FlutterTextureView textureView = new FlutterTextureView(context);
                    constructor = fieldClass.getConstructor(Context.class, FlutterTextureView.class);
                    view = (FlutterView) constructor.newInstance(context, textureView);
                    break;
                case image:
                    FlutterImageView imageView = new FlutterImageView(context);
                    constructor = fieldClass.getConstructor(Context.class, FlutterImageView.class);
                    view = (FlutterView) constructor.newInstance(context, imageView);
                    break;
            }
        } catch (Exception ignored) {}
        return view;
    }
}
