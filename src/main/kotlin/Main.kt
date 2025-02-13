import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.delay

@Composable
@Preview
fun App(window: WindowState) {
    val allDevices = remember { Commander.getNetworkDevices() }
    var selectedItem by remember { mutableStateOf(allDevices.first()) }
    var notificationVisible by remember { mutableStateOf(false) }

    var message by remember { mutableStateOf(emptyString) }
    var background by remember { mutableStateOf(Color.Red) }

    LaunchedEffect(selectedItem) {
        Commander.changeNetworkDevicePriority(selectedItem) { success ->
            message = if (success) "Success" else "Failure"
            background = if (success) Color.Green else Color.Red
            notificationVisible = true

            delay(1_000L)
            notificationVisible = false
        }
    }

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF121212))
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(text = "Current network: $selectedItem", color = Color(0xFFE0E0E0))

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    itemsIndexed(allDevices) { index, item ->
                        val bgColor = if (item == selectedItem) Color(0xFF3949AB) else Color(0xFF1E1E1E)

                        Text(
                            text = "${index + 1}) $item",
                            color = Color(0xFFE0E0E0),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .background(color = bgColor, shape = RoundedCornerShape(8.dp))
                                .clickable {
                                    if (selectedItem == item) return@clickable
                                    selectedItem = item
                                }
                                .padding(12.dp)
                        )
                    }
                }
            }

            AnimatedVisibility(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                visible = notificationVisible
            ) {
                Box(modifier = Modifier.background(background, shape = RoundedCornerShape(8.dp))) {
                    Text(modifier = Modifier.fillMaxWidth(), text = message, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

fun main() = application {
    val windowState = rememberWindowState(width = 282.dp, height = 450.dp)
    Window(
        resizable = false,
        onCloseRequest = ::exitApplication,
        state = windowState
    ) {
        App(windowState)
    }
}

val emptyString = ""