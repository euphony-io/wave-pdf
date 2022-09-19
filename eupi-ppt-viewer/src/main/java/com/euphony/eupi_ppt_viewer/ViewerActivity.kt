package com.euphony.eupi_ppt_viewer

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
import co.euphony.rx.EuRxManager
import co.euphony.util.EuOption
import com.euphony.common_lib.EuPICodeEnum
import java.io.File


class ViewerActivity : ComponentActivity() {

    private val TAG = "VIEWER"
    private val rxManager = EuRxManager(EuOption.ModeType.EUPI)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var str: String = intent.getStringExtra("Uri")!!
        Log.i("테스트", "받은 값 : $str")
        /*TODO: Load PDF */
        val pdfRenderer: PdfRenderer = loadRenderer(str)

        /*TODO: PDF to Bitmap 이미지 변환 후 List화 */
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

        rxManager.setOnWaveKeyDown(EuPICodeEnum.PREV_PAGE.code.toInt()) {
            if (currentPage.value <= 0) {
                Toast.makeText(context, "This is first page", Toast.LENGTH_LONG)
                Log.d(TAG, "This is first page.")
            } else {
                currentPage.value -= 1
                Log.d(TAG, "currentPage: $currentPage")
            }
        }
        rxManager.setOnWaveKeyDown(EuPICodeEnum.NEXT_PAGE.code.toInt()) {
            if (currentPage.value >= lastPage - 1) {
                Toast.makeText(context, "This is last page", Toast.LENGTH_LONG)
                Log.d(TAG, "This is last page.")
            } else {
                currentPage.value += 1
                Log.d(TAG, "currentPage: $currentPage")
            }
        }

        if(rxManager.listen()){
            Log.d(TAG, "rxManager: listen success")
        }else{
            Log.d(TAG, "rxManager: listen fail")
        }
        PageView(images[currentPage.value.toInt()])
    }
}

private fun loadRenderer(pdfUri: String): PdfRenderer {
    var pdfFile: File = File(pdfUri)
    var mPdfRenderer: PdfRenderer
    if (!pdfFile.exists()) {
        Log.i("readPdf", "File is not Exist, Your File Path : ${pdfUri}")
    }
    var mFileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)
    if (mFileDescriptor == null) {
        Log.i("readPdf", "Can't load mFileDescriptor")
    }
    mPdfRenderer = PdfRenderer(mFileDescriptor)
    Log.i("readPdf", "Pdf Page Count : " + mPdfRenderer.pageCount)
    return mPdfRenderer
}

private fun pdfToImageBitmaps(pdfRenderer: PdfRenderer): List<ImageBitmap> {
    var mutablePageList: MutableList<ImageBitmap> = mutableListOf<ImageBitmap>()
    for (i: Int in 0..pdfRenderer.pageCount - 1) {
        var curPage: PdfRenderer.Page = pdfRenderer.openPage(i)
        mutablePageList.add(pageToImageBitmap(curPage))
        curPage.close()
    }

    return mutablePageList.toList()
}

private fun pageToImageBitmap(page: PdfRenderer.Page): ImageBitmap {
    var pageWidth = page.width
    var pageHeight = page.height
    var bitmap: Bitmap = Bitmap.createBitmap(pageWidth, pageHeight, Bitmap.Config.ARGB_8888)
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
