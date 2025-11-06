package com.example.univibe.di

import com.example.univibe.data.repository.AuthRepositoryImpl
import com.example.univibe.domain.repository.AuthRepository
import com.example.univibe.domain.use_case.AuthUseCases
import com.example.univibe.domain.use_case.LoginUseCase
import com.example.univibe.domain.use_case.RegisterUseCase
import com.example.univibe.domain.use_case.GoogleSignInUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    @Singleton
    fun provideAuthUseCases(repository: AuthRepository): AuthUseCases {
        return AuthUseCases(
            login = LoginUseCase(repository),
            register = RegisterUseCase(repository),
            googleSignIn = GoogleSignInUseCase(repository)
        )
    }
}