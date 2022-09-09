package com.euphony.eupi_ppt_viewer

import android.media.Image
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import co.euphony.rx.EuRxManager
import co.euphony.util.EuOption
import com.euphony.eupi_ppt_viewer.ui.theme.WavepptTheme


class ViewerActivity : ComponentActivity() {

    private val TAG = "VIEWER"
    private val rxManager = EuRxManager(EuOption.ModeType.EUPI)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        /*TODO: Load PDF */
        /*TODO: PDF to Bitmap 이미지 변환 후 List화 */

        setContent {
            Surface(
                color = Color.White
            ){
                Text(text="ViewerActivity page")
            }

        }

    }
}


@Composable
fun PageView(page: ImageBitmap) {
    Image(
        bitmap = page,
        contentDescription = null,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    )
}