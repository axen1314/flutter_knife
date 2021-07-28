package org.axen.example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.android.RenderMode
import io.flutter.embedding.android.TransparencyMode
import io.flutter.embedding.engine.FlutterEngine
import org.axen.flutterknife.FlutterKnife
import org.axen.flutterknife.annotation.BindFlutterView
import org.axen.flutterknife.annotation.CreateFlutterView
import org.axen.flutterknife.annotation.ExecuteDartCode

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btn).setOnClickListener {
            startActivity(Intent(this@MainActivity, FlutterCompositionActivity::class.java))
        }
    }
}