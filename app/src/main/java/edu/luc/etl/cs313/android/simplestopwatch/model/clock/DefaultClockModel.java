package edu.luc.etl.cs313.android.simplestopwatch.model.clock;

import java.util.Timer;
import java.util.TimerTask;

import static edu.luc.etl.cs313.android.simplestopwatch.common.Constants.*; // for testing, remove when done

/**
 * An implementation of the internal clock.
 *
 * @author laufer
 */
public class DefaultClockModel implements ClockModel {

    // TODO make accurate by keeping track of partial seconds when canceled etc.


    private Timer timer;

    private TickListener listener;

    //track the start time of the timer in milliseconds
    private long startTime;

    //track the time in miliseconds when the timer is stopped
    private long elapsedTime;
    public DefaultClockModel() {
        this.elapsedTime = 0;
    }

    @Override
    public void setTickListener(final TickListener listener) {
        this.listener = listener;
    }

    @Override
    public void start() {
        timer = new Timer();
        startTime = System.currentTimeMillis();


        // The clock model runs onTick every 1000 milliseconds
        timer.schedule(new TimerTask() {
            @Override public void run() {
                long currentTime = System.currentTimeMillis();
                elapsedTime += currentTime - startTime; //get elapsed time
                //update start time for next tick
                startTime = currentTime;


                //fire the tick event only if one second has elapsed
                if (elapsedTime >= speed) {
                    //subtract one second
                    elapsedTime -= speed;
                    //notify listener
                    listener.onTick();
                }
            }
                // fire event
                /*listener.onTick();
                elapsedTime += System.currentTimeMillis() - startTime;
            }*/
        }, /*initial delay*/ speed, /*- (elapsedTime % speed),*/ /*periodic delay*/ speed);
    }

    @Override
    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;

            //calculate the total elapsed time by subtracting startTime from elapsedtime
            elapsedTime = System.currentTimeMillis() - startTime;
        }
    }
}