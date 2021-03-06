package com.bluelinelabs.conductor.rxlifecycle;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.OutsideLifecycleException;
import com.trello.rxlifecycle.RxLifecycle;

import rx.Observable;
import rx.functions.Func1;

public class RxControllerLifecycle {

    /**
     * Binds the given source to a Controller lifecycle. This is the Controller version of
     * {@link com.trello.rxlifecycle.RxLifecycle#bindFragment(Observable)}.
     *
     * @param lifecycle the lifecycle sequence of a Controller
     * @return a reusable {@link Observable.Transformer} that unsubscribes the source during the Controller lifecycle
     */
    @NonNull
    @CheckResult
    public static <T> LifecycleTransformer<T> bindController(@NonNull final Observable<ControllerEvent> lifecycle) {
        return RxLifecycle.bind(lifecycle, CONTROLLER_LIFECYCLE);
    }

    private static final Func1<ControllerEvent, ControllerEvent> CONTROLLER_LIFECYCLE =
            new Func1<ControllerEvent, ControllerEvent>() {
                @Override
                public ControllerEvent call(ControllerEvent lastEvent) {
                    switch (lastEvent) {
                        case CREATE:
                            return ControllerEvent.DESTROY;
                        case ATTACH:
                            return ControllerEvent.DETACH;
                        case CREATE_VIEW:
                            return ControllerEvent.DESTROY_VIEW;
                        case DETACH:
                            return ControllerEvent.DESTROY;
                        default:
                            throw new OutsideLifecycleException("Cannot bind to Controller lifecycle when outside of it.");
                    }
                }
            };
}
