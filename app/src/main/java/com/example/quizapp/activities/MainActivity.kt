package com.example.quizapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quizapp.R
import com.example.quizapp.adapters.QuizAdapter
import com.example.quizapp.databinding.ActivityMainBinding
import com.example.quizapp.models.Quiz
import com.example.quizapp.utils.UserLoginInfo
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date


// abc@gmail.com qwerty
// ALT+CTRL+L : to format
// CTRL+SHIFT+O : to sync gradle file

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var adapter: QuizAdapter
    private lateinit var firestore: FirebaseFirestore
    private var quizList = mutableListOf<Quiz>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpViews()

    }

    private fun setUpViews() {
        setUpFireStore()
        setUpDrawerLayout()
        setUpQuizRecyclerView()
        setUpDatePicker()
    }

    /**
    setSupportActionBar() - sets the provided app bar as action bar

    The ActionBarDrawerToggle is a helper class provided by Android for integrating the navigation drawer with the action bar.
    The two string resources it takes in constructor is description for navigation icon actions, open & close respectively

    syncState() synchronizes the state of the toggle with the state of the associated DrawerLayout.
    This is necessary for the correct animation and interaction between the action bar and the navigation drawer.

    onOptionsItemSelected(item: MenuItem) - this method is called when user selects an item from the options menu including
    the navigation drawer icon on the action bar. Check if the navigation drawer icon is clicked - if yes, it triggers the animation
    to open/close the drawer.
     */

    private fun setUpDrawerLayout() {
        setSupportActionBar(binding.appBar)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, binding.drawerLayout, R.string.app_name, R.string.app_name)
        actionBarDrawerToggle.syncState()

        binding.apply{
            navigationView.setNavigationItemSelectedListener {
                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                startActivity(intent)
                drawerLayout.closeDrawers()
                true
                /** return true at last says that we have handled this event & nthg more has to be done*/
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpQuizRecyclerView() {
        adapter = QuizAdapter(this, quizList)
        binding.apply {
            quizRecyclerView.layoutManager = GridLayoutManager(this@MainActivity, 2)
            quizRecyclerView.adapter = adapter
        }
    }

    private fun populateDummyData() {
        quizList.add(Quiz("1", "04-06-2002"))
        quizList.add(Quiz("2", "05-06-2002"))
        quizList.add(Quiz("3", "06-06-2012"))
        quizList.add(Quiz("4", "07-06-2022"))
        quizList.add(Quiz("5", "08-06-2032"))
        quizList.add(Quiz("6", "11-06-2001"))
        quizList.add(Quiz("7", "17-06-2023"))
        quizList.add(Quiz("1", "04-06-2002"))
        quizList.add(Quiz("2", "05-06-2002"))
        quizList.add(Quiz("3", "06-06-2012"))
        quizList.add(Quiz("4", "07-06-2022"))
        quizList.add(Quiz("5", "08-06-2032"))
        quizList.add(Quiz("6", "11-06-2001"))
        quizList.add(Quiz("7", "17-06-2023"))
    }

    /**
    addSnapshotListner: takes a snapshot of database and sends it to app.
    Snapshot is basically all data in database at that moment. This listner keeps listening
    for any changes in the database. If any changes occur, it quickly sends a new snapshot,
    thereby keeping app up-to-date.

    Why toObjects()??
    Firestore has data in the form of JSON Objects. So they must be first converted into
    Java/Kotlin objects inorder to use them in app
     */
    private fun setUpFireStore() {
        firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("Quizzes")
        collectionReference.addSnapshotListener { value, error ->
            if (value == null || error != null) {
                Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            for (x in value) {
                Log.d("YperFor", x.data.toString())
            }
            if (value.isEmpty) {
                Log.d("Yper", "value empty")
            }
            Log.d("Yper", value.toString() + " : 1")
            Log.d("Yper", value.toObjects(Quiz::class.java).toString() + " : 2")

            quizList.clear()
            quizList.addAll(value.toObjects(Quiz::class.java))
            Log.d("Yper", "$quizList : 3")
            adapter.notifyDataSetChanged()
        }
    }

    private fun setUpDatePicker() {
        binding.btnDatePicker.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")

            datePicker.addOnPositiveButtonClickListener {
                Log.d("DatePicker", datePicker.headerText)

                val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
                val date = dateFormatter.format(Date(it))
                Log.d("YperDateInMain", date)

                val intent = Intent(this, QuestionActivity::class.java)
                intent.putExtra("DATE", date)
                startActivity(intent)
            }
            datePicker.addOnNegativeButtonClickListener {
                Log.d("DatePicker", "Negative")
            }
            datePicker.addOnCancelListener {
                Log.d("DatePicker", "Cancelled")
            }
        }
    }

}