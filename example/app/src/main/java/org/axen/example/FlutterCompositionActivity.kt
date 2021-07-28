package org.axen.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.constraintlayout.widget.ConstraintLayout
import io.flutter.embedding.android.FlutterView
import org.axen.flutterknife.FlutterKnife
import org.axen.flutterknife.annotation.BindFlutterView
import org.axen.flutterknife.annotation.CreateFlutterView
import org.axen.flutterknife.annotation.ExecuteDartCode

class FlutterCompositionActivity : AppCompatActivity() {

    /**
     * 绑定FlutterEngine实例并执行Dart代码,
     * 在调用bind方法前, 需要初始化FlutterView，或者使用@CreateFlutterView或@BindFlutterView
     * 默认情况下，FlutterEngine会自动创建FlutterEngine实例，并把实例回调回来
     * 如果提供了engineId，则FlutterKnife会使用缓存的FlutterEngine实例
     */
    @ExecuteDartCode
    private lateinit var fv: FlutterView

    /**
     * 绑定一个FlutterView
     */
    @BindFlutterView(R.id.bfv)
    @ExecuteDartCode(entrypoint = "mine")
    private lateinit var bfv: FlutterView

    /**
     * 创建一个FlutterView
     */
    @CreateFlutterView
    @ExecuteDartCode(entrypoint = "info")
    private lateinit var cfv: FlutterView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flutter_composition)
        fv = findViewById(R.id.fv)// 调用bind前绑定view实例
        FlutterKnife.getInstance().bind(this) {
                _, _ -> Log.e("FlutterKnife", "EngineCallback") }
        val dp200 = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 200F, resources.displayMetrics).toInt()
        val params = ConstraintLayout.LayoutParams(dp200, dp200)
        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        findViewById<ConstraintLayout>(R.id.root).addView(cfv, params)// 将创建的FlutterView实例加入到页面中
    }
}