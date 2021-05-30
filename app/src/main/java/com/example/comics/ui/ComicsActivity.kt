package com.example.comics.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.example.comics.R
import com.example.comics.utils.Constaints.Companion.CHANNEL_ID
import com.example.comics.utils.Status
import com.example.comics.viewmodels.ComicsViewModel
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class ComicsActivity : AppCompatActivity() ,View.OnClickListener  {
    private val comicsViewModel: ComicsViewModel by viewModels()
    private lateinit var imageview: ImageView
    private lateinit var title: TextView
    private lateinit var desc: TextView
    private lateinit var desc2: TextView
    private lateinit var date: TextView
    private lateinit var share: TextView
    private lateinit var next: Button
    private lateinit var previous: Button
    private lateinit var textInputLayout: TextInputLayout
    private lateinit var autoCompleteTextView : AutoCompleteTextView
    private lateinit var imageLink:String
    private var counter:Int=0
    private var noLimit:Boolean=true
    private val notificationId=101
    private var max_comics_number=2469
    private lateinit var job: Job
    private  val handler = CoroutineExceptionHandler { _, exception ->
        println("Exception thrown in one of the children: $exception")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.comics_layout)
        imageview=findViewById(R.id.image)
        title=findViewById(R.id.title)
        desc=findViewById(R.id.description)
        desc2=findViewById(R.id.description2)
        date=findViewById(R.id.date)
        share=findViewById(R.id.share)
        autoCompleteTextView=findViewById(R.id.input_search)
        next=findViewById(R.id.next)
        previous=findViewById(R.id.previous)
        textInputLayout=findViewById(R.id.textInputLayout)
        next.setOnClickListener(this)
        previous.setOnClickListener(this)
        share.setOnClickListener(this)

        initData()
        updateUI()
        autoCompleteAttiude()
        
    }

    private fun initData() {
        comicsViewModel.getComics(counter)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.next ->{ nextBtnAttitude() }
            R.id.previous ->{ previousBtnAttitude() }
            R.id.share -> { shareData() }
        }
    }

    private fun nextBtnAttitude(){
        if(noLimit){
            if( counter<max_comics_number){
                ++counter
                comicsViewModel.getComics(counter)
                autoCompleteTextView.setText("")
                noLimit=false
                previous.setEnabled(true);
            }else{
                next.setEnabled(false)
            }
        }
    }

    private fun previousBtnAttitude(){
        if(noLimit){
            if( counter >0) {
                --counter
                comicsViewModel.getComics(counter)
                autoCompleteTextView.setText("")
                noLimit=false
                next.setEnabled(true);
            }else{
                previous.setEnabled(false)
            }
        }
    }

    private fun hideSoftKeyboard(v: View) {
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.applicationWindowToken, 0)
    }

    private fun autoCompleteAttiude(){
        autoCompleteTextView.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE
                || actionId == EditorInfo.IME_ACTION_SEARCH
                || event.action == KeyEvent.ACTION_DOWN
                || event.action == KeyEvent.KEYCODE_ENTER) {

                val typing:String=textView.text.trim().toString()
                if(!typing.isEmpty()){
                    counter=typing.toInt()
                    comicsViewModel.getComics(counter)
                }

            }
            hideSoftKeyboard(textView)
            false
        }
    }

    private fun updateUI(){
        job = CoroutineScope(Dispatchers.Main).launch(handler) {

            comicsViewModel.posts.collect {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { comics ->
                            imageLink=comics[0].img
                            Glide.with(this@ComicsActivity).load(imageLink).into(imageview)
                            val num=comics[0].num
                            val titl=comics[0].title
                            val descripe=comics[0].transcript
                            var descripe2=comics[0].alt
                            title.setText(titl)
                            desc.setText(descripe)
                            desc2.setText(descripe2)
                            date.setText(comics[0].day+"/"+comics[0].month+"/"+comics[0].year)

                            if(num > max_comics_number){
                                max_comics_number=num
                                sendNotification(titl,descripe2)
                            }

                            noLimit=true
                        }
                    }

                    Status.LOADING -> {


                    }
                    Status.ERROR -> {
                        //Handle Error
                    }
                }
            }

        }

        job.invokeOnCompletion { throwable ->
            if (throwable != null) {
                println("job  failed: ${throwable}")

            } else {
                println("job  SUCCESS")
            }
        }
    }

    private fun shareData(){
        val share=Intent.createChooser(Intent().apply {
            action=Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT,imageLink)
            putExtra(Intent.EXTRA_TITLE,"Sharing the Comics")
            type="text/*"
        },null)
        startActivity(share)
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name="New comic is published"
            val descriptionText="new Comics"
            val importance:Int= NotificationManager.IMPORTANCE_DEFAULT
            val channel: NotificationChannel = NotificationChannel(CHANNEL_ID,name,importance).apply {
                description=descriptionText
            }
            val notificationManager:NotificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(title:String,message:String){

        createNotificationChannel()

        val intent :Intent=Intent(this,ComicsActivity::class.java).apply {
            flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setNumber(1);

        with(NotificationManagerCompat.from(this)){
            notify(notificationId,builder.build())
        }
    }


}
