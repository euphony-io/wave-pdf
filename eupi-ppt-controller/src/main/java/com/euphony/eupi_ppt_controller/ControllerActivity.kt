package com.euphony.eupi_ppt_controller

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.euphony.common.Constants
import co.euphony.tx.EuTxManager
import co.euphony.util.EuOption
import com.euphony.eupi_ppt_controller.ui.theme.WavepptTheme

class ControllerActivity : ComponentActivity() {

    private val txManager = EuTxManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        txManager.setMode(EuOption.ModeType.EUPI)

        setContent {
            InitControllerView(txManager = txManager)
        }
    }
}

@Composable
fun InitControllerView(txManager: EuTxManager) {
    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    if( txManager.callEuPI(
                            EuPICodeEnum.PREV_PAGE.code,
                            EuTxManager.EuPIDuration.LENGTH_LONG
                        )== Constants.Result.OK) {
                        Log.i("Controller", "PREV button")
                    }
                },
                shape = RoundedCornerShape(20),
                colors = ButtonDefaults.outlinedButtonColors(Color.LightGray),
                modifier = Modifier.fillMaxHeight(0.1F)
            ) {
                Text("prev page", color = Color.Black)
            }
            Button(
                onClick = {
                    if( txManager.callEuPI(
                            EuPICodeEnum.NEXT_PAGE.code,
                            EuTxManager.EuPIDuration.LENGTH_LONG
                        )== Constants.Result.OK) {
                        Log.i("Controller", "NEXT button")
                    }
                },
                shape = RoundedCornerShape(20),
                colors = ButtonDefaults.outlinedButtonColors(Color.LightGray),
                modifier = Modifier.fillMaxHeight(0.1F)
            ) {
                Text("next page", color = Color.Black)
            }
        }
    }
}