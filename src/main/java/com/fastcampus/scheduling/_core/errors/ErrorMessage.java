package com.fastcampus.scheduling._core.errors;

public interface ErrorMessage {

    String UN_AUTHORIZED = "인증되지 않았습니다.";
    String FORBIDDEN = "권한이 없습니다.";

    String FAILED_TO_SEND_EMAIL = "이메일 전송이 실패 하였습니다.";
    String EMPTY_DATA_FOR_USER_SIGNUP = "회원 가입을 위한 데이터가 존재하지 않습니다.";
    String EMPTY_DATA_FOR_USER_UPDATE = "회원 정보 수정을 위한 데이터가 존재하지 않습니다.";
    String EMPTY_DATA_FOR_USER_CHECK_USEREMAIL = "이메일 중복 검사를 위한 데이터가 존재하지 않습니다.";
    String EMPTY_DATA_FOR_USER_CHECK_USEREMAILAUTH = "이메일 인증 검사를 위한 데이터가 존재하지 않습니다.";
    String EMPTY_DATA_FOR_USER_CHECK_PHONENUMBER = "핸드폰 번호 중복 검사를 위한 데이터가 존재하지 않습니다.";
    String EMPTY_DATA_FOR_CHECK_POSITION = "직책을 입력해 주세요.";
    String EMPTY_DATA_FOR_SAVE_CONTENT = "본문 내용을 입력해주세요.";
    String EMPTY_DATA_FOR_SAVE_TITLE = "제목을 입력해주세요.";
    String EMPTY_DATA_FOR_SAVE_USER = "유저 정보가 없습니다.";
    String EMPTY_DATA_FOR_SCHEDULE = "일정이 없습니다.";
    String FAILED_TO_SIGNUP = "회원가입에 실패하였습니다.";
    String INVALID_CHANGE_POSITION = "잘못 된 요청 입니다.";
    String INVALID_POSITION = "직책을 확인해 주세요.";
    String INVALID_PHONENUMBER = "잘못된 핸드폰 번호 입니다.";
    String INVALID_USRENAME = "회원이름은 2자에서 20자 사이어야 합니다.";
    String INVALID_PASSWORD = "패스워드는 8자에서 16자 사이어야 합니다.";
    String INVALID_EMAIL = "이메일 형식을 맞춰주세요.";
    String INVALID_SEND_EMAIL = "인증 이메일이 잘못 되었습니다.";
    String INVALID_SEND_EMAILAUTH = "인증 번호가 잘못 되었습니다.";
    String EXPIRED_SEND_EMAILAUTH = "인증 번호가 만료 되었습니다.";
    String NOT_FOUND_USER_FOR_UPDATE = "회원 정보 수정을 위한 회원 정보가 존재하지 않습니다.";
    String DUPLICATE_USEREMAIL = "이미 가입 된 이메일 입니다.";
    String DUPLICATE_PHONENUMBER = "이미 저장 된 번호 입니다.";

    String MISMATCH_SIGN_IN_INFO = "인증에 실패 했습니다. 사용자 ID 또는 패스워드가 잘못 되었습니다.";
    String INVALID_SIGNIN_REQUEST = "로그인 요청이 잘못 되었습니다. 필수 필드가 누락 되었습니다.";
    String USER_NOT_FOUND = "유저가 존재 하지 않습니다.";

    String TOKEN_NOT_EXISTS = "토큰을 확인 할 수 없습니다.";
    String TOKEN_NOT_VALID = "비정상적인 토큰입니다.";
    String TOKEN_EXPIRED = "토큰이 만료 되었습니다.";
    String INNER_SERVER_ERROR = "서버 내부 오류가 발생 했습니다.";

    String CANNOT_CANCEL_SCHEDULE = "취소할 수 없는 상태의 일정입니다.";
    String OVERLAPPING_SCHEDULE = "이미 신청한 일정입니다.";
}
