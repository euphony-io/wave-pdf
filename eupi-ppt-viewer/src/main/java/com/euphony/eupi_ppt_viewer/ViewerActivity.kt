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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import co.euphony.rx.EuRxManager
import co.euphony.util.EuOption
import java.io.File


class ViewerActivity : ComponentActivity() {

    private val TAG = "VIEWER"
    private val rxManager = EuRxManager(EuOption.ModeType.EUPI)
    private var currentPage = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var str: String = intent.getStringExtra("Uri")!!
        Log.i("테스트", "받은 값 : $str")
        /*TODO: Load PDF */
        val pdfRenderer: PdfRenderer = loadRenderer(str)
        initRxManager(pdfRenderer.pageCount)
        /*TODO: PDF to Bitmap 이미지 변환 후 List화 */
        val imageList: List<ImageBitmap> = pdfToImageBitmaps(pdfRenderer)

        setContent {
            Surface(
                color = Color.White
            ) {
                PageView(imageList.get(currentPage))
            }

        }

    }

    private fun initRxManager(lastPage: Int) {
        rxManager.setOnWaveKeyDown(EuPICodeEnum.PREV_PAGE.code.toInt()) {
            if (currentPage <= 0) {
                Toast.makeText(this, "This is first page", Toast.LENGTH_LONG)
                Log.d(TAG, "This is first page.")
            } else {
                currentPage -= 1
            }
        }
        rxManager.setOnWaveKeyDown(EuPICodeEnum.NEXT_PAGE.code.toInt()) {
            if (currentPage >= lastPage - 1) {
                Toast.makeText(this, "This is last page", Toast.LENGTH_LONG)
                Log.d(TAG, "This is last page.")
            } else {
                currentPage += 1
            }
        }
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
    Log.i("pdfToImageBitmaps", "Enter getImageBitmaps")
    var mutablePageList: MutableList<ImageBitmap> = mutableListOf<ImageBitmap>()
    for (i: Int in 0..pdfRenderer.pageCount - 1) {
        Log.i("pdfToImageBitmaps", i.toString())
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