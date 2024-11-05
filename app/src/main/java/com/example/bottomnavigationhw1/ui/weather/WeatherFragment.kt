package com.example.bottomnavigationhw1.ui.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.bottomnavigationhw1.R
import com.example.bottomnavigationhw1.databinding.FragmentWeatherBinding
import com.example.bottomnavigationhw1.ui.weather.currentWeather.utils.RetrofitInstance
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!


    private lateinit var position: String
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2
    private lateinit var cityCoordinate: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater,container,false)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val permissionLauncherInternet = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Log.d("@@@", "Интернет разрешение дано")
            } else {
                Log.d("@@@", "Интернет разрешение не дано")
            }
        }

        permissionLauncherInternet.launch(Manifest.permission.INTERNET)

        getLocation()
        getCurrentWeather()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {

        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        val list: MutableList<Address>? =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        cityCoordinate = list?.get(0)?.locality.toString()
                        binding.currentLocal.text = "Текущее местоположение: г. ${cityCoordinate}"
                        Log.d("@@@", "City ${cityCoordinate}")
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please turn on location", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
        return
    }


    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        ) {
            Log.d("@@@", "Разрешение на определение местоположения - дано")
            return true
        }
        Log.d("@@@", "Разрешение на определение местоположения - дано")
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }



    ///Тут ?
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun getCurrentWeather() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {

                cityCoordinate.let {
                    RetrofitInstance.api.getCurrentWeatherAboutCity(
                        it, "metric", requireContext().getString(R.string.api_key)
                    )
                }

            } catch (e: IOException) {
                Toast.makeText(
                    requireContext(), "app error ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
                return@launch
            } catch (e: HttpException) {
                Toast.makeText(
                    requireContext(), "http error ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
                return@launch
            }
            if (response != null) {
                if (response.isSuccessful && response.body() != null) {
                    withContext(Dispatchers.Main) {
                        val data = response.body()
                        Log.d("@@@","Данные с сервера погоды - ${data}")

//                        binding.currentTemperature.text = "Текущая температура воздуха - " + "${data?.main?.temp}\u00B0"
//
//                        binding.currentWeather.text = data?.weather?.get(0)?.main.toString()

                    }
                }
            }
        }
    }

}