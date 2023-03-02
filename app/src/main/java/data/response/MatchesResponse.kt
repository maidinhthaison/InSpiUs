package data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import utils.constant.*
import java.text.SimpleDateFormat
import java.util.Date

@Parcelize
data class MatchesResponse(
    @SerializedName("matches")
    val matches: Matches
): Parcelable

@Parcelize
data class Matches(

    @SerializedName("previous")
    val listPrevious: List<Match?>,

    @SerializedName("upcoming")
    val listUpcoming: List<Match?>

): Parcelable

@Parcelize
data class Match(
    @SerializedName("date")
    val date: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("home")
    val home: String?,

    @SerializedName("away")
    val away: String?,

    @SerializedName("winner")
    val winner: String?,

    @SerializedName("highlights")
    val highlights: String?
): Parcelable{
    fun formatDate(): String? {
        val sdf = SimpleDateFormat(MATCH_DATE_FORMAT)
        return sdf.format(parseMatchDate(date.toString(), MATCH_DATE_FORMAT_ORIGIN))
    }
    fun toUpcomingTime(): Long? {
        val comingDate = parseMatchDate(date.toString(), MATCH_DATE_FORMAT_ORIGIN)
        return comingDate?.let { System.currentTimeMillis().minus(it.time) }
    }
    fun getDomainName() : String {
        return highlights?.substringAfterLast("/").toString()
    }
}