package com.euphony.eupi_pdf_viewer

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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

    private val PERMISSIONS: Array<String> = arrayOf(
        android.Manifest.permission.RECORD_AUDIO,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions()

        if(checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED)
            Log.i("Viewer", "RECORD_AUDIO DENIED")
        if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            Log.i("Viewer", "READ_EXTERNAL_STORAGE DENIED")

        setContent {
            InitMainView()
        }
    }

    private fun requestPermissions() {
        val multiplePermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grantResult ->
            for(result in grantResult.values) {
                if(!result) {
                    finish()
                }
            }
        }

        multiplePermissionLauncher.launch(PERMISSIONS)
    }

}

fun goToViewer(context: Context, uri: String) {
    val intent = Intent(context, ViewerActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
    intent.putExtra("Uri", uri)
    context.startActivity(intent)
}

@Composable
fun InitMainView() {

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
                )
            }
        }
    }
}


@Composable
fun DialogButton(button: String) {
    val openDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    var isPDFLoaded = remember { mutableStateOf(false) }
    var pdfPath = remember { mutableStateOf("") }
    var pdfLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            pdfPath.value = uri.path.toString().split(":").get(1)
        } else {
            Log.i("pdfLauncher", "uri is null")
        }
        isPDFLoaded.value = !pdfPath.value.equals("")
        openDialog.value = true
    }

    TextButton(
        onClick = { pdfLauncher.launch("application/pdf") },
        shape = RoundedCornerShape(20),
        colors = ButtonDefaults.outlinedButtonColors(Color.LightGray),
        modifier = Modifier
            .fillMaxHeight(0.4F)
            .fillMaxWidth(0.5F)
    ) {
        Text("$button", color = Color.Black)
    }

    if (openDialog.value) {
        if (isPDFLoaded.value) {
            AlertDialog(
                onDismissRequest = { openDialog.value != openDialog.value },
                title = { Text(text = "PDF Load Success!") },
                text = { Text(text = "Do you want to go to Viewer?") },
                confirmButton = {

                    TextButton(onClick = {
                        goToViewer(context, pdfPath.value)
                    }) {
                        Text(text = "Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        openDialog.value = false
                        pdfPath.value = ""
                    }) {
                        Text(text = "Cancel")
                    }
                })
        } else {
            AlertDialog(
                onDismissRequest = { openDialog.value != openDialog.value },
                title = { Text(text = "PDF Load Failed") },
                text = { Text(text = "Please Check and Retry") },
                confirmButton = {
                    TextButton(onClick = { openDialog.value = false }) {
                        Text(text = "OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { openDialog.value = false }) {
                        Text(text = "Cancel")
                    }
                })
        }
    }
}
