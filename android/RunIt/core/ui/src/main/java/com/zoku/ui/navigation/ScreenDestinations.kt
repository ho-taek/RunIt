package com.zoku.util

sealed class ScreenDestinations(
    open val route: String
) {
    data object login : ScreenDestinations(route = "login")
    data object home : ScreenDestinations(route = "home")
    data object running : ScreenDestinations(route = "running")
    data object runHistory : ScreenDestinations(route = "home/run-history")
    data object RecordMode : ScreenDestinations(route = "home/record-mode")
    data object RecordModeDetail : ScreenDestinations(route = "home/record-mode/detail")
    data object pause: ScreenDestinations(route = "pause")
}