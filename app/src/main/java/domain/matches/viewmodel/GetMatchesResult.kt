package domain.matches.viewmodel

data class GetMatchesResult(
    val loading: Boolean = false,
    val success: GetMatchesView? = null,
    val error: String? = null
)