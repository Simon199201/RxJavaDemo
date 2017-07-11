package com.yishu.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable o = getObservable();

                //两种订阅方式
                //第一种
//                Observer observer = getObserver();
//                o.subscribe(observer);

                //第二种
//                o.subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String o) throws Exception {
//                        Log.d(TAG, "onNext: " + o);
//
//                    }
//                }, new Consumer<Throwable>() {//onError
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//
//                    }
//                }, new Action() {//onComplete
//                    @Override
//                    public void run() throws Exception {
//
//                    }
//                });

                //线程
                Observer observer = getObserver();
                o.subscribeOn(Schedulers.io())//切换到子线程 指定observable线程
                        .observeOn(AndroidSchedulers.mainThread())//切换到主线程 注定observer线程
                        .subscribe(observer);
                //操作符

            }
        });



    }

    public Observable<String> getObservable() {
        //两种定义方式
        //第一种
//        return Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> e) throws Exception {
//                e.onNext("test1");
//                e.onNext("泡妞");
//                e.onNext("test2");
//                e.onComplete();
////                e.onError(new Throwable());
//            }
//        });
        //第二种
//        return Observable.just("唱歌", "泡吧", "撩妹");
        //第三种
//        return Observable.fromArray("唱歌", "泡吧", "撩妹");
        //第四种
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "大保健";
            }
        });
    }

    public Observer<String> getObserver() {
        return new Observer<String>() {
            private Disposable dd;

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
                dd = d;
            }

            @Override
            public void onNext(String value) {
                Log.d(TAG, "onNext: " + value);
                if (value.equals("泡妞")) {
//                    dd.dispose();//断绝关系
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        };
    }
}
