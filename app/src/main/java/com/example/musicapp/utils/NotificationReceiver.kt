package com.example.musicapp.utils

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.musicapp.R
import com.example.musicapp.activities.PlayerActivity
import com.example.musicapp.exitApplication
import com.example.musicapp.fragments.NowPlayingFragment
import com.example.musicapp.setSongPosition

class NotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action){
            ApplicationClass.PREVIOUS -> if(PlayerActivity.musicListPA.size > 1) prevNextSong(increment = false, context = context!!)
            ApplicationClass.PLAY -> if(PlayerActivity.isPlaying) pauseMusic() else playMusic()
            ApplicationClass.NEXT -> if(PlayerActivity.musicListPA.size > 1) prevNextSong(increment = true, context = context!!)
            ApplicationClass.EXIT -> {
                exitApplication()
            }
        }
    }
    private fun playMusic(){
        PlayerActivity.isPlaying = true
        PlayerActivity.musicService!!.mediaPlayer!!.start()
        PlayerActivity.musicService!!.showNotification(R.drawable.pause_icon)
        PlayerActivity.binding.playPauseBtnPA.setIconResource(R.drawable.pause_icon)

        try{ NowPlayingFragment.binding.playPauseBtnNP.setIconResource(R.drawable.pause_icon) }catch (_: Exception){}
    }
    private fun pauseMusic(){
        PlayerActivity.isPlaying = false
        PlayerActivity.musicService!!.mediaPlayer!!.pause()
        PlayerActivity.musicService!!.showNotification(R.drawable.play_icon)
        PlayerActivity.binding.playPauseBtnPA.setIconResource(R.drawable.play_icon)

        try{ NowPlayingFragment.binding.playPauseBtnNP.setIconResource(R.drawable.play_icon) }catch (_: Exception){}
    }
    @SuppressLint("CheckResult")
    private fun prevNextSong(increment: Boolean, context: Context){
        setSongPosition(increment = increment)
        PlayerActivity.musicService!!.createMediaPlayer()
        Glide.with(context)
            .load(PlayerActivity.musicListPA[PlayerActivity.songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.headphone).centerCrop())
            .into(PlayerActivity.binding.songImgPA)
        PlayerActivity.binding.songNamePA.text = PlayerActivity.musicListPA[PlayerActivity.songPosition].title
        Glide.with(context)
            .load(PlayerActivity.musicListPA[PlayerActivity.songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.headphone).centerCrop())
            .into(NowPlayingFragment.binding.songImgNP)
        NowPlayingFragment.binding.songNameNP.text = PlayerActivity.musicListPA[PlayerActivity.songPosition].title
        playMusic()
    }
}
