package me.gumify.icecreamexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import me.gumify.icecream.Icecream
import me.gumify.icecreamexample.ui.theme.IcecreamExampleTheme
import sh.fearless.util.debug

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IcecreamExampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    App()
                }
            }
        }
    }
}


val icecream = Icecream()

@Composable
fun App() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Box(contentAlignment = Alignment.Center) {
        Button(onClick = {
            icecream.searchWallpapers("tamil", 1) { wallpapers, error ->
                wallpapers[0].directUrl { url, e ->
                    debug(url)
                }
            }
        }) {
            Text(text = "Go!")
        }
    }
}
