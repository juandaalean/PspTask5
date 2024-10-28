package com.example.psptask5

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private val job = Job() // Job principal para cancelar todas las corrutinas

    private lateinit var tvCatFact: TextView
    private lateinit var tvJoke: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvCatFact = findViewById(R.id.tvCatFact)
        tvJoke = findViewById(R.id.tvJoke)


        // Lanzar las corrutinas en el alcance del job principal
        GlobalScope.launch(Dispatchers.Main + job) {

            try {
                val catFact = getCatFact()
                val joke = getJoke()
                withContext(Main) {
                    tvCatFact.text = catFact
                    tvJoke.text = joke
                }
            } catch (e: Exception) {
                // Manejar errores
                Log.e("MainActivity", "Error al obtener el hecho sobre gatos", e)
                tvCatFact.text = "No se pudo obtener el hecho sobre gatos"
                tvJoke.text = "No se pudo obtener un chiste"

            }
        }



    }

    private suspend fun getCatFact(): String {
        val response = Fuel.get("https://catfact.ninja/fact").awaitString()
        return response
    }

    private suspend fun getJoke(): String {
        // Ajustar la URL según los parámetros de la API
        val response = Fuel.get("https://dog.ceo/api/breeds/image/random").awaitString()
        return response
    }


    // Función para cancelar todas las corrutinas
    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}