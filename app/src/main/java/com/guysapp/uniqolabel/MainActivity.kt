package com.guysapp.uniqolabel

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.androidstudy.networkmanager.Tovuti
import com.androidstudy.networkmanager.Monitor
import android.content.pm.PackageManager
import android.location.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*
import android.location.Geocoder
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.zxing.integration.android.IntentIntegrator
import com.guysapp.uniqolabel.ForecastModel.ForecastResponse
import com.guysapp.uniqolabel.ForecastModel.ListItem
import com.guysapp.uniqolabel.Response.Response
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import java.text.SimpleDateFormat


class MainActivity : AppCompatActivity() {


    lateinit var location : Location

     var lat : Double = 0.0
    var lng : Double = 0.0
    var cityName = ""
    var stateName = "";

    private var locationManager : LocationManager? = null

    val TAG = "MainActivity"
    val pref_name = "uniqolabel"
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pullToRefresh.setOnRefreshListener {
            pullToRefresh.isRefreshing = false
            Toast.makeText(this,"weather updated", Toast.LENGTH_SHORT).show()
//            obtieneLocalizacion()
            fetchTemp(lat,lat)
            fetchForecast(lat,lng)



        }


        progressbarrecycler.visibility = View.VISIBLE
        floatingActionButton.visibility = View.GONE

        floatingActionButton.setOnClickListener{

            var intent =  IntentIntegrator(this)
                intent.initiateScan();

        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101)
        }else{
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?;

            try {
                // Request location updates
                locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener);
            } catch(ex: SecurityException) {
                Log.d("myTag", "Security Exception, no location available");
            }
        }







        tx_lastupdate.visibility = View.GONE

        linear_addon.visibility = View.GONE

        linear_type.visibility = View.GONE
        tx_temp.visibility = View.GONE
        tx_location.setText("Fetching...")
        temp_icon.visibility = View.GONE
        animation_view.visibility = View.VISIBLE



        Tovuti.from(this).monitor(object : Monitor.ConnectivityListener {
            override fun onConnectivityChanged(connectionType: Int, isConnected: Boolean, isFast: Boolean) {

                Log.e(TAG,isConnected.toString()+" : Connection")
                if (isConnected){
                }
                else {
                    progressbarrecycler.visibility = View.GONE
                    tx_temp.visibility = View.GONE
                    tx_location.setText("Fetching...")
                    animation_view.setAnimation(R.raw.internetconnection)
                    animation_view.visibility = View.VISIBLE
                }
            }
        })



    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
