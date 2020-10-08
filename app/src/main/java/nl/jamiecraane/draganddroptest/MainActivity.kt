package nl.jamiecraane.draganddroptest

import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.gesture.DragObserver
import androidx.compose.ui.gesture.dragGestureFilter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.globalBounds
import androidx.compose.ui.onPositioned
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import nl.jamiecraane.draganddroptest.ui.DragAndDropTestTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var deleted by mutableStateOf(false)
        val draggedX = mutableStateOf(0f)
        val draggedY = mutableStateOf(0f)

        val colors = listOf(Color.Green, Color.Gray, Color.Blue)
        val dropTargetBounds = mutableStateMapOf<Int, Rect>()
        var dragObjectBounds = Rect(0f, 0f, 0f, 0f)

        setContent {
            val context = ContextAmbient.current

            DragAndDropTestTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Column() {
//                        This row contains potential drop targets
                        Row(
                            modifier = Modifier
                                .height(150.dp)
                                .fillMaxWidth()
                        ) {
                            colors.mapIndexed { index, color ->
                                Box(

                                    modifier = Modifier.background(color).weight(1f).fillMaxHeight()
                                        .onPositioned { layoutCoordinates ->
                                            dropTargetBounds[index] = layoutCoordinates.globalBounds
                                        }
                                ) {
                                    if (index == 1) {
                                        Text(text = "Drop to delete", modifier = Modifier.align(Alignment.Center))
                                    }
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
                                        .offsetPx(draggedX, draggedY)
                                        .background(Color.Cyan).width(75.dp).height(75.dp)
                                        .onPositioned { layoutCoordinates ->
                                            dragObjectBounds = layoutCoordinates.globalBounds
                                        }
                                        .dragGestureFilter(object : DragObserver {
                                            override fun onDrag(dragDistance: Offset): Offset {
                                                val newY = draggedY.value + dragDistance.y
                                                draggedX.value = draggedX.value + dragDistance.x
                                                draggedY.value = newY
                                                return dragDistance
                                            }

                                            override fun onStop(velocity: Offset) {
                                                dropTargetBounds.entries.forEach { entry ->
                                                    fun dropped(index: Int) {
                                                        when (index) {
                                                            0 -> Toast.makeText(context, "Dropped on green target", Toast.LENGTH_SHORT)
                                                                .show()
                                                            1 -> {
                                                                Toast.makeText(
                                                                    context,
                                                                    "Dropped on gray target, delete object",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                                deleted = true
                                                            }
                                                            2 -> Toast.makeText(context, "Dropped on blue target", Toast.LENGTH_SHORT)
                                                                .show()
                                                            else -> {
                                                            }
                                                        }
                                                    }

                                                    if (dragObjectBounds.overlaps(entry.value)) {
                                                        dropped(entry.key)
                                                    }
                                                }
                                            }
                                        })
                                ) {
                                    Text(
                                        text = "Dragme",
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
