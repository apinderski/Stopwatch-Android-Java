package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchUIUpdateListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.clock.ClockModel;
import edu.luc.etl.cs313.android.simplestopwatch.model.time.TimeModel;

/**
 * An implementation of the state machine for the stopwatch.
 *
 * @author laufer
 */
public class DefaultStopwatchStateMachine implements StopwatchStateMachine {

	public DefaultStopwatchStateMachine(final TimeModel timeModel, final ClockModel
            clockModel) {
		this.timeModel = timeModel;
		this.clockModel = clockModel;
	}

	private final TimeModel timeModel;

	private final ClockModel clockModel;

    int delay = 0;

	/**
	 * The internal state of this adapter component. Required for the State pattern.
	 */
	private StopwatchState state;

	protected void setState(final StopwatchState state) {
		this.state = state;
		uiUpdateListener.updateState(state.getId());
	}

	private StopwatchUIUpdateListener uiUpdateListener;

	@Override
	public void setUIUpdateListener(final StopwatchUIUpdateListener uiUpdateListener) {
		this.uiUpdateListener = uiUpdateListener;
	}

	// forward event uiUpdateListener methods to the current state
	@Override public void onStartStop() { state.onStartStop(); }
	@Override public void onTick()      { state.onTick(); }

	@Override public void updateUIRuntime() { uiUpdateListener.updateTime(timeModel.getRuntime()); }

	// known states
	private final StopwatchState STOPPED_RESET  = new StoppedResetState(this);
	private final StopwatchState RUNNING        = new RunningState(this);
	private final StopwatchState INCREMENT      = new IncrementState(this);
	private final StopwatchState STOPPED        = new StoppedState(this);

	// transitions
	@Override public void toRunningState()      { setState(RUNNING); }
	@Override public void toStoppedResetState() { setState(STOPPED_RESET); }
	@Override public void toIncrementState()    { setState(INCREMENT); }
	@Override public void toStoppedState()      { setState(STOPPED); }

	// actions
	@Override public void actionInit()       { toStoppedResetState(); actionReset(); }
	@Override public void actionReset()      { timeModel.resetRuntime(); actionUpdateView(); }
	@Override public void actionStart()      { clockModel.start(); actionInc(); }
	@Override public void actionStop()       { clockModel.stop(); }
	@Override public void actionDec()        { timeModel.decRuntime(); actionUpdateView(); }
    @Override public void actionInc()        { timeModel.incRuntime(); delay = 0; actionUpdateView();}
	@Override public void actionUpdateView() { state.updateView(); }
    @Override public void actionRingTheAlarm() {uiUpdateListener.playDefaultNotification();}

    @Override public int getDelay(){
        return delay;
    }

    @Override public void setDelay(int t) {delay = t;}

    @Override public boolean reachMax(){
        return timeModel.isFull();
    }

    @Override public boolean countedDown(){
        return timeModel.isEmpty();
    }
}
