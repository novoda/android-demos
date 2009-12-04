    package novoda.audio.service;

    import java.io.IOException;
    import java.sql.Time;

    import android.app.Service;
    import android.content.Intent;
    import android.media.AudioManager;
    import android.media.MediaPlayer;
    import android.media.MediaPlayer.OnBufferingUpdateListener;
    import android.media.MediaPlayer.OnCompletionListener;
    import android.media.MediaPlayer.OnErrorListener;
    import android.media.MediaPlayer.OnPreparedListener;
    import android.os.IBinder;
    import android.os.RemoteException;
    import android.util.Log;

    public class AudioStreamService extends Service implements OnCompletionListener, OnPreparedListener, OnBufferingUpdateListener, OnErrorListener {

        public AudioStreamService() {
            mAudioSrvState = NOT_PLAYING;
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); 
        }
        
        public IBinder onBind(Intent intent) {
            return mSrvBinding;
        }
        
        public void onPrepared(MediaPlayer mp) {
            mAudioSrvState = PLAYING;
            Time duration = new Time(mp.getDuration());
            Log.v(TAG, "Prepared mediaplayer for track of duration =["+duration+"]");
            mMediaPlayer.start();
        }
        
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            Log.v(TAG, "Buffered additional=["+percent+"%]");
            mAudioSrvState = BUFFERING;
        }
        
        public void onCompletion(MediaPlayer mp) {
            Log.i(TAG, "Completed playback");
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mAudioSrvState = NOT_PLAYING;
        }
        
        public boolean onError(MediaPlayer mp, int what, int extra) {
            switch(what){
                case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                    Log.e(TAG, "unknown media error what=["+what+"] extra=["+extra+"]");
                    break;
                case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                    Log.e(TAG, "Streaming source Server died what=["+what+"] extra=["+extra+"]");
                    break;
                default:
                    Log.e(TAG, "Default Problems what=["+ what +"] extra=["+extra+"]");
            }
            return false;
        }
        
        public void play(String audioTrackURL) {
            Log.i(TAG, "Playing track at URL=["+audioTrackURL+"]");
            mAudioTrackURL = audioTrackURL;

            if (mAudioSrvState == NOT_PLAYING) {
                try {
                    mMediaPlayer.setDataSource(mAudioTrackURL);
                    mMediaPlayer.prepareAsync();
                } catch (IllegalArgumentException e) {
                    Log.e(TAG, "AudioTrackUrl seems to be incorrectly formatted",e);
                } catch (IllegalStateException e) {
                    Log.e(TAG, "MediaPlayer is in an illegal state",e);
                } catch (IOException e) {
                    Log.e(TAG, "MediaPlayer failed due to exception",e);
                }
            }
            
        }
        
        public void stop() {
            Log.i(TAG, "Call to stop streaming audio");

            if (mMediaPlayer != null) {
                if (mMediaPlayer.isPlaying()) {
                    Log.i(TAG, "media player was playing and is now Stopping");
                    mMediaPlayer.stop();
                    mMediaPlayer.reset();
                    mAudioSrvState = NOT_PLAYING;
                }
            }
        }
        
        public String getFileName() {
            return null;
        }

        public long getPosition() {
            return 0;
        }

        public int getState() {
            return mAudioSrvState;
        }

        private final IAudioStreamService.Stub mSrvBinding = new IAudioStreamService.Stub() {

            public void play(String url) throws RemoteException {
                AudioStreamService.this.play(url);
            }

            public String getFileName() {
                return AudioStreamService.this.getFileName();
            }

            public long getCurrPlayingPosition() {
                return AudioStreamService.this.getPosition();
            }

            public int getState() {
                return AudioStreamService.this.getState();
            }

            public void stop() {
                AudioStreamService.this.stop();
            }
        };

        public static final int NOT_PLAYING = 0;

        public static final int PLAYING = 1;

        public static final int BUFFERING = 2;

        public static final String TAG = "[AudioStreamService]";

        private int mAudioSrvState;

        private MediaPlayer mMediaPlayer;

        private String mAudioTrackURL;

    }