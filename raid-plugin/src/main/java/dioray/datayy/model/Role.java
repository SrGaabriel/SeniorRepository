package dioray.datayy.model;

public enum Role {

    OWNER,
    MOD,
    MEMBER;

    public static Role getByOrdinal(int ordinal) {
        for (Role role : Role.values()) {
            if (role.ordinal() == ordinal) {
                return role;
            }
        }

        return null;
    }
}