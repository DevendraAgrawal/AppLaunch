package com.applaunch.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.applaunch.db.daw.UserFormDao
import com.applaunch.db.entity.UserForm

@Database(entities = [UserForm::class], version = 2)
abstract class UserFormDatabase: RoomDatabase() {
    abstract fun userFormDao(): UserFormDao

    companion object {
        private var instance: UserFormDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): UserFormDatabase {
            if(instance == null)
                instance = Room.databaseBuilder(ctx.applicationContext, UserFormDatabase::class.java,
                    "user_form")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()

            return instance!!

        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }
        }
    }
}