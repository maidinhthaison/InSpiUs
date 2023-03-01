package com.jetpack.demo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import data.Result
import data.response.Team
import data.response.TeamsResponse
import domain.teams.repository.TeamsRepository
import domain.teams.viewmodel.TeamsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TeamsViewModelTest {
    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var teamsViewModel: TeamsViewModel

    lateinit var teamsRepository: TeamsRepository


    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        teamsRepository = mock(teamsRepository::class.java)
        teamsViewModel = TeamsViewModel(teamsRepository)
    }

    @Test
    fun getListCurrencyTest() {
        runBlocking {
            Mockito.`when`(teamsRepository.getTeamRepo())
                .thenReturn(Result.Success<TeamsResponse>(TeamsResponse(listOf<Team>(Team("a", "b", "c",)))))
            val result = teamsViewModel.getListTeams()
            assertEquals((listOf<Team>(Team("a", "b", "c"))), result)
        }
    }


    @Test
    fun `empty list test`() {
        runBlocking {
            Mockito.`when`(teamsRepository.getTeamRepo())
                .thenReturn(Result.Success<TeamsResponse>(TeamsResponse(listOf<Team>(Team("a", "b", "c")))))
            val result = teamsViewModel.getListTeams()
            assertEquals(null, result)
        }
    }

}