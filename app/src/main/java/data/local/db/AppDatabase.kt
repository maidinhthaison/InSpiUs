package data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import data.local.db.dao.TeamDao
import data.local.db.dao.TeamItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [TeamItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun teamDao(): TeamDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "inspius_database"
                ).addCallback(TeamItemCallback(scope))
                    .build()

                INSTANCE = instance

                // return instance
                instance
            }
        }
    }

    private class TeamItemCallback(val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let { teamItemRoomDB ->
                scope.launch {
                    teamItemRoomDB.teamDao().insert(
                        TeamItem(
                            "767ec50c-7fdb-4c3d-98f9-d6727ef8252b", "Team Red Dragons",
                            "https://tstzj.s3.amazonaws.com/dragons.png"
                        )
                    )
                    teamItemRoomDB.teamDao().insert(
                        TeamItem(
                            "7b4d8114-742b-4410-971a-500162101158", "Team Cool Eagles",
                            "https://tstzj.s3.amazonaws.com/eagle.png"
                        )
                    )
                    teamItemRoomDB.teamDao().insert(
                        TeamItem(
                            "efb06ca2-78bc-448e-bda5-a6af9eccdcd0", "Team Chill Elephants",
                            "https://tstzj.s3.amazonaws.com/elephant.png"
                        )
                    )
                }
            }
        }
    }
}
