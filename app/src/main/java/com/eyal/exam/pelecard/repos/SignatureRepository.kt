package com.eyal.exam.pelecard.repos

import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Density
import com.eyal.exam.pelecard.helpers.SignatureHelper
import com.eyal.exam.pelecard.helpers.SignatureHelper.Companion.saveBitmapToFile
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton

class SignatureRepository @Inject constructor(@ApplicationContext private val context: Context) {

    //todo add logs
    suspend fun saveSignature(savedOffsets: List<Offset>, savedDensity: Density, paymentId: Int): File {
        val bitmap = SignatureHelper.captureSignatureAsBitmap(savedOffsets, savedDensity)
        return saveBitmapToFile(context, bitmap, "signature_$paymentId")
    }
}