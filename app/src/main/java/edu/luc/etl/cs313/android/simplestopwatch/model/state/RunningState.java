package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;

class RunningState implements StopwatchState {

    public RunningState(final StopwatchSMStateView sm) {
        this.sm = sm;
    }

    private final StopwatchSMStateView sm;

    @Override
    public void onButton() {
        /*
        TODO figure out
        */
        //sm.actionStart();
        sm.actionReset();
        sm.toStoppedState();
    }

    @Override
    public void onTick() {
        //System.out.println(""+sm.getTime());
        if(sm.getTime()<1){
            sm.toAlarmingState();
        }else{
            sm.actionDec();
            sm.toRunningState();
        }
    }

    @Override
    public void updateView() {
        sm.updateUIRuntime();
    }

    @Override
    public int getId() {
        return R.string.RUNNING;
    }
}
