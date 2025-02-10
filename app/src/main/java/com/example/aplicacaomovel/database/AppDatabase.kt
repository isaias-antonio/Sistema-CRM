package com.example.aplicacaomovel.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.aplicacaomovel.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Inserir usuários preconfigurados quando o banco for criado
                        val userDao = INSTANCE?.userDao()
                        if (userDao != null) {
                            CoroutineScope(Dispatchers.IO).launch {
                                // Inserir usuários preconfigurados
                                userDao.insert(User(username = "user1@gmail.com", password = "senha1"))
                                userDao.insert(User(username = "user2@gmail.com", password = "senha2"))
                            }
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}



