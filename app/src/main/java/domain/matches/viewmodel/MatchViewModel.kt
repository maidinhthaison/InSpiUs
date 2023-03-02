package domain.matches.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import data.Result
import data.response.MatchesResponse
import data.response.TeamsResponse
import domain.matches.repository.MatchRepository
import domain.teams.repository.TeamsRepository
import domain.teams.viewmodel.GetTeamsResult
import domain.teams.viewmodel.GetTeamsView
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(private val matchRepository: MatchRepository) :
    ViewModel() {
    private val _getMatchesResult = MutableSharedFlow<GetMatchesResult>()
    val getMatchesResult: SharedFlow<GetMatchesResult> = _getMatchesResult

    fun getListMatches() {
        viewModelScope.launch {
            when (val result = matchRepository.getMatchRepo()) {
                is Result.Success<MatchesResponse?> -> {

                    _getMatchesResult.emit(
                        GetMatchesResult(
                            success = GetMatchesView(
                                matchesResponse = result.data
                            )
                        )
                    )
                }
                is Result.FileNotFoundError -> {
                    _getMatchesResult.emit(
                        GetMatchesResult(
                            error = "File Not Found"
                        )
                    )
                }
                is Result.IOError -> {
                    _getMatchesResult.emit(
                        GetMatchesResult(
                            error = "IO Error"
                        )
                    )
                }
                else -> {
                    _getMatchesResult.emit(
                        GetMatchesResult(
                            error = "Reading user_stories.json causes error!!!"
                        )
                    )
                }
            }
        }
    }

    fun getListMatchesPerTeam(id: String) {
        viewModelScope.launch {
            when (val result = matchRepository.getMatchPerTeamRepo(id)) {
                is Result.Success<MatchesResponse?> -> {

                    _getMatchesResult.emit(
                        GetMatchesResult(
                            success = GetMatchesView(
                                matchesResponse = result.data
                            )
                        )
                    )
                }
                is Result.FileNotFoundError -> {
                    _getMatchesResult.emit(
                        GetMatchesResult(
                            error = "File Not Found"
                        )
                    )
                }
                is Result.IOError -> {
                    _getMatchesResult.emit(
                        GetMatchesResult(
                            error = "IO Error"
                        )
                    )
                }
                else -> {
                    _getMatchesResult.emit(
                        GetMatchesResult(
                            error = "Reading user_stories.json causes error!!!"
                        )
                    )
                }
            }
        }
    }
}