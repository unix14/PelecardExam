package com.eyal.exam.pelecard.data.migrations

import android.util.Log
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/// Added isCompleted: Bool = false
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE payment_details ADD COLUMN isCompleted INTEGER NOT NULL DEFAULT 0")
        // Add logging to verify migration execution
        Log.d("RoomMigration", "Migration from 1 to 2 executed")
    }
}