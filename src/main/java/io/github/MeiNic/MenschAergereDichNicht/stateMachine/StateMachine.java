package io.github.MeiNic.MenschAergereDichNicht.stateMachine;

import io.github.MeiNic.MenschAergereDichNicht.logger.Logger;
import io.github.MeiNic.MenschAergereDichNicht.logger.LoggerFactory;
import java.util.EnumMap;
import java.util.Map;

public class StateMachine {
    private State currentState;
    private Map<State, Map<Event, State>> transitions;
    private static final Logger LOGGER = LoggerFactory.getLoggerInstance();

    public StateMachine() {
        this.currentState = State.WAITING_TO_ROLL_DICE;
        this.transitions = new EnumMap<>(State.class);
        initializeTransitions();
    }

    private void initializeTransitions() {
        transitions.put(State.WAITING_TO_ROLL_DICE, new EnumMap<>(Event.class));
        transitions
                .get(State.WAITING_TO_ROLL_DICE)
                .put(Event.ROLL_DICE_CAN_MOVE, State.MOVING_PIECE);
        transitions
                .get(State.WAITING_TO_ROLL_DICE)
                .put(Event.ROLL_DICE_CANNOT_MOVE, State.NO_MOVES_AVAILABLE);

        transitions.put(State.NO_MOVES_AVAILABLE, new EnumMap<>(Event.class));
        transitions
                .get(State.NO_MOVES_AVAILABLE)
                .put(Event.TURN_COMPLETED_ENTER_PLAYER, State.WAITING_TO_ROLL_DICE);
        transitions
                .get(State.NO_MOVES_AVAILABLE)
                .put(Event.TURN_COMPLETED_ENTER_BOT, State.BOTS_TURN);

        transitions.put(State.MOVING_PIECE, new EnumMap<>(Event.class));
        transitions.get(State.MOVING_PIECE).put(Event.MOVED_PIECE, State.WAITING_TO_ROLL_DICE);
        transitions.get(State.MOVING_PIECE).put(Event.MOVED_WRONG_PIECE, State.MOVED_WRONG_PIECE);
        transitions
                .get(State.MOVING_PIECE)
                .put(Event.TURN_COMPLETED_ENTER_PLAYER, State.WAITING_TO_ROLL_DICE);
        transitions.get(State.MOVING_PIECE).put(Event.TURN_COMPLETED_ENTER_BOT, State.BOTS_TURN);

        transitions.put(State.MOVED_WRONG_PIECE, new EnumMap<>(Event.class));
        transitions.get(State.MOVED_WRONG_PIECE).put(Event.MOVED_PIECE, State.WAITING_TO_ROLL_DICE);
        transitions
                .get(State.MOVED_WRONG_PIECE)
                .put(Event.MOVED_WRONG_PIECE, State.MOVED_WRONG_PIECE);
        transitions
                .get(State.MOVED_WRONG_PIECE)
                .put(Event.TURN_COMPLETED_ENTER_BOT, State.BOTS_TURN);
        transitions
                .get(State.MOVED_WRONG_PIECE)
                .put(Event.TURN_COMPLETED_ENTER_PLAYER, State.WAITING_TO_ROLL_DICE);

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
        Map<Event, State> stateTransitions = transitions.get(currentState);
        if (stateTransitions != null && stateTransitions.containsKey(event)) {
            currentState = stateTransitions.get(event);
        } else {
            throw new IllegalStateException("Invalid event " + event + " in state " + currentState);
        }
        LOGGER.info("Transitioned to state: " + currentState + " on event: " + event);
    }
}
