package org.axen.flutterknife.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import org.axen.flutterknife.android.R;

import java.lang.reflect.Field;

import io.flutter.embedding.android.FlutterImageView;
import io.flutter.embedding.android.FlutterSurfaceView;
import io.flutter.embedding.android.FlutterTextureView;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

public class FlutterView extends io.flutter.embedding.android.FlutterView {
    private static final String SURFACE_FILED_NAME = "flutterSurfaceView";
    private static final String TEXTURE_FILED_NAME = "flutterSurfaceView";
    private static final String IMAGE_FILED_NAME = "flutterSurfaceView";

    private static final String METHOD_CHANNEL_NAME = "org.axen.flutterknife";
    private static final String METHOD_SET_ROUTE = "setRoute";

    private MethodChannel mMethodChannel;

    public FlutterView(Context context) {
        super(context);
    }

    public FlutterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FlutterView(Context context, FlutterSurfaceView surfaceView) {
        super(context, surfaceView);
    }

    public FlutterView(Context context, FlutterTextureView textureView) {
        super(context, textureView);
    }

    public FlutterView(Context context, FlutterImageView imageView) {
        super(context, imageView);
    }

    private void init(Context context, AttributeSet attrs) {
        Class<?> clazz = getClass();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FlutterView);
        int renderMode = array.getInt(R.styleable.FlutterView_renderMode, 0);
        try {
            Field field = clazz.getField(SURFACE_FILED_NAME);
            field.setAccessible(true);
            field.set(this, null);
            switch (renderMode) {
                case 0:
                    int transparencyMode = array.getInt(R.styleable.FlutterView_transparencyMode, 1);
                    field.set(this, new FlutterSurfaceView(context, transparencyMode == 1));
                    break;
                case 1:
                    field = clazz.getField(TEXTURE_FILED_NAME);
                    field.setAccessible(true);
                    field.set(this, new FlutterTextureView(context));
                    break;
                case 2:
                    field = clazz.getField(IMAGE_FILED_NAME);
                    field.setAccessible(true);
                    field.set(this, new FlutterImageView(context));
                    break;
                default:
                    throw new IllegalArgumentException(
                            String.format("RenderMode not supported with this constructor: %s", renderMode));
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            array.recycle();
        }
    }

    @Override
    public void attachToFlutterEngine(@NonNull FlutterEngine engine) {
        super.attachToFlutterEngine(engine);
        mMethodChannel = new MethodChannel(engine.getDartExecutor().getBinaryMessenger(), METHOD_CHANNEL_NAME);
    }

    @Override
    public void detachFromFlutterEngine() {
        super.detachFromFlutterEngine();
        mMethodChannel = null;
    }

    public void setRoute(String route) {
        mMethodChannel.invokeMethod(METHOD_SET_ROUTE, route);
    }
}
