package yatsekk.example.com.myweatherkt

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import yatsekk.example.com.myweatherkt.ui.screen.CurrentWeatherScreen
import yatsekk.example.com.myweatherkt.ui.screen.DailyWeatherScreen
import yatsekk.example.com.myweatherkt.ui.screen.HourlyWeatherScreen
import yatsekk.example.com.myweatherkt.ui.theme.MyWeatherKtTheme
import yatsekk.example.com.myweatherkt.ui.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {

    private val weatherViewModel: WeatherViewModel by viewModels()

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            fetchLocationAndLoad()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (savedInstanceState == null) {
            if (hasLocationPermission()) {
                fetchLocationAndLoad()
            } else {
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }

        setContent {
            MyWeatherKtTheme {
                WeatherApp()
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun fetchLocationAndLoad() {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val lastLocation = tryGetLastKnownLocation(locationManager)
        if (lastLocation != null) {
            weatherViewModel.loadWeatherByCoords(lastLocation.latitude, lastLocation.longitude)
        } else {
            requestSingleLocationUpdate(locationManager)
        }
    }

    private fun tryGetLastKnownLocation(locationManager: LocationManager): Location? {
        return try {
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                ?: locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        } catch (e: SecurityException) {
            null
        }
    }

    private fun requestSingleLocationUpdate(locationManager: LocationManager) {
        val providers = listOf(LocationManager.NETWORK_PROVIDER, LocationManager.GPS_PROVIDER)
        val enabledProvider = providers.firstOrNull { locationManager.isProviderEnabled(it) } ?: return

        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                locationManager.removeUpdates(this)
                weatherViewModel.loadWeatherByCoords(location.latitude, location.longitude)
            }

            override fun onProviderDisabled(provider: String) {
                locationManager.removeUpdates(this)
            }
        }

        try {
            locationManager.requestLocationUpdates(
                enabledProvider, 0L, 0f, listener, Looper.getMainLooper()
            )
            // Remove listener after 15 s to avoid leak if no fix is obtained
            Handler(Looper.getMainLooper()).postDelayed({
                locationManager.removeUpdates(listener)
            }, 15_000L)
        } catch (e: SecurityException) {
            // Permission was revoked between check and use; fall back to idle state
        }
    }
}

@Composable
fun WeatherApp() {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text(stringResource(R.string.tab_current)) }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.List, contentDescription = null) },
                    label = { Text(stringResource(R.string.tab_hourly)) }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                    label = { Text(stringResource(R.string.tab_daily)) }
                )
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            0 -> CurrentWeatherScreen(modifier = Modifier.padding(innerPadding))
            1 -> HourlyWeatherScreen(modifier = Modifier.padding(innerPadding))
            2 -> DailyWeatherScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}

