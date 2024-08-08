package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;
import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchModelListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.clock.ClockModel;
import edu.luc.etl.cs313.android.simplestopwatch.model.time.TimeModel;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import java.io.IOException;

/**
 * An implementation of the state machine for the stopwatch.
 *
 * @author laufer
 */
public class DefaultStopwatchStateMachine implements StopwatchStateMachine {

    public DefaultStopwatchStateMachine(final Context context, final TimeModel timeModel, final ClockModel clockModel) {
        this.context = context;
        this.timeModel = timeModel;
        this.clockModel = clockModel;
    }

    private final Context context;

    private final TimeModel timeModel;

    private final ClockModel clockModel;

    /**
     * The internal state of this adapter component. Required for the State pattern.
     */
    private StopwatchState state;

    protected void setState(final StopwatchState state) {
        this.state = state;
        listener.onStateUpdate(state.getId());
    }

    private StopwatchModelListener listener;

    @Override
    public void setModelListener(final StopwatchModelListener listener) {
        this.listener = listener;
    }

    public int getTime() {
        return timeModel.getRuntime();
    }

    // forward event uiUpdateListener methods to the current state
    // these must be synchronized because events can come from the
    // UI thread or the timer thread
    @Override
    public synchronized void onButton() {
        state.onButton();
    }

    @Override
    public synchronized void onTick() {
        state.onTick();
    }

    @Override
    public void updateUIRuntime() {
        listener.onTimeUpdate(timeModel.getRuntime());
    }

    @Override
    public void updateUILaptime() {
    }//listener.onTimeUpdate(timeModel.getLaptime());}

    // known states
    private final StopwatchState STOPPED = new StoppedState(this);
    private final StopwatchState RUNNING = new RunningState(this);
    private final StopwatchState ALARMING = new AlarmingState(this);

    // transitions
    @Override
    public void toRunningState() {
        setState(RUNNING);
    }

    @Override
    public void toStoppedState() {
        ((StoppedState) STOPPED).setLastClick();
        setState(STOPPED);
    }

    @Override
    public void toAlarmingState() {
        setState(ALARMING);
    }

    // actions
    @Override
    public void actionInit() {
        toStoppedState();
        actionReset();
    }

    @Override
    public void actionReset() {
        timeModel.resetRuntime();
        actionUpdateView();
    }

    @Override
    public void actionStart() {
        clockModel.start();
    }

    @Override
    public void actionStop() {
        clockModel.stop();
    }

    @Override
    public void actionBeep() {
        /*
        TODO figure out how to beep
        */

    }

    @Override
    public void actionInc() {
        timeModel.incRuntime();
        actionUpdateView();
    }

    @Override
    public void actionDec() {
        timeModel.decRuntime();
        actionUpdateView();
    }

    @Override
    public void actionUpdateView() {
        state.updateView();
    }

    protected void playDefaultNotification() {
        final Uri defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final MediaPlayer mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(context, defaultRingtoneUri);
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build());
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(MediaPlayer::release);
            mediaPlayer.start();
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }

    }
}
