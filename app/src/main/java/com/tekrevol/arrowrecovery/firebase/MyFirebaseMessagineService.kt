package com.tekrevol.arrowrecovery.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.activities.HomeActivity
import com.tekrevol.arrowrecovery.constatnts.AppConstants.KEY_FIREBASE_TOKEN
import com.tekrevol.arrowrecovery.constatnts.AppConstants.KEY_FIREBASE_TOKEN_UPDATED
import com.tekrevol.arrowrecovery.managers.SharedPreferenceManager
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.models.receiving_model.NotificationModel
import kotlin.random.Random

class MyFirebaseMessagineService : FirebaseMessagingService() {

    private val TAG: String = "FIREBASE MESSAGE"

    override fun onNewToken(s: String) {
        super.onNewToken(s)

        SharedPreferenceManager.getInstance(this).putValue(KEY_FIREBASE_TOKEN, s)
        SharedPreferenceManager.getInstance(this).putValue(KEY_FIREBASE_TOKEN_UPDATED, true)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        //      Log.d(TAG, "From: " + remoteMessage.from)
/*

        if (SharedPreferenceManager.getInstance(this).currentUser == null) {
            return
        }

        val java: Class<HomeActivity>
        val user = SharedPreferenceManager.getInstance(this).currentUser

        if ((user.userDetails.isCompleted == 1) && (user.userDetails.isVerified == 1) && (user.userDetails.isApproved == 1)) {
            java = HomeActivity::class.java
        } else {
            return
        }
*/

        /*    if (remoteMessage.data["extra_payload"].isNullOrEmpty()) {
                val intent = Intent(applicationContext, java)
                handleNotification("Arrow Recovery", "NO PAYLOAD", intent)
            } else {
                val notificationModel: NotificationModel = GsonFactory.getSimpleGson().fromJson(remoteMessage.data["extra_payload"], NotificationModel::class.java)
                val intent = Intent(applicationContext, java)
                handleNotification("Arrow Recovery", notificationModel.data.message, intent)
            }*/

    }


    private fun handleNotification(title: String, message: String, intentToOpen: Intent) {
        playNotificationSound()
        handleDataMessage(title, message, intentToOpen)
    }


    private fun playNotificationSound() {
        try {
            val player = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI)
            player.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun handleDataMessage(title: String, message: String, intentToOpen: Intent) {
        val mBundle = Bundle()

        intentToOpen.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        intentToOpen.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        showNotification(applicationContext,
                title, message,
                intentToOpen)
    }


    //launcher class close
    fun showNotification(context: Context, title: String?, message: String?, intent: Intent?) {
        val nextInt = Random.nextInt()
        val sharedPreferenceManager = SharedPreferenceManager.getInstance(context)
        val pendingIntent = PendingIntent.getActivity(context, nextInt, intent, PendingIntent.FLAG_ONE_SHOT)
        val CHANNEL_ID = "arrow_recovery_channel" // The id of the channel.
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = context.getString(R.string.app_name) // The user-visible name of the channel.
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            notificationManager.createNotificationChannel(mChannel)
        }
        notificationManager.notify(Random.nextInt(), notificationBuilder.build())
//        if (sharedPreferenceManager.getInt(context.getString(R.string.notification_req_code)) <= 1000) {
//            sharedPreferenceManager.putValue(context.getString(R.string.notification_req_code),
//                    sharedPreferenceManager.getInt(context.getString(R.string.notification_req_code)) + 1)
//        } else {
//            sharedPreferenceManager.putValue(context.getString(R.string.notification_req_code), 0)
//        }
        Log.d("showNotification", "showNotification: " + nextInt)
    }


}