package com.lucasalfare.flrefs.main

interface AppError

interface AppResult<out D, out E : AppError>

data class Success<D, E : AppError>(val data: D) : AppResult<D, E>

data class Failure<D, E : AppError>(val error: E) : AppResult<D, E>