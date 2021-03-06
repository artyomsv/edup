package lv.company.edup.persistence.account.account_actions;

public enum ActionType {
    REGISTRATION_REQUEST,
    REGISTRATION_CONFIRMATION_PENDING,
    REGISTRATION_CONFIRMED,
    REGISTRATION_FAILED,
    PASSWORD_RESET_REQUEST,
    PASSWORD_RESET_CONFIRMATION_PENDING,
    PASSWORD_RESET_CONFIRMED,
    PASSWORD_RESET_FAILED
}
