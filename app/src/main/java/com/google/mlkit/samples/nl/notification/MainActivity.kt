package com.google.mlkit.samples.nl.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.postDelayed
import java.lang.reflect.Method

class MainActivity : AppCompatActivity() {

    val CHANNEL_ID = "CHANNEL_ID"
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this

        findViewById<Button>(R.id.btn1).setOnClickListener {
            createNotificationChannel()
            createNormalNoti()
        }

        findViewById<Button>(R.id.btn2).setOnClickListener {
            createNotificationChannel()
            createLargeNormalNoti()
        }

        findViewById<Button>(R.id.btn3).setOnClickListener {
            createNotificationChannel()
            createBigNormalNoti()
        }

        findViewById<Button>(R.id.btn4).setOnClickListener {
            createNotificationChannel()
            createInboxNormalNoti()
        }

        findViewById<Button>(R.id.btn5).setOnClickListener {
            //createHighChannel()
        }

        findViewById<Button>(R.id.btn6).setOnClickListener {
            val id = CHANNEL_ID + (Math.random() * 100).toInt()
            Log.i("id", "xxxxid:" + id)
            createHighChannel(id)
            findViewById<Button>(R.id.btn6).postDelayed({
                createHighNormalNoti(id)
            }, 5000)
        }


        findViewById<Button>(R.id.btn7).setOnClickListener {
            val id = CHANNEL_ID + (Math.random() * 100).toInt()
            Log.i("id", "77777xxxxid:" + id)
            createHighChannel(id)
            findViewById<Button>(R.id.btn6).postDelayed({
                createHighNormalNoti2(id)
            }, 1)
        }

        findViewById<Button>(R.id.btn8).setOnClickListener {
            createNotificationChannel()
            createSelfNoti()
        }

        findViewById<Button>(R.id.btn9).setOnClickListener {
            startActivity(Intent(this, TestActivity::class.java))
        }
    }

    @SuppressLint("RemoteViewLayout")
    private fun createSelfNoti() {

        val notificationLayout = RemoteViews(packageName, R.layout.notification_small)

        var notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // 获取通知栏的Builder
        val builder = NotificationCompat.Builder(context)
        builder.setOnlyAlertOnce(true)
        builder.setOngoing(true)
        // 必须设置这个smallIcon，否则会出现崩溃。
        builder.setSmallIcon(R.drawable.aaa)
        // builder.setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.icon_about_us_icon))
        // 设置跳转的逻辑
        //builder.setContentIntent(pendIntent(context))
        setPriority(builder)
        builder.setCategory(Notification.CATEGORY_MESSAGE)
        builder.setLocalOnly(true)
        // 设置频道Id
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID)
        }
        builder.setCustomContentView(notificationLayout)
        builder.setCustomBigContentView(notificationLayout)
        // 获取通知
        val notification: Notification = builder.notification
        // 展示通知
        val notificationId = CHANNEL_ID.hashCode()
        notificationManager.notify(notificationId, notification)

