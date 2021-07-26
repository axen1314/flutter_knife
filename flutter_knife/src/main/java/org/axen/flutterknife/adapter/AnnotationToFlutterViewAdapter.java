package org.axen.flutterknife.adapter;

import android.content.Context;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import io.flutter.embedding.android.FlutterView;

public abstract class AnnotationToFlutterViewAdapter<T extends Annotation> implements IAnnotationToFlutterViewAdapter {
    public abstract FlutterView onConvert(Context context, T annotation);
    public FlutterView convert(Context context, Field field) {
        T annotation = field.getAnnotation(getTClass());
        if (annotation != null) return onConvert(context, annotation);
        return null;
    }

    private Class<T> getTClass() {
        return (Class<T>)((ParameterizedType)getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
    }
}
