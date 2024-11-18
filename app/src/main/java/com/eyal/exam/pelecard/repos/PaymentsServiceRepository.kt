package com.eyal.exam.pelecard.repos

import com.eyal.exam.pelecard.data.entities.PaymentDetails
import com.eyal.exam.pelecard.network.PaymentService
import com.eyal.exam.pelecard.network.PaymentServiceResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentsServiceRepository @Inject constructor(
    private val paymentService: PaymentService
) {

    // This repo acts as if we finished the payment and we sent it to a remote server
    suspend fun postPaymentDetails(paymentDetails: PaymentDetails): PaymentServiceResponse {
        // We use a fake API to get a 200 result for this POST endpoint
        return paymentService.postPaymentDetails(paymentDetails)
    }
}