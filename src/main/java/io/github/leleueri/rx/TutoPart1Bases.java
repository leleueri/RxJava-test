package io.github.leleueri.rx;

import rx.Observable;
import rx.Subscriber;

/**
 * Tutorial ref :
 * http://blog.danlew.net/2014/09/15/grokking-rxjava-part-1/
 */
public class TutoPart1Bases {
    public static void main(String[] args) {

        /*
         * VERBOSE VERSION
         */
        Observable<String> myObservable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> sub) {
                        sub.onNext("Hello, world!");
                        sub.onCompleted();
                    }
                }
        );

        Subscriber<String> mySubscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) { System.out.println(s); }

            @Override
            public void onCompleted() { }

            @Override
            public void onError(Throwable e) { }
        };

        myObservable.subscribe(mySubscriber);
        // Outputs "Hello, world!"





        /*
         * SIMPLER VERSION
         */
        Observable<String> myObservable2 = Observable.just("Hello World!", "Hello World2!!");
        // use lamba for onXXX method, onNext, onComplete and onError
        myObservable2.subscribe((s) -> System.out.println(s));
        // addition of some transformations
        System.out.println("\n\nApply a transformation on the emitted value through 'map' method");
        // Subscriber shouldn't mutate, it should only react
        myObservable2
                .map(x -> ">:(" + x + "):<")
                .map(s -> s.hashCode())
                .map(s -> s.hashCode())
                .map(i -> Integer.toString(i))
                .subscribe((s) -> System.out.println(s));

    }
}
