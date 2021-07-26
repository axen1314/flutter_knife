package org.axen.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.android.RenderMode
import io.flutter.embedding.android.TransparencyMode
import org.axen.flutterknife.annotation.BindFlutterView
import org.axen.flutterknife.annotation.CreateFlutterView
import org.axen.flutterknife.annotation.ExecuteDartCode

class MainActivity : AppCompatActivity() {

    /**
     * 创建一个FlutterView
     */
    @CreateFlutterView
    /**
     * 绑定一个FlutterView
     */
    private lateinit var cfv:FlutterView
    @BindFlutterView(R.id.bv)
    private lateinit var cfv1:FlutterView
    /**
     * 绑定FlutterEngine实例并执行Dart代码
     * 默认情况下，FlutterEngine会自动创建FlutterEngine实例，
     * 如果提供了engineId，则FlutterKnife会使用缓存的FlutterEngine实例
     */
    @ExecuteDartCode
    private lateinit var fv:FlutterView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}