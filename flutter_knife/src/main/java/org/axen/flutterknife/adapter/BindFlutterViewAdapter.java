package org.axen.flutterknife.adapter;

import android.app.Activity;
import android.content.Context;

import org.axen.flutterknife.annotation.BindFlutterView;

import io.flutter.embedding.android.FlutterView;


public class BindFlutterViewAdapter extends AnnotationToFlutterViewAdapter<BindFlutterView> {

    @Override
    public FlutterView onConvert(Context context, BindFlutterView annotation) {
        return ((Activity)context).findViewById(annotation.value());
    }
}
