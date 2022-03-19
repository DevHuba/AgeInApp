package com.example.ageinsecondsapp

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor


class MainActivity : AppCompatActivity() {

    private var tvSelectedDate: TextView? = null
    private var tvResults: TextView? = null
    private var tvDescription: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Texts
        tvSelectedDate = findViewById(R.id.tvSelectedDate)
        tvResults = findViewById(R.id.tvResultInMinutes)
        tvDescription = findViewById(R.id.tvDescription)

        //Settings spinner logic
        val settings = resources.getStringArray(R.array.settings)
        val settingsSpinner: Spinner = findViewById(R.id.settingsSpinner)
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, settings)
        settingsSpinner.adapter = adapter

        settingsSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {

                //Date pick button logic
                val btnDatePicker: Button = findViewById(R.id.btnSelectDate)
                btnDatePicker.setOnClickListener {

                    clickDatePicker(settings[position])
                }

                when {
                    settings[position] == "Minutes" -> {
                        tvDescription?.text = "Minutes You`r alive"
                    }
                    settings[position] == "Hours" -> {
                        tvDescription?.text = "Hours You`r alive"
                    }
                    settings[position] == "Days" -> {
                        tvDescription?.text = "Days You`r alive"
                    }
                    settings[position] == "Months" -> {
                        tvDescription?.text = "Months You`r alive"
                    }
                    settings[position] == "Years" -> {
                        tvDescription?.text = "Years You`r alive"
                    }
                    else -> {
                        Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(this@MainActivity, "Nothing was selected", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun clickDatePicker(selectedSetting: String) {
        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val day = myCalendar.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            this,
            { _, pickedYear, pickedMonth, pickedDay ->
                //Save user selected date
                val selectedDate =
                    "${pickedDay}/${pickedMonth + 1}/${pickedYear}"
                //Display user selected date
                tvSelectedDate?.text = selectedDate

                //Parse and transform selected user date
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                val theDate = sdf.parse(selectedDate)



                //Check for not null of parsed date
                theDate?.let {
                    val selectedMinutes = theDate.time / 60000
                    val selectedHours = selectedMinutes / 60
                    val selectedDays = selectedHours / 24
                    val selectedMonths = selectedDays / 30.4167
                    val selectedYears = selectedMonths / 12.0

                    //Get current time in millis
                    val currentDate =
                        sdf.parse(sdf.format(System.currentTimeMillis()))
                    currentDate?.let {
                        //Transform current date
                        val currentMinutes = currentDate.time / 60000
                        val currentHours = currentMinutes / 60
                        val currentDays = currentHours / 24
                        val currentMonths = currentDays / 30.4167
                        val currentYear = currentMonths / 12

                        //Calculate date
                        val differenceMinutes = currentMinutes - selectedMinutes
                        val differenceHours = currentHours - selectedHours
                        val differenceDays = currentDays - selectedDays
                        val differenceMonths = currentMonths - selectedMonths
                        val differenceYears = currentYear - selectedYears



                        //Dependent on what setting selected change displayed result
                        when (selectedSetting) {
                            "Minutes" -> {
                                tvResults?.text = differenceMinutes.toString()
                            }
                            "Hours" -> {
                                tvResults?.text = differenceHours.toString()
                            }
                            "Days" -> {
                                tvResults?.text = differenceDays.toString()
                            }
                            "Months" -> {
                                tvResults?.text = (floor(differenceMonths * 100.0) / 100.0).toString()
                            }
                            "Years" -> {
                                tvResults?.text = (floor(differenceYears * 100.0) / 100.0).toString()
                            }
                            else -> {
                                Toast.makeText(this, "Error in date picker", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }


            }, year, month, day
        )
        dpd.datePicker.maxDate = System.currentTimeMillis() - 86400000
        dpd.show()
    }
}