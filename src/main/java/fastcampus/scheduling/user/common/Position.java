package fastcampus.scheduling.user.common;

import java.util.Objects;

public enum Position {
    //임시
    INTERN,
    STAFF,
    ASSISTANT_MANAGER,
    MANAGER;

    public static Position from(String name) {
        for (Position role : Position.values()) {
            if (Objects.equals(role.name(), name)) return role;
        }

        throw new RuntimeException();
        //throw new Exception500("권한 매칭 오류");
    }
}
