package org.axen.example

import android.app.Application
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache

class MainApplication: Application() {
    companion object {
        const val ENGINE_ID:String = "ENGINE"
    }

    override fun onCreate() {
        super.onCreate()
        val engine = FlutterEngine(this)
        FlutterEngineCache.getInstance().put(ENGINE_ID, engine)
    }
}