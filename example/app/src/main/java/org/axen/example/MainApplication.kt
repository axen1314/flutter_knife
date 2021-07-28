package org.axen.example

import android.app.Application
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.FlutterEngineGroup
import io.flutter.embedding.engine.dart.DartExecutor

class MainApplication: Application() {
    companion object {
        const val ENGINE_ID:String = "ENGINE"
        const val ENGINE_ID_1:String = "ENGINE1"
        const val ENGINE_ID_2:String = "ENGINE2"
    }

    override fun onCreate() {
        super.onCreate()
//        val engineGroup = FlutterEngineGroup(this)
//        val engine = engineGroup.createAndRunDefaultEngine(this)
//        val engine1 = engineGroup.createAndRunDefaultEngine(this)
//        val engine2 = engineGroup.createAndRunDefaultEngine(this)
//        val engine = FlutterEngine(this)
//        engine.dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault())
//        val engine1 = FlutterEngine(this)
//        engine1.dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault())
//        val engine2 = FlutterEngine(this)
//        engine2.dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault())
//
//        engine.dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault())
//        FlutterEngineCache.getInstance().put(ENGINE_ID, engine)
//        FlutterEngineCache.getInstance().put(ENGINE_ID_1, engine1)
//        FlutterEngineCache.getInstance().put(ENGINE_ID_2, engine2)
    }
}