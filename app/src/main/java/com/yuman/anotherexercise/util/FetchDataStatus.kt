package com.yuman.anotherexercise.util

/**
 * define different status of fetch data
 * using enum is easier to add more status
 */
enum class FetchDataStatus {
    NETWORK_ERROR, // network error, such no network or timeout
    NETWORK_NOT_SUCCESS, // got server response data, but with success=false
    FETCHED_FROM_LOCAL, // get data from local database
    FETCHED_FROM_REMOTE // get data from remote server
}