package com.eyal.exam.pelecard.data.dao

import androidx.room.*
import com.eyal.exam.pelecard.data.entities.PaymentDetails

@Dao
interface PaymentDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaymentDetails(paymentDetails: PaymentDetails): Long

    @Query("SELECT * FROM payment_details")
    suspend fun getAllPaymentDetails(): List<PaymentDetails>

    // Retrieve PaymentDetails by ID
    @Query("SELECT * FROM payment_details WHERE id = :id")
    suspend fun getPaymentDetailsById(id: Int): PaymentDetails?

    // Update an existing PaymentDetails record
    @Update
    suspend fun updatePaymentDetails(paymentDetails: PaymentDetails)


    // Delete all PaymentDetails from the table
    @Query("DELETE FROM payment_details")
    suspend fun clearPaymentDetails()
}