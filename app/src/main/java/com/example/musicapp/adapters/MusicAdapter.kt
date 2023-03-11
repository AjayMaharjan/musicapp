package com.example.musicapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.musicapp.Music
import com.example.musicapp.R
import com.example.musicapp.activities.MainActivity
import com.example.musicapp.activities.PlayerActivity

import com.example.musicapp.databinding.MusicViewBinding
import com.example.musicapp.formatDuration

class MusicAdapter(
    private val context: Context,
    private var musicList: ArrayList<Music>,
    private val playlistDetails: Boolean = false
)
    : RecyclerView.Adapter<MusicAdapter.MyHolder>() {

    class MyHolder(binding: MusicViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.songNameMV
        val album = binding.songAlbumMV
        val image = binding.imageMV
        val duration = binding.songDuration
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(MusicViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.title.text = musicList[position].title
        holder.album.text = musicList[position].album
        holder.duration.text = formatDuration(musicList[position].duration)
        Glide.with(context)
            .load(musicList[position].artUri)
            .apply(RequestOptions().placeholder(R.drawable.headphone).centerCrop())
            .into(holder.image)

        when{
            playlistDetails ->{
                holder.root.setOnClickListener {
                    sendIntent(ref = "PlaylistDetailsAdapter", pos = position)
                }
            }
            else ->{
                holder.root.setOnClickListener {
                    when{
                        MainActivity.search -> sendIntent(ref = "MusicAdapterSearch", pos = position)
                        musicList[position].id == PlayerActivity.nowPlayingId ->
                            sendIntent(ref = "NowPlaying", pos = PlayerActivity.songPosition)
                        else->sendIntent(ref="MusicAdapter", pos = position) } }
            }

        }
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateMusicList(searchList : ArrayList<Music>){
        musicList = ArrayList()
        musicList.addAll(searchList)
        notifyDataSetChanged()
    }
    private fun sendIntent(ref: String, pos: Int){
        val intent = Intent(context, PlayerActivity::class.java)
        intent.putExtra("index", pos)
        intent.putExtra("class", ref)
        ContextCompat.startActivity(context, intent, null)
    }

}