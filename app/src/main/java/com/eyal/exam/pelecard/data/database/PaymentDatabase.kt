package com.eyal.exam.pelecard.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.eyal.exam.pelecard.data.dao.PaymentDetailsDao
import com.eyal.exam.pelecard.data.entities.PaymentDetails
import com.eyal.exam.pelecard.data.migrations.MIGRATION_1_2

@Database(entities = [PaymentDetails::class], version = 2, exportSchema = false)
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
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}