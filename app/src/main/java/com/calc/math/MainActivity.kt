package com.calc.math

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Calculus", "Discrete", "Algebra", "Probability", "Settings")

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, title ->
                    NavigationBarItem(
                        icon = { Text(title.take(1)) },
                        label = { Text(title) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            when (selectedTab) {
                0 -> CalcView("calculus", "Expression (e.g. x**2 + sin(x))", listOf("derive", "integrate"))
                1 -> CalcView("discrete", "Logic (e.g. A & (B | ~C))", listOf("table"))
                2 -> CalcView("algebra", "Matrix (e.g. 1,2;3,4)", listOf("det", "inv"))
                3 -> CalcView("probability", "n,k (e.g. 5,2)", listOf("comb", "perm"))
                4 -> SettingsView()
            }
        }
    }
}

@Composable
fun CalcView(subject: String, hint: String, ops: List<String>) {
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    val py = Python.getInstance().getModule("math_core")

    Column {
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text(hint) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ops.forEach { op ->
                Button(onClick = {
                    result = py.callAttr("calculate", subject, input, op).toString()
                }) {
                    Text(op.uppercase())
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Result:\n$result", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun SettingsView() {
    Column {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Version: 1.0.0-Canary")
        Text("Engine: Python (Chaquopy) + SymPy")
        Text("UI: Jetpack Compose MD3")
    }
}
