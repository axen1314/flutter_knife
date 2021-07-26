package org.axen.flutterknife.adapter;

import android.content.Context;

import org.axen.flutterknife.annotation.CreateFlutterView;

import io.flutter.embedding.android.FlutterImageView;
import io.flutter.embedding.android.FlutterSurfaceView;
import io.flutter.embedding.android.FlutterTextureView;
import io.flutter.embedding.android.FlutterView;
import io.flutter.embedding.android.TransparencyMode;

public class CreateFlutterViewAdapter extends AnnotationToFlutterViewAdapter<CreateFlutterView> {
    @Override
    public FlutterView onConvert(Context context, CreateFlutterView annotation) {
        FlutterView view = null;
        switch (annotation.renderMode()) {
            case surface:
                FlutterSurfaceView surfaceView = new FlutterSurfaceView(
                        context, annotation.transparencyMode() == TransparencyMode.transparent);
                view = new FlutterView(context, surfaceView);
                break;
            case texture:
                FlutterTextureView textureView = new FlutterTextureView(context);
                view = new FlutterView(context, textureView);
                break;
            case image:
                FlutterImageView imageView = new FlutterImageView(context);
                view = new FlutterView(context, imageView);
                break;
        }
        return view;
    }
}
