package app.manny.rxjavarxandroid;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Observable<Task> taskObservable = Observable // create a new Observable object
                .fromIterable(DataSource.createTasksList()) // apply 'fromIterable' operator
                .subscribeOn(Schedulers.io()) // designate worker thread (background)
                .filter(new Predicate<Task>() { // this runs in the background thread
                    @Override
                    public boolean test( Task task) throws Exception {
                        Log.d(TAG, "test: ThreadName"+Thread.currentThread().getName());
                        Log.d(TAG, "test:"+task.getDescription());
                       try {
                           Thread.sleep(1000);
                       }catch (Exception e){

                       }

                        return task.isComplete();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()); // designate observer thread (main thread)


        taskObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: Called");
            }
            @Override
            public void onNext(Task task) { // run on main thread

                Log.d(TAG, "onNext: "+Thread.currentThread().getName());
                Log.d(TAG, "onNext: : " + task.getDescription());
            }
            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: Called");
            }
            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: Called");

            }
        });
    }
}