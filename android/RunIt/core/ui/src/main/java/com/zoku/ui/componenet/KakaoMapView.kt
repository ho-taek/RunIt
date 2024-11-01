package com.zoku.ui.componenet

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapType
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.route.RouteLine
import com.kakao.vectormap.route.RouteLineLayer
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.route.RouteLineStyles
import com.kakao.vectormap.route.RouteLineStylesSet
import com.zoku.ui.model.LocationData
import com.zoku.ui.routeColor

@Composable
fun KakaoMapView(
    modifier: Modifier = Modifier,
    totalLocationList: List<LocationData>
) {

    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    AndroidView(
        modifier = modifier
            .fillMaxSize(),
        factory = { _ ->
            mapView.apply {
                mapView.start(
                    object : MapLifeCycleCallback() {
                        override fun onMapDestroy() {
                            Log.d("확인", "카카오 맵 에러")
                        }

                        override fun onMapError(p0: Exception?) {
                            Log.d("확인", "카카오 맵 에러")
                        }
                    },

                    object : KakaoMapReadyCallback() {
                        override fun onMapReady(kakaoMap: KakaoMap) {

                            kakaoMap.changeMapType(MapType.NORMAL)

                            val routeLineManager = kakaoMap.routeLineManager!!
                            val routeLineLayer: RouteLineLayer = routeLineManager.layer

                            val routeLineStyles = RouteLineStyles.from(
                                RouteLineStyle.from(10f, routeColor.toArgb())
                            )
                            val stylesSet = RouteLineStylesSet.from(routeLineStyles)

                            val latLngList = totalLocationList.map {
                                LatLng.from(it.latitude, it.longitude)
                            }
                            if (latLngList.size > 1) {
                                val segment = RouteLineSegment.from(latLngList)
                                    .setStyles(stylesSet.getStyles(0))

                                val routeLineOptions = RouteLineOptions.from(segment)
                                    .setStylesSet(stylesSet)

                                routeLineLayer.addRouteLine(routeLineOptions)

                                val centerLat = latLngList.map { it.latitude }.average()
                                val centerLng = latLngList.map { it.longitude }.average()
                                val cameraUpdate = CameraUpdateFactory.newCenterPosition(
                                    LatLng.from(centerLat, centerLng)
                                )
                                kakaoMap.moveCamera(cameraUpdate)
                            }
                        }
                    },
                )
            }

        }
    )

}