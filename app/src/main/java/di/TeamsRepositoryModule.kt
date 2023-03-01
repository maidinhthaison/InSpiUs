package di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import data.local.AppLocalDataSource
import data.remote.TeamsRemoteDataSource
import domain.teams.repository.TeamsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TeamsRepositoryModule {

    @Singleton
    @Provides
    fun provideTeamsRepository(
        localDataSource: AppLocalDataSource,
        remoteDataSource: TeamsRemoteDataSource,
    ) = TeamsRepository(localDataSource, remoteDataSource)
}