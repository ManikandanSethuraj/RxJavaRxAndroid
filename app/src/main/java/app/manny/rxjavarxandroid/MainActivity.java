package app.manny.rxjavarxandroid;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


   // vars
    // Disposables are used when Observables are no longer needed.
    private CompositeDisposable disposable;




    /**
     * RxJava Operators are of different types:
     * Create Operaters, Filter Operators, Transform Operators
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     //   CreateOperator();
     //   RangeOperator();
      //  RepeatOperator();
        IntervalOperator();
        TimerOperators();
    }



    // Create Operator : Create()

    private void CreateOperator(){
       final Task task = new Task("Walk the dog", false, 2);

        final List<Task> tasks = DataSource.createTasksList();

        Observable<Task> taskObservable = Observable
                .create(new ObservableOnSubscribe<Task>() {

                    // This Subscribe method will be called once OnScbscribe method is called in below Subscribed Observer..
                    @Override
                    public void subscribe(ObservableEmitter<Task> emitter) throws Exception {


                        // Single Task
//                        if (!emitter.isDisposed()){
//                            Log.d(TAG, "subscribe: ");
//                            emitter.onNext(task);
//                            emitter.onComplete();
//                        }


                        // List of Tasks
                        for (Task taskLoop : tasks){
                            if (!emitter.isDisposed()){
                                emitter.onNext(taskLoop);
                            }
                        }

                        if (!emitter.isDisposed()){
                            emitter.onComplete();
                        }


                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


        taskObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(Task task) {
                Log.d(TAG, "onNext: ");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        });
    }



    // Create Operator: Just()
    private void JustOperator(){

        final Task task = new Task("Walk the dog", false, 2);

        final List<Task> tasks = DataSource.createTasksList();

        Observable<Task> taskObservable = Observable
                .just(task)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


        taskObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(Task task) {
                Log.d(TAG, "onNext: ");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        });

    }


    // Create Operator: Range()
    private void RangeOperator(){

        Observable<Task> observable = Observable
                .range(0,9)
                .subscribeOn(Schedulers.io())
                .map(new Function<Integer, Task>() {
                    @Override
                    public Task apply(Integer integer) throws Exception {
                        Log.d(TAG, "apply: "+ Thread.currentThread().getName());
                        return new Task("Priority of the task::",false,integer);
                    }
                })
                .takeWhile(new Predicate<Task>() {
                    @Override
                    public boolean test(Task task) throws Exception {
                        return task.getPriority() < 5;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe( Disposable d) {
                Log.d(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext( Task taskNew) {
                Log.d(TAG, "onNext: "+taskNew.getPriority());
            }

            @Override
            public void onError( Throwable e) {
                Log.d(TAG, "onError: ");

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        });
    }


    // Create Operator: Repeat()
    private void RepeatOperator(){

        Observable<Integer> observable = Observable
                .range(0,4)
                .subscribeOn(Schedulers.io())
                .repeat(3)
                .observeOn(AndroidSchedulers.mainThread());


        observable.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe( Disposable d) {

            }

            @Override
            public void onNext( Integer integer) {
                Log.d(TAG, "onNext:Repeat:"+integer);
            }

            @Override
            public void onError( Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    // Create Operator: Interval()
    private void IntervalOperator(){


        // emit an observable every time interval
        Observable<Long> observable = Observable
                .interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .takeWhile(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Exception {
                        // stop the process if more than 5 seconds passes
                        return aLong <= 5;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());


        observable.subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe( Disposable d) {

            }

            @Override
            public void onNext( Long integer) {
                Log.d(TAG, "onNext:Repeat:"+integer);
            }

            @Override
            public void onError( Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    // Create Operators: Timer()

    private void TimerOperators(){

        Observable<Long> observable = Observable
                .timer(1,TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new Observer<Long>() {

            long time = 0; // variable for demonstating how much time has passed
            @Override
            public void onSubscribe( Disposable d) {

            }

            @Override
            public void onNext( Long integer) {
                Log.d(TAG, "onNext: " + ((System.currentTimeMillis() / 1000) - time) + " seconds have elapsed." );
            }

            @Override
            public void onError( Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }


    /**
     * You need to return a Task object from a local SQLite database cache. All database operations must be done on a background thread. Then the result is returned to the main thread.
     *
     * 1)You can use a callable to execute the method on a background thread.
     * 2)Then return the results to the main thread.
     */
    
    private void CallableOperator(){
        // create Observable (method will not execute yet)
        // fromCallable is used to
        Observable<List<Task>> callable = Observable
                .fromCallable(new Callable<List<Task>>() {
                    @Override
                    public List<Task> call() throws Exception {

                        // Returning a list of Tasks from Database
                        //  return MyDatabase.getTask();
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

// method will be executed since now something has subscribed
        callable.subscribe(new Observer<List<Task>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Task> task) {
              //  Log.d(TAG, "onNext: : " + task.getDescription());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    private void ObservablesandObservers(){
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
                disposable.add(d);
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

        disposable.add(taskObservable.subscribe(new Consumer<Task>() {
            @Override
            public void accept(Task task) throws Exception {

            }
        }));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }

}