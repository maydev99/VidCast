package com.bombadu.vidcast

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ext.cast.CastPlayer
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.MediaQueueItem
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext


class MainActivity : AppCompatActivity() {
    private lateinit var mCastContext: CastContext
    private lateinit var mediaRouteMenuItem: MenuItem
    private val videoUrl = "https://www.cinemaworldtheaters.com/trailers/starwars.mp4"
    private lateinit var videoView: VideoView
    private lateinit var castPlayer: CastPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mCastContext =  CastContext.getSharedInstance(this)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        videoView = findViewById(R.id.video_view)

        castPlayer = CastPlayer(CastContext.getSharedInstance(this))
        castPlayer.setSessionAvailabilityListener(object : CastPlayer.SessionAvailabilityListener {
            override fun onCastSessionAvailable() {
                castPlayer.loadItem(buildMediaQueueItem(videoUrl),0)
            }

            override fun onCastSessionUnavailable() {
                Log.e("CAST", "Cast session unavailable")
            }

        })


        val mediaController = MediaController(this)

        videoView.apply {
            setVideoPath(videoUrl)
            setMediaController(mediaController)
            start()
        }

    }

    private fun buildMediaQueueItem(video: String): MediaQueueItem {
        val movieMetadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE)
        movieMetadata.putString(MediaMetadata.KEY_TITLE, "Star Wars")
        val mediaInfo = MediaInfo.Builder(Uri.parse(video).toString())
            .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
            .setContentType(MimeTypes.APPLICATION_MP4)
            .setMetadata(movieMetadata).build()

        return MediaQueueItem.Builder(mediaInfo).build()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(
            applicationContext,
            menu,
            R.id.media_route_menu_item
        )
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.about) {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("VidCast v1.0")
            alert.setMessage("by Michael May\nBombadu 2021")
            alert.setIcon(R.mipmap.ic_launcher)
            alert.show()
        }
        return super.onOptionsItemSelected(item)
    }



}