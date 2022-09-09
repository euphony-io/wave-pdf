package com.euphony.eupi_ppt_viewer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SettingsInputAntenna
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InitMainView()
        }
    }
}

fun goToViewer(context: Context) {
    val intent = Intent(context, ViewerActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
    context.startActivity(intent)
}

@Composable
fun InitMainView() {
    val context = LocalContext.current

    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "wave-ppt",
                style = MaterialTheme.typography.h2,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,

                )
            Spacer(modifier = Modifier.padding(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7F),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Outlined.SettingsInputAntenna,
                    "",
                    modifier = Modifier.fillMaxSize(0.6F)
                )
                DialogButton(
                    "Load PDF",
                    "PDF loading success",
                    "Do you want to move to Viewer?",
                    )
            }
        }
    }
}

@Composable
fun DialogButton(button: String, title: String, body: String) {
    val openDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    TextButton(
        onClick = { openDialog.value = !openDialog.value },
        shape = RoundedCornerShape(20),
        colors = ButtonDefaults.outlinedButtonColors(Color.LightGray),
        modifier = Modifier
            .fillMaxHeight(0.4F)
            .fillMaxWidth(0.5F)
    ) {
        Text("$button", color = Color.Black)
    }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value != openDialog.value },
            title = { Text(text = "$title") },
            text = { Text(text = "$body") },
            confirmButton = {
                TextButton(onClick = { goToViewer(context) }) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { openDialog.value = false }) {
                    Text(text = "cancel")
                }
            })
    }
}