//        // Get the layouts to use in the custom notification.
//        val notificationLayout = RemoteViews(packageName, R.layout.notification_small)
//        val notificationLayoutExpanded = RemoteViews(packageName, R.layout.notification_large)
//
//// Apply the layouts to the notification.
//        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            builder.setChannelId(CHANNEL_ID)
//        }
//        val customNotification = NotificationCompat.Builder(context, CHANNEL_ID)
//            .setSmallIcon(R.drawable.aaa)
//            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
//            .setCustomContentView(notificationLayout)
//            .setCustomBigContentView(notificationLayoutExpanded)
//            .setCustomContentView(notificationLayout)
//            .setCustomHeadsUpContentView(notificationLayout)
//            .build()
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        notificationManager.notify(666, customNotification)

    }

    private fun setPriority(builder: NotificationCompat.Builder) {
        val priority = 2
        try {
            if (priority != 0) {
                val methodPriporty: Method = builder::class.java.getMethod("setPriority", Int::class.java)
                methodPriporty.invoke(builder, priority)
                val setUsesChronometer: Method = builder::class.java.getMethod("setUsesChronometer", Boolean::class.java)
                setUsesChronometer.invoke(builder, true)
            }

            //ignore exception
        } catch (e: Exception) {
        }
    }

    private fun createHighChannel(id: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channel_name"
            val descriptionText = "descriptionText"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        // 申请权限
        val permissions = arrayOf("android.permission.POST_NOTIFICATIONS")
        ActivityCompat.requestPermissions(this, permissions, 0);
    }

    private fun createHighNormalNoti(id: String) {
        val intent = Intent(this, TestActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notificationBuilder =
            NotificationCompat.Builder(this, id)
                .setSmallIcon(R.drawable.aaa)
                .setContentTitle("HIGH PRIORITY")
                .setContentText("Check this dog puppy video NOW!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_RECOMMENDATION)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define.
            if (ActivityCompat.checkSelfPermission(context, "android.permission.POST_NOTIFICATIONS") != PackageManager.PERMISSION_GRANTED) {
                return
            }
            notify((Math.random() * 100).toInt(), notificationBuilder.build())
        }
    }

    private fun createHighNormalNoti2(id: String) {
//        val intent = Intent(this, TestActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
//
//        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.aaa)
//        val notificationBuilder =
//            NotificationCompat.Builder(this, id)
//                .setSmallIcon(R.drawable.aaa)
//                .setContentTitle("HIGH PRIORITY")
//                .setContentText("Check this dog puppy video NOW!")
//                .setColor(Color.RED)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setContentIntent(pendingIntent)
//                .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(bitmap))
//                .setCategory(NotificationCompat.CATEGORY_RECOMMENDATION)
//
//        with(NotificationManagerCompat.from(this)) {
//            // notificationId is a unique int for each notification that you must define.
//            if (ActivityCompat.checkSelfPermission(context, "android.permission.POST_NOTIFICATIONS") != PackageManager.PERMISSION_GRANTED) {
//                return
//            }
//            notify((Math.random() * 100).toInt(), notificationBuilder.build())
//        }


        val intent = Intent(this, TestActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.aaa)
        var builder = NotificationCompat.Builder(this, id)
            .setSmallIcon(R.drawable.ic_launcher_foreground) //设置小图标
            .setLargeIcon(bitmap)
            .setContentTitle("ContentTitle")
            .setTicker("welcome")
            .setContentText("Much longer text that cannot fit one line...")
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
            .setOngoing(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_RECOMMENDATION)
        // 设置优先级,决定了通知在 Android 7.1 及更低版本上的干扰程度。对于 Android 8.0 及更高版本，请改为设置渠道重要性，

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define.
            if (ActivityCompat.checkSelfPermission(context, "android.permission.POST_NOTIFICATIONS") != PackageManager.PERMISSION_GRANTED) {
                return
            }
            notify(22222, builder.build())
        }
    }

    private fun createInboxNormalNoti() {
        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) //设置小图标
            .setContentTitle("ContentTitle")
            .setContentText("Much longer text that cannot fit one line...")
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.aaa))
            .setStyle(
                NotificationCompat.InboxStyle()
                    .addLine("Re: Planning")
                    .addLine("Delivery on its way")
                    .addLine("Follow-up")
            )

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define.
            if (ActivityCompat.checkSelfPermission(context, "android.permission.POST_NOTIFICATIONS") != PackageManager.PERMISSION_GRANTED) {
                return
            }
            notify(22222, builder.build())
        }
    }

    private fun createBigNormalNoti() {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.aaa)
        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) //设置小图标
            .setContentTitle("ContentTitle")
            .setContentText("Much longer text that cannot fit one line...")
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // 设置优先级
        // 设置优先级,决定了通知在 Android 7.1 及更低版本上的干扰程度。对于 Android 8.0 及更高版本，请改为设置渠道重要性，

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define.
            if (ActivityCompat.checkSelfPermission(context, "android.permission.POST_NOTIFICATIONS") != PackageManager.PERMISSION_GRANTED) {
                return
            }
            notify(22222, builder.build())
        }
    }

    private fun createLargeNormalNoti() {

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) //设置小图标
            .setContentTitle("ContentTitle")
            .setContentText("Much longer text that cannot fit one line...")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Much longer text that cannot fit one line...Much longer text that cannot fit one line...Much longer text that cannot fit one line...Much longer text that cannot fit one line...Much longer text that cannot fit one line...Much longer text that cannot fit one line...Much longer text that cannot fit one line...Much longer text that cannot fit one line...")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // 设置优先级
        // 设置优先级,决定了通知在 Android 7.1 及更低版本上的干扰程度。对于 Android 8.0 及更高版本，请改为设置渠道重要性，


        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define.
            if (ActivityCompat.checkSelfPermission(context, "android.permission.POST_NOTIFICATIONS") != PackageManager.PERMISSION_GRANTED) {
                return
            }
            notify(22222, builder.build())
        }
    }

    private fun createNormalNoti() {
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.ic_launcher_foreground) //设置小图标
            .setContentTitle("ContentTitle")
            .setContentText("ContentText")
            .setFullScreenIntent(pendingIntent, false)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // 设置优先级
        // 设置优先级,决定了通知在 Android 7.1 及更低版本上的干扰程度。对于 Android 8.0 及更高版本，请改为设置渠道重要性，


        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define.
            if (ActivityCompat.checkSelfPermission(context, "android.permission.POST_NOTIFICATIONS") != PackageManager.PERMISSION_GRANTED) {
                return
            }
            notify(1111, builder.build())
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channel_name"
            val descriptionText = "descriptionText"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}