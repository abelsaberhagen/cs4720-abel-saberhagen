package edu.virginia.cs4720.helloworld_eug8xn

import android.R.attr.onClick
import android.os.Bundle
import android.service.autofill.OnClickAction
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.virginia.cs4720.helloworld_eug8xn.ui.theme.Helloworldeug8xnTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Helloworldeug8xnTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Abel",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    val state = rememberTextFieldState();
    val name = state.text
    val greeting = remember { mutableStateOf("Hello") }

    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "${greeting.value}, ${name}",
        )
        TextField(
            state = state,
            modifier = modifier,
            label = { Text("Your name here!") }
        )
        Row (
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    greeting.value = "Hello"
                }
            ) {
                Text("Hello")
            }
            Button(
                onClick = {
                    greeting.value = "Hi there"
                }
            ) {
                Text("Hi there")
            }
            Button(
                onClick = {
                    greeting.value = "Bonjour"
                }
            ) {
                Text("Bonjour")
            }

        }
     }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Helloworldeug8xnTheme {
        Greeting("Android")
    }
}