package pl.marek1and.myworktime.dc;

public class DigitalClockUpdateTask extends Thread {

    private DigitalClockUpdater updater;
    private boolean canceled = false;

    public DigitalClockUpdateTask(DigitalClockUpdater updater) {
        this.updater = updater;
    }

    @Override
    public void run() {
        while(!canceled) {
            try {
                updater.updateDigitalClock();
                Thread.sleep(1000);
            } catch (InterruptedException ex) {

            }
        }
    }

    public void cancelTask() {
        canceled = true;
    }

}
