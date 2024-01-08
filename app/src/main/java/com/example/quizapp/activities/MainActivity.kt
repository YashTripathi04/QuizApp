package com.example.quizapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quizapp.R
import com.example.quizapp.adapters.QuizAdapter
import com.example.quizapp.databinding.ActivityMainBinding
import com.example.quizapp.models.Quiz
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Date


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

        binding.apply {
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
        val collectionReference =
            firestore.collection("Quizzes").orderBy("title", Query.Direction.DESCENDING)

        collectionReference.addSnapshotListener { value, error ->
            if (value == null || error != null) {
                Toast.makeText(this, "Error fetching data....", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            quizList.clear()
            quizList.addAll(value.toObjects(Quiz::class.java))

            adapter.notifyDataSetChanged()
        }
    }

    private fun setUpDatePicker() {
        binding.btnDatePicker.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")

            datePicker.addOnPositiveButtonClickListener {
                val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
                val date = dateFormatter.format(Date(it))

                val intent = Intent(this, QuestionActivity::class.java)
                intent.putExtra("DATE", date)
                startActivity(intent)
            }
        }
    }
}