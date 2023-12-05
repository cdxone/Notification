package com.google.mlkit.samples.nl.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat

class TestActivity : AppCompatActivity(), View.OnClickListener {
    private val CHANNEL_ID: String = "aaaa"
    private var PAUSE_EVENT = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notify_custom)
        findViewById<View>(R.id.btn_send_custom).setOnClickListener(this)
        PAUSE_EVENT = "sssss"
    }

    override fun onClick(v: View) {
        if (v.id == R.id.btn_send_custom) {
            // 创建通知的通道
            createNotifyChannel(this, "hdl")
            // 创建通知
            val notifyMgr = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notify = getNotify(
                this, PAUSE_EVENT, "小虎隊", true,
                50, SystemClock.elapsedRealtime()
            )
            notifyMgr.notify(R.string.app_name, notify)
        } else if (v.id == R.id.btn2) {
            createNotificationChannel()
            createSelfNoti()
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

    private fun createSelfNoti() {

        val context = this

        val notificationLayout = RemoteViews(this.packageName, R.layout.notification_small)

        var notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // 获取通知栏的Builder
        var builder: NotificationCompat.Builder? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = NotificationCompat.Builder(context, CHANNEL_ID)
        } else {
            builder = NotificationCompat.Builder(context)
        }


//        builder.setContentIntent(contentIntent)
        builder.setOnlyAlertOnce(true)
        builder.setOngoing(true)
        // 必须设置这个smallIcon，否则会出现崩溃。
        builder.setSmallIcon(R.drawable.aaa)
        // builder.setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.icon_about_us_icon))
        builder.setCategory(Notification.CATEGORY_MESSAGE)
        builder.setLocalOnly(true)

        builder.setCustomContentView(notificationLayout)
        builder.setCustomBigContentView(notificationLayout)
        // 获取通知
        val notification: Notification = builder.notification
        // 展示通知
        val notificationId = CHANNEL_ID.hashCode()
        notificationManager.notify(notificationId, notification)

    }

    private fun getNotify(
        ctx: Context,
        event: String,
        song: String,
        isPlay: Boolean,
        progress: Int,
        time: Long
    ): Notification {
        val pIntent = Intent(event)
        val nIntent = PendingIntent.getBroadcast(ctx, R.string.app_name, pIntent, PendingIntent.FLAG_MUTABLE)
        // 创建RemoteViews
        val notify_music = RemoteViews(ctx.packageName, R.layout.notify_music)
//        notify_music.setTextViewText(R.id.tv_info, song + "正在播放")
//        notify_music.setProgressBar(R.id.pb_play, 100, progress, false)
//        notify_music.setOnClickPendingIntent(R.id.btn_play, nIntent)
        // 构建PendintIntent
        val intent = Intent(ctx, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(ctx, R.string.app_name, intent, PendingIntent.FLAG_MUTABLE)
        // Notification
        var builder: NotificationCompat.Builder? = null
        builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(ctx, "hdl")
        } else {
            NotificationCompat.Builder(ctx)
        }
        builder.setContentIntent(contentIntent) //                .setContent(notify_music)
            .setCustomContentView(notify_music)
            .setCustomBigContentView(notify_music)
            .setCustomHeadsUpContentView(notify_music)
            .setTicker("xxxxxx")
            .setSmallIcon(R.drawable.aaa)
        return builder.build()
    }

    fun createNotifyChannel(ctx: Context, channelld: String?) {
        //创建一个默认重要性的通知渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 1、创建通知渠道对象，并且设置参数。
            val channel = NotificationChannel(
                channelld, "Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.setSound(null, null) // 设置推送通知之时的铃声。null 表示静音推送
            channel.enableLights(true) //设置在桌面图标右上角展示小红点
            channel.lightColor = Color.RED // 设置小红点的颜色
            channel.setShowBadge(true) //在长按桌面图标时显示该渠道的通知

            // 2、将通知渠道设置到NotificationManager中。
            //从系统服务中获取通知管理器
            val notifyMgr = ctx.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            //创建指定的通知渠道
            notifyMgr.createNotificationChannel(channel)
        }
    }
}