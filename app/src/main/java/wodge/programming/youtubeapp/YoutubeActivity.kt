package wodge.programming.youtubeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

const val  YOUTUBE_VIDEO_ID = "k-LPLazY22I"
const val YOUTUBE_PLAYLIST = "PLOzDu-MXXLliO9fBNZOQTBDddoA3FzZUo"

class YoutubeActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    private val TAG = "YoutubeActivity"
    private val DIALOG_REQUEST_CODE = 1
    private val playerView by lazy { YouTubePlayerView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube)
        val layout = layoutInflater.inflate(R.layout.activity_youtube, null) as ConstraintLayout
        setContentView(layout)

        playerView.layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        layout.addView(playerView)

        playerView.initialize(getString(R.string.GOOGLE_API_KEY), this)
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider?,
        youTubePlayer: YouTubePlayer?,
        wasRestored: Boolean
    ) {
        Log.d(TAG, "onInitializationSuccess: provider is ${provider?.javaClass} ")
        Log.d(TAG, "onInitializationSuccess: youTubePlayer is ${youTubePlayer?.javaClass}")
        Toast.makeText(this, "Initialized YouTube Player successfully", Toast.LENGTH_LONG).show()

        youTubePlayer?.setPlayerStateChangeListener(playerStateChangeListener)
        youTubePlayer?.setPlaybackEventListener(playbackListener)

        if (!wasRestored) {
            youTubePlayer?.loadVideo(YOUTUBE_VIDEO_ID)
        } else {
            youTubePlayer?.play()
        }
    }

    override fun onInitializationFailure(
        provider: YouTubePlayer.Provider?,
        youTubeInitializationResult: YouTubeInitializationResult?
    ) {

        if(youTubeInitializationResult?.isUserRecoverableError == true) {
            youTubeInitializationResult.getErrorDialog(this, DIALOG_REQUEST_CODE).show()
        } else {
            val errorMessage = "There was an error initializing the Youtube player ($youTubeInitializationResult)"
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG)?.show()
        }
    }

    private val playbackListener = object: YouTubePlayer.PlaybackEventListener {
        override fun onSeekTo(p0: Int) {

        }

        override fun onBuffering(p0: Boolean) {

        }

        override fun onPlaying() {
            Toast.makeText(this@YoutubeActivity, "Good, video is playing ok", Toast.LENGTH_LONG).show()
        }

        override fun onStopped() {
            Toast.makeText(this@YoutubeActivity, "Video has stopped", Toast.LENGTH_LONG).show()
        }

        override fun onPaused() {
            Toast.makeText(this@YoutubeActivity, "Good, video has paused", Toast.LENGTH_LONG).show()
        }
    }

    private val playerStateChangeListener = object: YouTubePlayer.PlayerStateChangeListener {
        override fun onAdStarted() {
            Toast.makeText(this@YoutubeActivity, "Click Ad now, make the video creator rich", Toast.LENGTH_LONG).show()
        }

        override fun onLoading() {
        }

        override fun onVideoStarted() {
            Toast.makeText(this@YoutubeActivity, "Video has started", Toast.LENGTH_LONG).show()
        }

        override fun onLoaded(p0: String?) {
        }

        override fun onVideoEnded() {
            Toast.makeText(this@YoutubeActivity, "Congratulations you have completed another video", Toast.LENGTH_LONG).show()
        }

        override fun onError(p0: YouTubePlayer.ErrorReason?) {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult called with response code $resultCode for request $requestCode")

        if(requestCode == DIALOG_REQUEST_CODE) {
            Log.d(TAG, intent?.toString())
            Log.d(TAG, intent?.extras.toString())
            playerView.initialize(getString(R.string.GOOGLE_API_KEY), this)
        }
    }
}