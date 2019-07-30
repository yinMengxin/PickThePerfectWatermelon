package com.example.picktheperfectwatermelon;


import android.content.Intent;

import android.os.Environment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.picktheperfectwatermelon.view.AudioView;
import com.zlw.main.recorderlib.RecordManager;
import com.zlw.main.recorderlib.recorder.RecordConfig;
import com.zlw.main.recorderlib.recorder.RecordHelper;
import com.zlw.main.recorderlib.recorder.listener.RecordDataListener;
import com.zlw.main.recorderlib.recorder.listener.RecordFftDataListener;
import com.zlw.main.recorderlib.recorder.listener.RecordResultListener;
import com.zlw.main.recorderlib.recorder.listener.RecordSoundSizeListener;
import com.zlw.main.recorderlib.recorder.listener.RecordStateListener;
import com.zlw.main.recorderlib.utils.Logger;

import java.io.File;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "yin" ;

    private Button bt_record;
    private byte butttonFlag;
    private AudioView audioView;

    final RecordManager recordManager = RecordManager.getInstance();
    int waterCon=20,sweetness=40,texture=80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initRecord();//录音初始化
    }

    private void initRecord() {

        recordManager.init(App.getInstance(), BuildConfig.DEBUG);
        recordManager.changeFormat(RecordConfig.RecordFormat.WAV);
        String recordDir = String.format(Locale.getDefault(), "%s/Record/com.yin.main/",
                Environment.getExternalStorageDirectory().getAbsolutePath());
        recordManager.changeRecordDir(recordDir);

        //录音状态监听
        recordManager.setRecordStateListener(new RecordStateListener() {
            @Override
            public void onStateChange(RecordHelper.RecordState state) {
                Logger.i(TAG, "onStateChange %s", state.name());
                switch (state) {
                    case PAUSE://暂停
                        break;
                    case IDLE://空闲
                        break;
                    case RECORDING:
                        //Toast.makeText(MainActivity.this, "recording", Toast.LENGTH_SHORT).show();
                        break;
                    case STOP://停止
                        //Toast.makeText(MainActivity.this, "stop recording ", Toast.LENGTH_SHORT).show();
                        break;
                    case FINISH://结束
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onError(String error) {
                Logger.i(TAG, "onError %s", error);
            }
        }
    );
        //录音结果监听
        recordManager.setRecordResultListener(new RecordResultListener() {
            @Override
            public void onResult(File result) {
                Toast.makeText(MainActivity.this, "录音文件： " + result.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            }
        });
        //音频可视化监听
        recordManager.setRecordFftDataListener(new RecordFftDataListener() {
            @Override
            public void onFftData(byte[] data) {
                audioView.setWaveData(data);
            }
        });

        //声音大小监听
        recordManager.setRecordSoundSizeListener(new RecordSoundSizeListener() {
            @Override
            public void onSoundSize(int soundSize) {

            }
        });
        //音频数据监听
        recordManager.setRecordDataListener(new RecordDataListener() {
            @Override
            public void onData(byte[] data) {
                cal(data);

            }
        });
    }

    private void cal(final byte [] data){
    }
    private void initView() {
        bt_record = (Button) findViewById(R.id.bt_record);
        audioView = (AudioView) findViewById(R.id.audioView);
        bt_record.setOnClickListener(this);
        butttonFlag = 0;//设置按钮初始状态
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_record:
                if (0 == butttonFlag) {
                    Toast.makeText(MainActivity.this, "start recording", Toast.LENGTH_SHORT).show();
                    bt_record.setText("END");
                    recordManager.start();//开始录音
                    butttonFlag = 1;//设置停止
                } else if(1 == butttonFlag) {



                    butttonFlag = 2;
                    recordManager.stop();//停止录音
                    Toast.makeText(MainActivity.this, "stop recording", Toast.LENGTH_SHORT).show();
                    bt_record.setText("Checking The Result");
                }else if(2 == butttonFlag){
                    butttonFlag = 0;
                    bt_record.setText("record");
                    //跳转结果页面
                    Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                    intent.putExtra("waterCon", waterCon);
                    intent.putExtra("sweetness", sweetness);
                    intent.putExtra("texture", texture);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        recordManager.stop();
    }



}
