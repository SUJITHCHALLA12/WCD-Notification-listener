package com.pentester.wcd.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [MessageEntity::class, NotificationEntity::class],
    version = 3,
    exportSchema = false
)
abstract class WcdDatabase : RoomDatabase() {

    abstract fun dao(): MessageDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: WcdDatabase? = null

        // Migration from version 1 to 2: add new columns to notifications table
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE notifications ADD COLUMN isDeleted INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE notifications ADD COLUMN isGroup INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE notifications ADD COLUMN groupName TEXT")
            }
        }

        // Migration from version 2 to 3: add category column
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE notifications ADD COLUMN category TEXT NOT NULL DEFAULT 'chats'")
            }
        }

        fun getDatabase(context: Context): WcdDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WcdDatabase::class.java,
                    "wcd_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
