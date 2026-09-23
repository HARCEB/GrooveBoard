package com.example.grooveboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.grooveboard.ui.navigation.AppNavGraph
import com.example.grooveboard.ui.theme.GrooveBackground
import com.example.grooveboard.ui.theme.GrooveBoardTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GrooveBoardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = GrooveBackground
                ) {
                    AppNavGraph()
                }
            }
        }
    }
}
