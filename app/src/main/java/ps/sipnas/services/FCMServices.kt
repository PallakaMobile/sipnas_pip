package ps.sipnas.services

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONException
import org.json.JSONObject
import ps.sipnas.R
import ps.sipnas.ui.MainActivity
import java.util.*

/**
 **********************************************
 * Created by ukie on 11/2/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class FCMServices : FirebaseMessagingService() {

    override fun onNewToken(p0: String?) {
        Log.d("firebase", "Refreshed token: $p0")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        /**
         * Receive from console firebase
         */
//        Logger.d("on Receive FCM")
//        Logger.d(remoteMessage?.data ?: throw NullPointerException())


        if (remoteMessage?.data?.isNotEmpty() ?: throw NullPointerException()) {
            try {
                val jsonObject = JSONObject(remoteMessage.data)
                sendNotification(jsonObject)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    private fun sendNotification(jsonObject: JSONObject) {
        try {
            //get root json
            val messageTitle = jsonObject.getString("title")
            val messageBody = jsonObject.getString("body")
//            val type = jsonObject.getString("type")

//            Logger.d("$messageTitle, $type,$messageBody")

            var notifIntent: Intent? = null
//            Logger.d(type.toLowerCase())
//            when (type.toLowerCase()) {
//                "verifikasi" -> {
//                    notifIntent = Intent(applicationContext, ConfirmCodeActivity::class.java)
//                    notifIntent.putExtra("notifIntent", true)
//                    notifIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

//                    codeListener.retriveCode(messageBody.toString())
//        }
//            }

            notifIntent = Intent(applicationContext, MainActivity::class.java)
            notifIntent.putExtra("notifIntent", true)
            notifIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

//            codeListener.retriveCode(messageBody.toString())

            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notifID = (Date().time / 1000L % Integer.MAX_VALUE).toInt()
            val notificationBuilder: NotificationCompat.Builder

            val inboxStyle = NotificationCompat.InboxStyle()

            @TargetApi(Build.VERSION_CODES.O)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                /* Create or update. */

                val mChannel = NotificationChannel(getString(R.string.app_name),
                        messageTitle,
                        NotificationManager.IMPORTANCE_HIGH)

                mChannel.enableLights(true)
                mChannel.lightColor = Color.GREEN
                mChannel.setShowBadge(true)
                mChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                notificationManager.createNotificationChannel(mChannel)

                val pendingIntent = PendingIntent.getActivity(this, 0, notifIntent, PendingIntent.FLAG_ONE_SHOT)
                inboxStyle.addLine(messageBody)

                notificationBuilder = NotificationCompat.Builder(applicationContext, getString(R.string.app_name))
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_notif, 1)
                        .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                        .setChannelId(getString(R.string.app_name))
                        .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        .setAutoCancel(true)
                        .setTicker(messageTitle)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())

            } else {
                val pendingIntent = PendingIntent.getActivity(applicationContext, 0, notifIntent, PendingIntent.FLAG_ONE_SHOT)
                inboxStyle.addLine(messageBody)

                @Suppress("DEPRECATION")
                notificationBuilder = NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notif)
                        .setTicker(messageTitle)
                        .setAutoCancel(true)
                        .setContentTitle(messageTitle)
                        .setContentIntent(pendingIntent)
                        .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setStyle(inboxStyle)
                        .setWhen(System.currentTimeMillis())
                        .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                        .setContentText(messageBody)

            }
            notificationManager.notify(notifID, notificationBuilder.build())

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
