package com.eyal.exam.pelecard.di

import android.content.Context
import androidx.room.Room
import com.eyal.exam.pelecard.data.dao.PaymentDetailsDao
import com.eyal.exam.pelecard.data.database.PaymentDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePaymentDatabase(@ApplicationContext context: Context): PaymentDatabase {
        return PaymentDatabase.getDatabase(context)
    }

    @Provides
    fun providePaymentDetailsDao(paymentDatabase: PaymentDatabase): PaymentDetailsDao {
        return paymentDatabase.paymentDetailsDao()
    }
}