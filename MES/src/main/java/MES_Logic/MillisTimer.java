package MES_Logic;

public class MillisTimer {
    private static long startTime = 0;
    private static long currentTime = 0;
    private static long nanotime = 0;

    private boolean isRunning = false;

    public MillisTimer() {
        // The timer is not started initially
        this.isRunning = false;
    }

    public long getElapsedTimeMillis() {
        if (isRunning) {
            return System.currentTimeMillis() - this.startTime;
        } else {
            return 0;
        }
    }
    public int getDay() {
        if (isRunning) {
            return (int) ((float)(System.nanoTime() - this.currentTime)/60/1000000000);
        } else {
            return 0;
        }
    }
    public int getSeconds() {
        if (isRunning) {
            return (int) ((float)(System.nanoTime() - this.nanotime)/1000000000);
        } else {
            return 0;
        }
    }

    public void start() {
        if (!isRunning) {
            this.startTime = System.currentTimeMillis();
            this.isRunning = true;
        }
    }
    public void start_nano() {
        if (!isRunning) {
            this.currentTime = System.nanoTime();
            this.isRunning = true;
        }
    }
    public void start_nano_seconds() {
        if (!isRunning) {
            this.nanotime = System.nanoTime();
            this.isRunning = true;
        }
    }
    public void reset() {
        this.nanotime = 0;
        this.isRunning = false;
    }
}


