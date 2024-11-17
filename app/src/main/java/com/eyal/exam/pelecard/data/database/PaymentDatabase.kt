package com.eyal.exam.pelecard.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.eyal.exam.pelecard.data.dao.PaymentDetailsDao
import com.eyal.exam.pelecard.data.entities.PaymentDetails

@Database(entities = [PaymentDetails::class], version = 1, exportSchema = false)
abstract class PaymentDatabase : RoomDatabase() {
    abstract fun paymentDetailsDao(): PaymentDetailsDao

    companion object {
        @Volatile
        private var INSTANCE: PaymentDatabase? = null

        fun getDatabase(context: Context): PaymentDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PaymentDatabase::class.java,
                    "payment_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}