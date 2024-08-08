package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;

class StoppedState implements StopwatchState {

    public StoppedState(final StopwatchSMStateView sm) {
        this.sm = sm;
    }

    private final StopwatchSMStateView sm;
    private long lastClick=0;

    public void setLastClick(){
        lastClick=System.currentTimeMillis();
    }

    @Override
    public void onButton() {
        sm.actionStart();
        sm.actionInc(); // increase the counter
        lastClick=System.currentTimeMillis();
    }

    @Override
    public void onTick() {
        /*
        TODO if 3 seconds since last button click
        */
        if(sm.getTime()==99 || System.currentTimeMillis()-lastClick>=3000){
            sm.actionBeep();
            sm.toRunningState();
        }
    }

    @Override
    public void updateView() {
        sm.updateUIRuntime();
    }

    @Override
    public int getId() {
        return R.string.STOPPED;
    }
}
