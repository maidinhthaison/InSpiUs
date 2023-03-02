package domain.teams.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import data.Result
import data.response.TeamsResponse
import domain.teams.repository.TeamsRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamsViewModel @Inject constructor(private val teamsRepository: TeamsRepository) :
    ViewModel() {
    private val _getTeamsResult = MutableSharedFlow<GetTeamsResult>()
    val getTeamsResult: SharedFlow<GetTeamsResult> = _getTeamsResult

    fun getListTeams() {
        viewModelScope.launch {
            when (val result = teamsRepository.getTeamRepo()) {
                is Result.Success<TeamsResponse?> -> {

                    _getTeamsResult.emit(
                        GetTeamsResult(
                            success = GetTeamsView(
                                teamsResponse = result.data
                            )
                        )
                    )
                }
                is Result.FileNotFoundError -> {
                    _getTeamsResult.emit(
                        GetTeamsResult(
                            error = "File Not Found"
                        )
                    )
                }
                is Result.IOError -> {
                    _getTeamsResult.emit(
                        GetTeamsResult(
                            error = "IO Error"
                        )
                    )
                }
                else -> {
                    _getTeamsResult.emit(
                        GetTeamsResult(
                            error = "Reading user_stories.json causes error!!!"
                        )
                    )
                }
            }
        }
    }

}