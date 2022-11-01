package com.example.to_do

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Data::class], version = 1, exportSchema = false)
abstract class TaskDatabase: RoomDatabase() {

    abstract fun dataDao(): DataDao

    companion object {

        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "word_database"
                ).fallbackToDestructiveMigration()
                   // .addCallback(databaseCallback)
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
//        private val databaseCallback: Callback = object : Callback() {
//            override fun onCreate(db: SupportSQLiteDatabase) {
//                super.onCreate(db)
//                populate(INSTANCE).execute()
//            }
//        }
//        class populate(db: TaskDatabase?) : AsyncTask<Void?, Void?, Void?>() {
//
//            var dataDao: DataDao
//
//            init {
//                dataDao = db!!.dataDao()
//            }
//
//            override fun doInBackground(vararg p0: Void?): Void? {
//                dataDao.insert(Data("Danis", "Android ", "25 AUG 2023", "25 AUG 2001"))
//                dataDao.insert(Data("Danis", "Android devr", "25 AUG 2023", "25 AUG 2001"))
//                dataDao.insert(Data("Danis", "Android lo", "25 AUG 2023", "25 AUG 2001"))
//                dataDao.insert(Data("Danis", "Android develttttttttt", "25 AUG 2023", "25 AUG 2001"))
//                return null
//            }
//        }
    }
}