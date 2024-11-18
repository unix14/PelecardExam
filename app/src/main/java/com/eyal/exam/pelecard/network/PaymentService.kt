package com.eyal.exam.pelecard.network

import com.eyal.exam.pelecard.abs.Constants
import com.eyal.exam.pelecard.data.entities.PaymentDetails
import retrofit2.http.Body
import retrofit2.http.POST
interface PaymentService {
    @POST(Constants.DEFAULT_PAYMENT_API_POST_ENDPOINT)
    suspend fun postPaymentDetails(@Body paymentDetails: PaymentDetails): PaymentServiceResponse
}