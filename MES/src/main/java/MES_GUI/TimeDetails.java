package MES_GUI;

public class TimeDetails {

    private int totalTime;

    private int St5s;

    private int St3s;

    private int St6s;

    private int PT5s;


    public TimeDetails(int totalTime, int orderIDD, int PT5s, int St6s, int St5s, int St3s) {

        this.totalTime = totalTime;
        this.St5s = St5s;
        this.St3s = St3s;
        this.St6s = St6s;
        this.PT5s = PT5s;


    }


    public int getTotalTime() {
        return totalTime;
    }

    public int getSt5s() {
        return St5s;
    }

    public int getSt3s() {
        return St3s;
    }

    public int getSt6s() {
        return St6s;
    }

    public int getPT5s() {
        return PT5s;
    }
}


