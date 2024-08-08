package edu.luc.etl.cs313.android.simplestopwatch.model.state;
/**
 * Represents the alarming state in the stopwatch state machine.
 * In this state, the stopwatch is continuously beeping to indicate the occurrence of an alarm.
 * The state transitions to the stopped state when the button is pressed.
 * Behavior specific to the alarming state includes
 * handling button presses and timer ticks, as well as updating the UI to reflect
 * the current runtime. The alarming state does not transition to another state
 * except when the button is pressed, at which it moves to the stopped state.
 */

import edu.luc.etl.cs313.android.simplestopwatch.R;

public class AlarmingState implements StopwatchState {

    public AlarmingState(final StopwatchSMStateView sm) {
        this.sm = sm;
    }

    private final StopwatchSMStateView sm;

    @Override
    public void onButton() {
        sm.actionStop();
        sm.toStoppedState();
    }

    @Override
    public void onTick() {
        /*
        TODO beep indefinitely
        */
        sm.actionBeep();
        sm.toAlarmingState();
    }

    @Override
    public void updateView() {
        sm.updateUIRuntime();
    }

    @Override
    public int getId() {
        return R.string.ALARMING;
    }
}
