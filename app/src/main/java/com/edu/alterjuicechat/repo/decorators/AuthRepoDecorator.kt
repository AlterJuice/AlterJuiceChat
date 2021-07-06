package com.edu.alterjuicechat.repo.decorators

import com.edu.alterjuicechat.repo.interfaces.AuthRepo

class AuthRepoDecorator(
    private val localAuthRepo: AuthRepo,
    private val remoteAuthRepo: AuthRepo
): AuthRepo {
    override fun passwordIsValid(password: String): Boolean {
        return localAuthRepo.passwordIsValid(password)
                && remoteAuthRepo.passwordIsValid(password)
    }

    override fun userIsLoggedIn(): Boolean {
        return localAuthRepo.userIsLoggedIn() && remoteAuthRepo.userIsLoggedIn()
    }

    override fun authorizeUser(name: String) {
        remoteAuthRepo.authorizeUser(name)
    }
}