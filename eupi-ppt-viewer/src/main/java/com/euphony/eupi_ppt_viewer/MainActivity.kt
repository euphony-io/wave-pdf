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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class MainActivity : ComponentActivity() {
    private val TAG = "pptViewer"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        System.setProperty(
            "org.apache.poi.javax.xml.stream.XMLInputFactory",
            "com.fasterxml.aalto.stax.InputFactoryImpl"
        )
        System.setProperty(
            "org.apache.poi.javax.xml.stream.XMLOutputFactory",
            "com.fasterxml.aalto.stax.OutputFactoryImpl"
        )
        System.setProperty(
            "org.apache.poi.javax.xml.stream.XMLEventFactory",
            "com.fasterxml.aalto.stax.EventFactoryImpl"
        )

        setContent {
            InitMainView(context = this)

        }
    }
}


fun goPPT(context: Context) {
    val intent = Intent(context, PPTActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
    context.startActivity(intent)
}


@Composable
fun InitMainView(context: Context) {
    Surface(color = MaterialTheme.colors.background) {
        Column(
            modifier = Modifier
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(bottom = 20.dp),
                text = "wave-ppt",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Outlined.SettingsInputAntenna,
                    "",
                    modifier = Modifier.size(60.dp)
                )
                TextButton(
                    onClick = { goPPT(context = context) },
                    shape = RoundedCornerShape(20),
                    colors = ButtonDefaults.outlinedButtonColors(Color.White),
                    modifier = Modifier.size(90.dp, 30.dp)
                ) {
                   Text ("Load PPT", color = Color.Black, fontSize = 10.sp)
                }
            }
        }
    }

}

//@Preview
@Composable
fun defaultPreveiw() {
    val context = LocalContext.current
    InitMainView(context)
}



