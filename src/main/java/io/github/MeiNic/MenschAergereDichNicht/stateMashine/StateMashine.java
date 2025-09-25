package io.github.MeiNic.MenschAergereDichNicht.stateMashine;

import io.github.MeiNic.MenschAergereDichNicht.logger.Logger;
import io.github.MeiNic.MenschAergereDichNicht.logger.LoggerFactory;
import java.util.EnumMap;
import java.util.Map;

public class StateMashine {
    private State currentState;
    private Map<State, Map<Event, State>> transitions;
    private static final Logger LOGGER = LoggerFactory.getLoggerInstance();

    public StateMashine() {
        this.currentState = State.WAITING_TO_ROLL_DICE;
        this.transitions = new EnumMap<>(State.class);
        initializeTransitions();
    }

    private void initializeTransitions() {
        transitions.put(State.WAITING_TO_ROLL_DICE, new EnumMap<>(Event.class));
        transitions
                .get(State.WAITING_TO_ROLL_DICE)
                .put(Event.ROLL_DICE_CORRECT, State.MOVING_PIECE);
        transitions
                .get(State.WAITING_TO_ROLL_DICE)
                .put(Event.ROLL_DICE_INCORRECT, State.NO_MOVES_AVAILABLE);

        transitions.put(State.NO_MOVES_AVAILABLE, new EnumMap<>(Event.class));
        transitions
                .get(State.NO_MOVES_AVAILABLE)
                .put(Event.TURN_COMPLETED_ENTER_PLAYER, State.WAITING_TO_ROLL_DICE);
        transitions
                .get(State.NO_MOVES_AVAILABLE)
                .put(Event.TURN_COMPLETED_ENTER_BOT, State.WAITING_TO_ROLL_DICE);

        transitions.put(State.MOVING_PIECE, new EnumMap<>(Event.class));
        transitions.get(State.MOVING_PIECE).put(Event.MOVED_PIECE, State.WAITING_TO_ROLL_DICE);
        transitions.get(State.MOVING_PIECE).put(Event.MOVED_WRONG_PIECE, State.MOVING_PIECE);
        transitions
                .get(State.MOVING_PIECE)
                .put(Event.TURN_COMPLETED_ENTER_PLAYER, State.WAITING_TO_ROLL_DICE);
        transitions.get(State.MOVING_PIECE).put(Event.TURN_COMPLETED_ENTER_BOT, State.BOTS_TURN);

        transitions.put(State.MOVED_WRONG_PIECE, new EnumMap<>(Event.class));
        transitions.get(State.MOVED_WRONG_PIECE).put(Event.MOVED_PIECE, State.WAITING_TO_ROLL_DICE);
        transitions
                .get(State.MOVED_WRONG_PIECE)
                .put(Event.MOVED_WRONG_PIECE, State.MOVED_WRONG_PIECE);

        transitions.put(State.BOTS_TURN, new EnumMap<>(Event.class));
        transitions
                .get(State.BOTS_TURN)
                .put(Event.TURN_COMPLETED_ENTER_PLAYER, State.WAITING_TO_ROLL_DICE);
        transitions.get(State.BOTS_TURN).put(Event.TURN_COMPLETED_ENTER_BOT, State.BOTS_TURN);
    }

    public State getCurrentState() {
        return currentState;
    }

    public void handleEvent(Event event) {
        // Stacktrace-Analyse, um den Aufrufer zu ermitteln
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length > 2) {
            StackTraceElement caller = stackTrace[2];
            LOGGER.debug(
                    "handleEvent aufgerufen von: "
                            + caller.getClassName()
                            + "."
                            + caller.getMethodName()
                            + " (Zeile "
                            + caller.getLineNumber()
                            + ")");
        }
        Map<Event, State> stateTransitions = transitions.get(currentState);
        if (stateTransitions != null && stateTransitions.containsKey(event)) {
            currentState = stateTransitions.get(event);
        } else {
            throw new IllegalStateException("Invalid event " + event + " in state " + currentState);
        }
        LOGGER.debug("Transitioned to state: " + currentState + " on event: " + event);
    }
}
