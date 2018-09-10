package org.ruoxue.backend.feature;

public class PermissionException extends Exception {
    public PermissionException(Integer modulePermission, Integer userPermission) {
        super("Invalid permission with module permission="+modulePermission+", and user permission=" + userPermission);
    }
}
