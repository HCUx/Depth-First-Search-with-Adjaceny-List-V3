package hcu.depthfirstsearchwithadjacenylist;

import java.util.concurrent.TimeUnit;

/**
 * Created by stava on 26.03.2017.
 */

public class Watch {
    private long accessTime,processTime;

    public void startAccessTime() {
            accessTime = System.nanoTime();
        }

    public long getAccessTime(){
        return TimeUnit.NANOSECONDS.toMicros(accessTime);
    }

    public void startProcessTime() {
        processTime = System.nanoTime();
    }

    public long getProcessTime() {
        return TimeUnit.NANOSECONDS.toMicros(processTime);
    }

    public long getTime() {
        return (TimeUnit.NANOSECONDS.toMicros(System.nanoTime() - accessTime));
    }

}
