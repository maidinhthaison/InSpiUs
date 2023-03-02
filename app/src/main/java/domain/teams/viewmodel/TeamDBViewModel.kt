package domain.teams.viewmodel

import androidx.lifecycle.*
import data.local.db.dao.TeamItem
import data.local.db.repository.TeamTblRepository
import kotlinx.coroutines.launch

class TeamDBViewModel (private val teamTblRepository: TeamTblRepository ) : ViewModel() {

    val allItems : LiveData<MutableList<TeamItem>> = teamTblRepository.allTeams.asLiveData()

    // Launching a new coroutine to insert the data in a non-blocking way
    fun insert(teamItem: TeamItem) = viewModelScope.launch {
        teamTblRepository.insert(teamItem)
    }

    fun delete(teamItem: TeamItem) = viewModelScope.launch {
        teamTblRepository.delete(teamItem)
    }

    class TeamItemViewModelFactory(private val teamTblRepository: TeamTblRepository)
        : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TeamDBViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return TeamDBViewModel(teamTblRepository) as T
            }
            throw IllegalArgumentException("Unknown VieModel Class")
        }

    }
}