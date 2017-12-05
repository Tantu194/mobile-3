package com.example.nguyenkim.mediaplayerr;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MediaActivity extends AppCompatActivity {
    private TextView txtTitle ;
    private TextView txtTimeSong;
    private TextView txtTimeTotal;
    private SeekBar skSong;
    private ImageButton btnPree ;
    private ImageButton btnPlayy ;
    private ImageButton btnStopp ;
    private ImageButton btnNextt;

    ArrayList<Song> arraySong;
    int position = 0;
    MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_layout);
        AnhXa();
        addSong();
        khoitao();

        btnPlayy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    btnPlayy.setImageResource(R.drawable.play);
                }
                else
                {
                    mediaPlayer.start();
                    btnPlayy.setImageResource(R.drawable.playerpause);
                }
                setTimeTotal();
                updateTime();

            }
        });
        btnStopp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                btnPlayy.setImageResource(R.drawable.play);
                khoitao();
            }
        });
        btnNextt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position++;
                if (position > arraySong.size() - 1)
                {
                    position = 0;

                }
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                }
                khoitao();
                mediaPlayer.start();
                btnPlayy.setImageResource(R.drawable.playerpause);
                setTimeTotal();
            }
        });
        btnPree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position -- ;
                if (position < 0 )
                {
                    position = arraySong.size() -1;
                }
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                }
                khoitao();
                mediaPlayer.start();
                btnPlayy.setImageResource(R.drawable.playerpause);
                setTimeTotal();
            }
        });
        skSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(skSong.getProgress());
            }
        });
    }

    private void updateTime()
    {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dinhdanggio = new SimpleDateFormat("mm:ss");
                txtTimeSong.setText(dinhdanggio.format(mediaPlayer.getCurrentPosition()));
                skSong.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this,500);

            }
        },100);
    }


    private void AnhXa()
    {
        txtTimeSong     = (TextView) findViewById(R.id.txtTimeSong);
        txtTimeTotal    = (TextView) findViewById(R.id.txtTimeTotal);
        txtTitle        = (TextView) findViewById(R.id.textviewTitle);
        skSong          = (SeekBar) findViewById(R.id.seekBarSong);
        btnNextt        = (ImageButton) findViewById(R.id.btnNext);
        btnPree         = (ImageButton) findViewById(R.id.btnPre);
        btnPlayy        = (ImageButton) findViewById(R.id.btnPlay);
        btnStopp        = (ImageButton) findViewById(R.id.btnStop);
    }
    private  void setTimeTotal()
    {
        SimpleDateFormat dinhdanggio = new SimpleDateFormat("mm:ss");
        txtTimeTotal.setText(dinhdanggio.format(mediaPlayer.getDuration()));
        skSong.setMax(mediaPlayer.getDuration());

    }

    private void addSong(){
        arraySong = new ArrayList<>();
        arraySong.add(new Song("We Don_t Talk Anymore - Charlie Puth",R.raw.wedon));
        arraySong.add(new Song("Fire - BTS",R.raw.fire));
        arraySong.add(new Song("Lạc Trôi - SơnTùng M-TP",R.raw.lac_troi));
        arraySong.add(new Song("Phía Sau Một Cô Gái - Soobin Hoàng Sơn",R.raw.phia_sau_mot_co_gai));
        arraySong.add(new Song("Despacito - Luis Fonsi, Daddy Yankee, Justin Bieber",R.raw.despacito));
        arraySong.add(new Song("Shape Of You - Ed Sheeran",R.raw.shapeofyou));
    }

    private void khoitao(){
        mediaPlayer = MediaPlayer.create(MediaActivity.this ,arraySong.get(position).getFile());
        txtTitle.setText(arraySong.get(position).getTitle());
    }

}
