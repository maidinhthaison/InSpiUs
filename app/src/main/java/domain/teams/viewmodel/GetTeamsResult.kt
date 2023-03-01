package domain.teams.viewmodel

data class GetTeamsResult(
    val loading: Boolean = false,
    val success: GetTeamsView? = null,
    val error: String? = null
)