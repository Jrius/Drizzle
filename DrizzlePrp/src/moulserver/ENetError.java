/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moulserver;

class ENetError extends Enums.EnumToVal
{
    static final int kNetPending = -1;
    static final int kNetSuccess = 0;
    static final int kNetErrInternalError = 1;
    static final int kNetErrTimeout = 2;
    static final int kNetErrBadServerData = 3;
    static final int kNetErrAgeNotFound = 4;
    static final int kNetErrConnectFailed = 5;
    static final int kNetErrDisconnected = 6;
    static final int kNetErrFileNotFound = 7;
    static final int kNetErrOldBuildId = 8;
    static final int kNetErrRemoteShutdown = 9;
    static final int kNetErrTimeoutOdbc = 10;
    static final int kNetErrAccountAlreadyExists = 11;
    static final int kNetErrPlayerAlreadyExists = 12;
    static final int kNetErrAccountNotFound = 13;
    static final int kNetErrPlayerNotFound = 14;
    static final int kNetErrInvalidParameter = 15;
    static final int kNetErrNameLookupFailed = 16;
    static final int kNetErrLoggedInElsewhere = 17;
    static final int kNetErrVaultNodeNotFound = 18;
    static final int kNetErrMaxPlayersOnAcct = 19;
    static final int kNetErrAuthenticationFailed = 20;
    static final int kNetErrStateObjectNotFound = 21;
    static final int kNetErrLoginDenied = 22;
    static final int kNetErrCircularReference = 23;
    static final int kNetErrAccountNotActivated = 24;
    static final int kNetErrKeyAlreadyUsed = 25;
    static final int kNetErrKeyNotFound = 26;
    static final int kNetErrActivationCodeNotFound = 27;
    static final int kNetErrPlayerNameInvalid = 28;
    static final int kNetErrNotSupported = 29;
    static final int kNetErrServiceForbidden = 30;
    static final int kNetErrAuthTokenTooOld = 31;
    static final int kNetErrMustUseGameTapClient = 32;
    static final int kNetErrTooManyFailedLogins = 33;
    static final int kNetErrGameTapConnectionFailed = 34;
    static final int kNetErrGTTooManyAuthOptions = 35;
    static final int kNetErrGTMissingParameter = 36;
    static final int kNetErrGTServerError = 37;
    static final int kNetErrAccountBanned = 38;
    static final int kNetErrKickedByCCR = 39;
    static final int kNetErrScoreWrongType = 40;
    static final int kNetErrScoreNotEnoughPoints = 41;
    static final int kNetErrScoreAlreadyExists = 42;
    static final int kNetErrScoreNoDataFound = 43;
    static final int kNetErrInviteNoMatchingPlayer = 44;
    static final int kNetErrInviteTooManyHoods = 45;
    static final int kNetErrNeedToPay = 46;
    static final int kNetErrServerBusy = 47;
    static final int kNumNetErrors = 48;
}
