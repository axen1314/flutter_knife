package org.axen.flutterknife.plugin;

import android.app.Activity;
import android.content.Context;

import org.axen.flutterknife.annotation.BindFlutterView;

import java.lang.reflect.Field;

import io.flutter.embedding.android.FlutterView;


public class BindFlutterViewAnnotationPlugin extends AbstractViewAnnotationPlugin<BindFlutterView> {

    @Override
    public FlutterView onConvert(Context context, Field field, BindFlutterView annotation) {
        return ((Activity)context).findViewById(annotation.value());
    }
}
