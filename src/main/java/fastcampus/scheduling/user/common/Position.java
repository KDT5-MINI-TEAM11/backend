package fastcampus.scheduling.user.common;

import java.util.Objects;
import lombok.Getter;

@Getter
public enum Position {

    LEVEL1(15),
    LEVEL2(18),
    LEVEL3(21),
    LEVEL4(24),
    MANAGER(27);

    private final int totalVacation;

    Position(int totalVacation) {
        this.totalVacation = totalVacation;
    }

    public static Position from(String name) {
        for (Position role : Position.values()) {
            if (Objects.equals(role.name(), name)) return role;
        }

        throw new RuntimeException();
        //throw new Exception500("권한 매칭 오류");
    }

}
