package io.github.MeiNic.MenschAergereDichNicht.stateMachine;

public enum Event {
    ROLL_DICE_CAN_MOVE,
    ROLL_DICE_CANNOT_MOVE,
    NEXT_PLAYER,
    MOVED_PIECE,
    MOVED_WRONG_PIECE,
    TURN_COMPLETED_ENTER_PLAYER,
    TURN_COMPLETED_ENTER_BOT
}
