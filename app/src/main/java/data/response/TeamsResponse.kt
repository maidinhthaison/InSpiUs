package data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TeamsResponse(
    @SerializedName("teams")
    val listTeams: List<Team>
): Parcelable

@Parcelize
data class Team(
    @SerializedName("id")
    val id: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("logo")
    val logo: String?
): Parcelable