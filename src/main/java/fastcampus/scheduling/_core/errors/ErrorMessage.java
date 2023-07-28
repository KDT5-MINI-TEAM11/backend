package fastcampus.scheduling._core.errors;

public class ErrorMessage {

    private ErrorMessage() {
        throw new IllegalArgumentException("Suppress default constructor");
    }


    public static final String UN_AUTHORIZED = "인증되지 않았습니다.";
    public static final String FORBIDDEN = "권한이 없습니다.";

    public static final String EMPTY_DATA_FOR_USER_SIGNUP = "회원 가입을 위한 데이터가 존재하지 않습니다.";
    public static final String EMPTY_DATA_FOR_USER_UPDATE = "회원 정보 수정을 위한 데이터가 존재하지 않습니다.";
    public static final String EMPTY_DATA_FOR_USER_CHECK_USEREMAIL = "이메일 중복 검사를 위한 데이터가 존재하지 않습니다.";
    public static final String EMPTY_DATA_FOR_USER_CHECK_PhoneNumber = "핸드폰 번호 중복 검사를 위한 데이터가 존재하지 않습니다.";
    public static final String EMPTY_DATA_FOR_SAVE_CONTENT = "본문 내용을 입력해주세요.";
    public static final String EMPTY_DATA_FOR_SAVE_TITLE = "제목을 입력해주세요.";


    public static final String INVALID_PhoneNumber = "잘못된 핸드폰 번호 입니다.";
    public static final String INVALID_USRENAME = "회원이름은 8자에서 16자 사이어야 합니다.";
    public static final String INVALID_PASSWORD = "패스워드는 8자에서 16자 사이어야 합니다.";
    public static final String INVALID_EMAIL = "이메일 형식을 맞춰주세요.";
    public static final String LOGIN_FAILED = "회원 정보가 존재하지 않습니다.";
    public static final String NOT_FOUND_USER_FOR_UPDATE = "회원 정보 수정을 위한 회원 정보가 존재하지 않습니다.";
    public static final String DUPLICATE_USEREMAIL = "이미 가입 된 이메일 입니다.";
    public static final String DUPLICATE_PHONENUMBER = "이미 저장 된 번호 입니다.";
    public static final String NOT_FOUND_USER = "회원 정보가 존재하지 않습니다.";
}
