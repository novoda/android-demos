
package novoda.audio;

import java.net.URL;

import novoda.audio.service.AudioStreamService;
import novoda.audio.service.IAudioStreamService;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class StreamerInterface extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_interface);
        mAppContext = getApplicationContext();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        bindService(new Intent(mAppContext, AudioStreamService.class), mSrvConnection,
                Context.BIND_AUTO_CREATE);

        mbtn_Ifm2 = (Button)findViewById(R.id.btnIFM2);
        mbtn_Ifm2.setOnClickListener(getIFM1clickListener());
    }

    private ServiceConnection mSrvConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName classname, IBinder service) {
            mAudioPlayerSrv = IAudioStreamService.Stub.asInterface(service);
        }

        public void onServiceDisconnected(ComponentName name) {
            mAudioPlayerSrv = null;
        }
    };

    private OnClickListener getIFM1clickListener() {
        return new OnClickListener() {
            int state = 9999999;

            public void onClick(View v) {
                if (mAudioPlayerSrv == null) {
                    return;
                }

                try {
                    state = mAudioPlayerSrv.getState();

                    if (state == AudioStreamService.NOT_PLAYING) {
                        Log.i(TAG, "Media Player state is NOT_PLAYING" );
                        mAudioPlayerSrv.play(IFM2);
                    } else {
                        mAudioPlayerSrv.stop();
                    }

                    if (state == AudioStreamService.PLAYING) {
                        Log.i(TAG, "Media Player state is PLAYING" );
                        mAudioPlayerSrv.stop();
                    }

                } catch (RemoteException e) {
                    Log.e(TAG, "Error connecting to AudioService: ", e);
                }

            }
        };
    }

    public Context mAppContext;

    public static final String TAG = "[StreamerInterface]";

    private static final String IFM2 = "http://radio.intergalacticfm.com/2";
    
    public IAudioStreamService mAudioPlayerSrv = null;

    public URL mTrackUrl;

    private TextView mbtn_Ifm2;


}
