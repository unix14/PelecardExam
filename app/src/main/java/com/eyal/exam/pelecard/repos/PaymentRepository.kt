package com.eyal.exam.pelecard.repos

import com.eyal.exam.pelecard.data.dao.PaymentDetailsDao
import com.eyal.exam.pelecard.data.entities.PaymentDetails
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepository @Inject constructor(private val paymentDetailsDao: PaymentDetailsDao) {

    suspend fun insert(paymentDetails: PaymentDetails): Long {
        return paymentDetailsDao.insertPaymentDetails(paymentDetails)
    }

    suspend fun getAllPaymentDetails(): List<PaymentDetails> {
        return paymentDetailsDao.getAllPaymentDetails()
    }

    suspend fun getPaymentById(id: Int): PaymentDetails? {
        return paymentDetailsDao.getPaymentDetailsById(id)
    }

    suspend fun updatePaymentDetails(paymentDetails: PaymentDetails) {
        paymentDetailsDao.updatePaymentDetails(paymentDetails)
    }

    suspend fun deleteAllPaymentDetails() {
        paymentDetailsDao.clearPaymentDetails()
    }
}
