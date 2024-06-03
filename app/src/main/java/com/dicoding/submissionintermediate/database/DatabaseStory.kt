package com.dicoding.submissionintermediate.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.submissionintermediate.viewmodel.Story

@Database(
    entities = [Story::class, RemoteKey::class],
    version = 1,
    exportSchema = false
)
abstract class DatabaseStory: RoomDatabase() {
    abstract fun storyDao(): StroyDAO
    abstract fun remoteKeysDao(): RemoteKeyDAO

    companion object {
        @Volatile
        private var INSTANCE: DatabaseStory? = null

        fun getDatabase(context: Context): DatabaseStory {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseStory::class.java, "story_database"
                )
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}