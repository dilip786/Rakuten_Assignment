package com.android.rakuten.data.usecases

class BaseUseCase {
    interface NoParamUseCase<Result> {
        suspend fun getAction(): Any?
    }

    interface ParamUseCase<Params, Result> {
        suspend fun getAction(params: Params): Any?
    }

    interface ParamWithNoResultsUseCase<Params> {
        suspend fun getAction(params: Params): Any?
    }
}