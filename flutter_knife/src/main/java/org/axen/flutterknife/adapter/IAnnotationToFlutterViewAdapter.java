package org.axen.flutterknife.adapter;

import android.content.Context;

import java.lang.reflect.Field;

import io.flutter.embedding.android.FlutterView;

public interface IAnnotationToFlutterViewAdapter {
    FlutterView convert(Context context, Field field);
}