//                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                var content = result.contents;
                var parts = content.split(",");
                Log.d(TAG,parts[0]);
                Log.d(TAG,parts[1]);

               fetchTemp(parts[0].toDouble(),parts[1].toDouble());
               fetchForecast(parts[0].toDouble(),parts[1].toDouble());




            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    override fun onStop() {
        super.onStop()
        Tovuti.from(this).stop()
    }



    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            101 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                  Log.e(TAG,"Permission granted")
                    Toast.makeText(this@MainActivity,"Permission granted",Toast.LENGTH_SHORT).show()


                } else {

                }
                return
            }


            else -> {
            }
        }


    }


    fun fetchTemp(latitude: Double ,longitude: Double ){
        val endpoints = RetrofitClientInstance.getRetrofitInstance().create(Endpoints::class.java)

        val data = HashMap<String,String>()

        Log.d(TAG,latitude.toString() +" ,"+longitude);
        data.put("lat",latitude.toString())
        data.put("lon",longitude.toString())
        data.put("appid", Constant.API_KEY)
        data.put("units", Constant.UNITS)
        val call = endpoints.getWeatherResponse(data)

        call.enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {

                Log.e(TAG,response.code().toString())

                @SuppressLint("RestrictedApi")
                floatingActionButton.visibility = View.VISIBLE

                if (response.code()==200){
                    linear_addon.visibility = View.VISIBLE

                    tx_lastupdate.visibility = View.VISIBLE

                    im_pressure.setImageResource(R.drawable.pressure)
                    im_humidity.setImageResource(R.drawable.humidity)

                    tx_pressure.setText(response.body()?.main?.pressure.toString())
                    tx_humidity.setText(response.body()?.main?.humidity.toString())
                    temp_icon.visibility = View.VISIBLE
                    linear_type.visibility = View.VISIBLE
                    tx_location.setText(response.body()?.name+", "+response.body()?.sys?.country)
                    Log.e(TAG,response.body()?.main?.temp.toString())
                    tx_temp.visibility = View.VISIBLE
                    tx_temp.setText(response.body()?.main?.temp.toString()+ 0x00B0.toChar())
                    progressBar.visibility = View.GONE
                    animation_view.visibility = View.GONE
                    tx_temp_type.setText(response.body()?.weather?.get(0)?.main)
                    if (response.body()?.weather?.get(0)?.main.equals("haze",true)){
                        temp_icon.setImageResource(R.drawable.haze)
                        im_type.setImageResource(R.drawable.haze_multicolor)

                    }
                    if (response.body()?.weather?.get(0)?.main.equals("thunderstorm",true)){
                        temp_icon.setImageResource(R.drawable.thunderstorm)
                        im_type.setImageResource(R.drawable.thunderstorm_multicolor)

                    }
                    if (response.body()?.weather?.get(0)?.main.equals("mist",true)){
                        temp_icon.setImageResource(R.drawable.mist)
                        im_type.setImageResource(R.drawable.mist_multicolor)

                    }

                    if (response.body()?.weather?.get(0)?.main.equals("clouds",true)){
                        temp_icon.setImageResource(R.drawable.clouds)
                        im_type.setImageResource(R.drawable.cloudy_day_multicolor)
                    }

                    if (response.body()?.weather?.get(0)?.main.equals("rain",true)){
                        temp_icon.setImageResource(R.drawable.rain)

                        im_type.setImageResource(R.drawable.rain_multicolor)
                    }




                    var d =  Date(response.body()?.dt?.toLong()!!.times(1000));
                    var simpledate  = SimpleDateFormat("dd,MMM,yyyy hh:mm:ss aa");
                    System.out.println(simpledate.format(d))

                    tx_lastupdate.setText("Last Recorded update - "+simpledate.format(d))

                }




            }

            override fun onFailure(call: Call<Response>, t: Throwable) {

                Log.e("MainActivity",t.message)

            }
        })

    }
    private val locationListener: android.location.LocationListener = object :
        android.location.LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.e(TAG,"" + location.longitude + ":" + location.latitude);
            lat = location.latitude;
            lng = location.longitude;
            geoDecoder(location)

        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    fun geoDecoder(location: Location){

        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        val address = addresses.get(0)
        cityName = address.getLocality()
        stateName = address.adminArea

        System.out.println(cityName)
        val countryname = address.countryName
        fetchTemp(location.latitude,location.longitude)

        fetchForecast(location.latitude,location.longitude)
    }


    fun initiliazeRecyclerView(list :List<ListItem>){

        recyclerview.adapter = CustomAdapter(this,list)

        recyclerview.setItemViewCacheSize(0)
        recyclerview.layoutManager = LinearLayoutManager(this)

    }

    fun fetchForecast(latitude: Double,longitude: Double){

        progressbarrecycler.visibility = View.VISIBLE

        val endpoints = RetrofitClientInstance.getRetrofitInstanceForecast().create(Endpoints::class.java)

        val data = HashMap<String,String>()

        data.put("lat",latitude.toString())
        data.put("lon",longitude.toString())
        data.put("appid", Constant.API_KEY)
        data.put("units", Constant.UNITS)
        val call = endpoints.getForecastResponse(data)

        call.enqueue(object : Callback<ForecastResponse> {
            override fun onResponse(call: Call<ForecastResponse>, response: retrofit2.Response<ForecastResponse>) {

                @SuppressLint("RestrictedApi")
                floatingActionButton.visibility = View.VISIBLE
                progressbarrecycler.visibility = View.GONE

                var element : List<ListItem> ;
                element = response.body()!!.list
                Log.d(TAG,"Forecast :"+element.size)
                initiliazeRecyclerView(element)

            }

            override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {

                Log.e("MainActivity",t.message)

            }
        })


    }




}




