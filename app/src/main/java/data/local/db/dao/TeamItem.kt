package data.local.db.dao

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="TeamTbl")
data class TeamItem(@PrimaryKey @NonNull @ColumnInfo(name = "id") val id: String
                    ,@ColumnInfo(name = "name") val name: String?
                    ,@ColumnInfo(name = "logo") val logo: String?)