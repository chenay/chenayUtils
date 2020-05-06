package com.chenay.platform.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.io.ObjectStreamException;
import java.util.HashMap;

/**
 * @author Y.Chen5
 */
public class AlertMediaUtil {
    private static final int KEY_SOUND_A1;
    private static final int KEY_SOUND_A2;

    static {
        KEY_SOUND_A1 = 1;
        KEY_SOUND_A2 = 2;
    }


    public static final int TYPE_SUCCESS = 100;
    public static final int TYPE_FAILE = 101;

    private static SoundPool mSoundPool;
    private static HashMap<Integer, Integer> soundPoolMap;

    public  void initAll(Context context) {
        mSoundPool = new SoundPool(1, AudioManager.STREAM_ALARM, 0);

        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//                alert(" " + sampleId);
            }
        });
        soundPoolMap = new HashMap<Integer, Integer>();

        soundPoolMap.put(KEY_SOUND_A2, mSoundPool.load(context, R.raw.failed, 1));
        soundPoolMap.put(KEY_SOUND_A1, mSoundPool.load(context, R.raw.succ1, 1));
    }

    public void playSuss() {
        final Integer soundID = soundPoolMap.get(KEY_SOUND_A1);
        mSoundPool.play(soundID, 1, 1, 0, 0, 1);
    }

    public void playFailed() {
        final Integer soundID = soundPoolMap.get(KEY_SOUND_A2);
        mSoundPool.play(soundID, 1, 1, 0, 0, 1);
    }

    /**
     * 获取单例内部类句柄
     *
     * @return Created by Chenay
     */
    private static class AlertMediaHolder {
        private static final AlertMediaUtil SINGLETON = new AlertMediaUtil();
    }

    /**
     * 获取单例
     *
     * @return Created by Chenay
     */
    public static AlertMediaUtil newInstance() {
        return AlertMediaHolder.SINGLETON;
    }

    /**
     * 防止反序列化操作破坏单例模式 (条件) implements Serializable
     *
     * @return Created by Chenay
     */
    private Object readResolve() throws ObjectStreamException {
        return AlertMediaHolder.SINGLETON;
    }
}
