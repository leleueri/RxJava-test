package io.github.leleueri.rx;

import rx.Observable;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * http://blog.danlew.net/2014/09/22/grokking-rxjava-part-2/
 */
public class TutoPart2 {

    public static void main(String[] args) {
            long start = System.currentTimeMillis();
            query(args[0])
                    .flatMap(urls -> Observable.from(urls))
                    .flatMap(url -> getContentType(url))
                    .filter(title -> title != null)
                    .take(2)
                    .doOnNext(ct -> System.out.println("Additional Action on each onNext : "+ct))
                    .subscribe(ct -> System.out.println(ct));
            long end = System.currentTimeMillis();
            System.out.println("SYNC>>" + (end - start));

            /*start = System.currentTimeMillis();
            query(args[0])
                    .flatMap(urls -> Observable.from(urls))
                    .flatMap(url -> getContentTypeAsync(url))
                    .subscribe(url -> System.out.println(url));
            end = System.currentTimeMillis();
            System.out.println("ASYNC>>" + (end - start));*/
    }

    public static Observable<List<String>> query(String args) {
        return Observable.just(Arrays.asList(args.split(",")));
    }

    public static Observable getContentType(String arg) {
        return Observable.just(arg).map(x -> {
            try {
                final URLConnection urlConnection = new URL(x).openConnection();
                urlConnection.setUseCaches(false);
                return urlConnection.getContentType();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                return null;
            }
        });
    }

    public static Observable getContentTypeAsync(String arg) {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final Future<String> submit = executorService.submit(new MyCallable(arg));
        executorService.shutdown();
        return Observable.from(submit);
    }

    private static class MyCallable implements Callable<String> {
        private String arg;

        public MyCallable(String arg) {
            this.arg = arg;
        }

        @Override
        public String call() throws Exception {
            final URLConnection urlConnection = new URL(arg).openConnection();
            urlConnection.setUseCaches(false);
            return urlConnection.getContentType();
        }
    }
}
