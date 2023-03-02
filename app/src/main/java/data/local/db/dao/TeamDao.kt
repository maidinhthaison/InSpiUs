package data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {
    @Query("SELECT * FROM TeamTbl")
    fun getAllTeams(): Flow<MutableList<TeamItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(sellItem: TeamItem)

    @Delete
    suspend fun delete(sellItem: TeamItem)

    @Delete
    suspend fun update(sellItem: TeamItem)
}