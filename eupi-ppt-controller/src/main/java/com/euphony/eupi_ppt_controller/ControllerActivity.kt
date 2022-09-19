package com.euphony.eupi_ppt_controller

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import co.euphony.common.Constants
import co.euphony.tx.EuTxManager
import co.euphony.util.EuOption
import com.euphony.common_lib.EuPICodeEnum

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
                        Log.i("Controller", "PREV button is clicked")
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
                        Log.i("Controller", "NEXT button is clicked")
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