package com.charlie.wheather;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

import com.charlie.wheather.common.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Observable
                .create(
                        new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> e) throws Exception {
                                SystemClock.sleep(2000);

                                File dbDir = new File(Constants.PATH_DATABASE_DIR);
                                if (!dbDir.exists()) {
                                    dbDir.mkdir();
                                }

                                File dbFile = new File(dbDir, Constants.NAME_DB);
                                if (!dbFile.exists()) {
                                    FileOutputStream fos = new FileOutputStream(dbFile);
                                    InputStream ins = getAssets().open(Constants.NAME_DB);

                                    int len = -1;
                                    byte[] bs = new byte[1024];
                                    while ((len = ins.read(bs)) != -1) {
                                        fos.write(bs, 0, len);
                                    }

                                    fos.close();
                                    ins.close();
                                }

                                if(!e.isDisposed()){
                                    e.onNext("c");
                                }
                            }
                        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<String>() {
                            @Override
                            public void accept(@NonNull String s) throws Exception {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }
                        }
                );
    }
}
