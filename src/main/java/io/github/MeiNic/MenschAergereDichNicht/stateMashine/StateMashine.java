package io.github.MeiNic.MenschAergereDichNicht.stateMashine;

import java.util.EnumMap;
import java.util.Map;

public class StateMashine {
    private State currentState;
    private Map<State, Map<Event, State>> transitions;

    public StateMashine() {
        this.currentState = State.WAITING_TO_ROLL_DICE;
        this.transitions = new EnumMap<>(State.class);
        initializeTransitions();
    }

    private void initializeTransitions() {
        transitions.put(State.WAITING_TO_ROLL_DICE, new EnumMap<>(Event.class));
        transitions.get(State.WAITING_TO_ROLL_DICE).put(Event.ROLL_DICE, State.MOVING_PIECE);

        transitions.put(State.NO_MOVES_AVAILABLE, new EnumMap<>(Event.class));
        transitions
                .get(State.NO_MOVES_AVAILABLE)
                .put(Event.NEXT_PLAYER, State.WAITING_TO_ROLL_DICE);

        transitions.put(State.MOVING_PIECE, new EnumMap<>(Event.class));
        transitions.get(State.MOVING_PIECE).put(Event.MOVED_PIECE, State.WAITING_TO_ROLL_DICE);
        transitions.get(State.MOVING_PIECE).put(Event.MOVED_WRONG_PIECE, State.MOVING_PIECE);

        transitions.put(State.MOVED_WRONG_PIECE, new EnumMap<>(Event.class));
        transitions.get(State.MOVED_WRONG_PIECE).put(Event.MOVED_PIECE, State.WAITING_TO_ROLL_DICE);
        transitions
                .get(State.MOVED_WRONG_PIECE)
                .put(Event.MOVED_WRONG_PIECE, State.MOVED_WRONG_PIECE);
    }

    public State getCurrentState() {
        return currentState;
    }

    public void handleEvent(Event event) {
        Map<Event, State> stateTransitions = transitions.get(currentState);
        if (stateTransitions != null && stateTransitions.containsKey(event)) {
            currentState = stateTransitions.get(event);
        } else {
            throw new IllegalStateException("Invalid event " + event + " in state " + currentState);
        }
    }
}
