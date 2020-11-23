package com.example.tribune.middleware

import com.example.tribune.FeedAction
import com.example.tribune.FeedState
import com.example.tribune.Repository
import com.example.tribune.mvi.Middleware
import kotlinx.coroutines.flow.*

class FirstPageMiddleware(
    private val repository: Repository
) : Middleware<FeedAction, FeedState> {

    override fun transform(
        actions: Flow<FeedAction>,
        state: StateFlow<FeedState>,
    ): Flow<FeedAction> =
        actions.filterIsInstance<FeedAction.LoadFirstPage>()
            .map {
                repository.getRecent().let {
                    if (it.isSuccessful) {
                        FeedAction.FirstPageLoaded(it.body().orEmpty())
                    } else {
                        FeedAction.FirstPageError
                    }
                }
            }.catch {
                emit(
                    FeedAction.FirstPageError
                )
            }
}