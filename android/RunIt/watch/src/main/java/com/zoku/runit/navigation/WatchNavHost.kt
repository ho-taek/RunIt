package com.zoku.runit.navigation


import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zoku.runit.model.ExerciseResult
import com.zoku.runit.screen.HomeScreen
import com.zoku.runit.screen.RunningPauseScreen
import com.zoku.runit.screen.RunningScreen
import timber.log.Timber

@Composable
fun WatchNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = WatchScreenDestination.home.route,
    sendBpm: (Int) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(route = WatchScreenDestination.home.route) {
            HomeScreen(modifier) {
                navController.navigate(WatchScreenDestination.running.route)
            }
        }

        composable(route = WatchScreenDestination.running.route) {
            RunningScreen(modifier, onPauseClick =
            { exerciseResult ->
                navController.run {
                    Timber.tag("WatchNavHost RunningScreen Result").d("${exerciseResult}")
                    this.navigate(WatchScreenDestination.runningPause.createRoute(exerciseResult))
                }
            },
                sendBpm = { bpm ->
                    sendBpm(bpm)
                }
            )
        }
        composable(
            route = WatchScreenDestination.runningPause.route,
            WatchScreenDestination.runningPause.arguments
        ) { backStackEntry ->
            val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                backStackEntry.arguments?.getParcelable("result", ExerciseResult::class.java)
            } else {
                backStackEntry.arguments?.getParcelable("result")
            }

            Timber.tag("WatchNavHost").d("result $data")
            RunningPauseScreen(modifier, data,
                onStopClick = {
                    navController.popBackStack(
                        route = WatchScreenDestination.home.route,
                        inclusive = false
                    )
                },
                onResumeClick = {
                    navController.popBackStack(
                        route = WatchScreenDestination.running.route,
                        inclusive = false
                    )

                }
            )
        }

    }


}