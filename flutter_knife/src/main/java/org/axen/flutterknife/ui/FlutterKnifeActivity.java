package org.axen.flutterknife.ui;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

public class FlutterKnifeActivity extends Activity implements LifecycleOwner {
    private final LifecycleRegistry lifecycle = new LifecycleRegistry(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycle;
    }
}
