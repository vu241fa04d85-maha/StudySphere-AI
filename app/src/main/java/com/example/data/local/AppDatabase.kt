package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.model.*

@Database(
    entities = [
        UserProfile::class,
        AdminProfile::class,
        StudyFile::class,
        FileSummary::class,
        ChatConversation::class,
        ChatMessage::class,
        Quiz::class,
        QuizQuestion::class,
        QuizAttempt::class,
        OfficialTest::class,
        TestQuestion::class,
        TestAttempt::class,
        Hackathon::class,
        HackathonParticipant::class,
        CodeSession::class,
        StudyPlan::class,
        StudyTask::class,
        Announcement::class,
        AppNotification::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun studyDao(): StudyDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ai_study_assistant.db"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
