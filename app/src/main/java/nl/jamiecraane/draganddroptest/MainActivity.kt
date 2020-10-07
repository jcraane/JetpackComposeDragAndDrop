package nl.jamiecraane.draganddroptest

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.gesture.DragObserver
import androidx.compose.ui.gesture.dragGestureFilter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.onPositioned
import androidx.compose.ui.onSizeChanged
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.platform.ViewAmbient
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import nl.jamiecraane.draganddroptest.ui.DragAndDropTestTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var deleted by mutableStateOf(false)
        val x = mutableStateOf(0f)
        val y = mutableStateOf(0f)

        val colors = listOf(Color.Green, Color.Gray, Color.Blue)

        setContent {
            val height = with(DensityAmbient.current) {
                150.dp.toPx()
            }

            DragAndDropTestTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Column() {
//                        This row contains potential drop targets
                        Row(
                            modifier = Modifier
                                .height(150.dp)
                                .fillMaxWidth()) {
                            colors.mapIndexed { index, color ->
                                Box(
                                    modifier = Modifier.background(color).weight(1f).fillMaxHeight()
                                        .onPositioned {
//                                            todo investigate further for finding coordinates of drop targets.
                                            println("layout coordinates = $it")
                                        }
                                ) {

                                }
                            }
                        }
                        Box(
                            modifier = Modifier.fillMaxWidth().fillMaxHeight()
                                .background(Color.Magenta)
                        ) {
                            if (!deleted) {
                                Box(
                                    modifier = Modifier
                                        .offsetPx(x, y)
                                        .background(Color.Cyan).width(75.dp).height(75.dp)
                                        .dragGestureFilter(object : DragObserver {
                                            override fun onDrag(dragDistance: Offset): Offset {
                                                val newY = y.value + dragDistance.y
                                                x.value = x.value + dragDistance.x
                                                y.value = newY
                                                return dragDistance
                                            }

                                            override fun onStop(velocity: Offset) {
                                                // Todo what is the best method of determining what box the component is dragged on
                                                println("height = $height")
//                                                val hitRect = Rect()
//                                                println("box2 = ${box2View?.getHitRect(hitRect)}")
//                                                println("hitRect = $hitRect")
                                            }
                                        })
                                ) {
                                    Text(text = "Dragme", modifier = Modifier.align(Alignment.Center))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
