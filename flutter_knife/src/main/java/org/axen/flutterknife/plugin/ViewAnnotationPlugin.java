package org.axen.flutterknife.plugin;

import android.content.Context;

import androidx.annotation.Nullable;

import java.lang.reflect.Field;

import io.flutter.embedding.android.FlutterView;

public interface ViewAnnotationPlugin {
    @Nullable FlutterView convert(Context context, Field field);
}
