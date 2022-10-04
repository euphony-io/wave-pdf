package com.euphony.eupi_pdf_viewer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import co.euphony.rx.EuRxManager
import co.euphony.util.EuOption
import com.euphony.common_lib.EuPICodeEnum


class ViewerActivity : ComponentActivity() {

    private val TAG = "WAVE_VIEWER"
    private val rxManager = EuRxManager(EuOption.ModeType.EUPI)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var pdfUri: String = intent.getStringExtra("Uri")!!
        Log.i(TAG, "Received Uri : $pdfUri")

        val pdfRenderer: PdfRenderer = loadRenderer(applicationContext, pdfUri)
        val imageList: List<ImageBitmap> = pdfToImageBitmaps(pdfRenderer)

        setContent {
            Surface(
                color = Color.White
            ) {
                UpdateView(imageList, rxManager)
            }
        }
    }


    @Composable
    fun UpdateView(images: List<ImageBitmap>, rxManager: EuRxManager){
        var currentPage = remember{ mutableStateOf(0)}
        val lastPage = images.lastIndex
        val context = LocalContext.current

        rxManager.setOnWaveKeyUp(EuPICodeEnum.PREV_PAGE.code.toInt()) {

            if (currentPage.value == 0) {
                Toast.makeText(context, "This is first page", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "This is first page.")
            } else{ currentPage.value -= 1
                Log.d(TAG, "currentPage: $currentPage")
                rxManager.finish()}

            rxManager.listen()
        }
        rxManager.setOnWaveKeyUp(EuPICodeEnum.NEXT_PAGE.code.toInt()) {

            if (currentPage.value == lastPage - 1) {
                Toast.makeText(context, "This is last page", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "This is last page.")
            } else {
                currentPage.value += 1
                Log.d(TAG, "currentPage: $currentPage")
                rxManager.finish()}
            rxManager.listen()
        }

        if(rxManager.listen()){
            Log.d(TAG, "rxManager: listen success")
        }else{
            Log.d(TAG, "rxManager: listen fail")
            rxManager.listen()
        }
        PageView(images[currentPage.value])
    }
}

private fun loadRenderer(context : Context, pdfUri: String): PdfRenderer {
    val mFileDescriptor : ParcelFileDescriptor? = context.contentResolver.openFileDescriptor(pdfUri.toUri(), "r")
    if (mFileDescriptor == null) {
        Log.i("loadRenderer", "Can't load mFileDescriptor")
    }
    val mPdfRenderer = PdfRenderer(mFileDescriptor!!)
    Log.i("loadRenderer", "Pdf Page Count : " + mPdfRenderer.pageCount)
    return mPdfRenderer
}

private fun pdfToImageBitmaps(pdfRenderer: PdfRenderer): List<ImageBitmap> {
    var pageImageList : MutableList<ImageBitmap> = mutableListOf()
    for (i : Int in 0..pdfRenderer.pageCount - 1) {
        var curPage : PdfRenderer.Page = pdfRenderer.openPage(i)
        pageImageList.add(pageToImageBitmap(curPage))
        curPage.close()
    }
    return pageImageList
}

private fun pageToImageBitmap(page: PdfRenderer.Page): ImageBitmap {
    var bitmap: Bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
    return bitmap.asImageBitmap()
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
