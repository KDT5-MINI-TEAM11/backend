package fastcampus.scheduling.email.service;

import fastcampus.scheduling._core.errors.ErrorMessage;
import fastcampus.scheduling._core.errors.exception.Exception400;
import fastcampus.scheduling.user.dto.UserRequest;
//import fastcampus.scheduling.user.dto.UserRequest.SendEmailDTO;
import fastcampus.scheduling.user.dto.UserResponse;
import fastcampus.scheduling.user.model.User;
import fastcampus.scheduling.user.repository.UserRepository;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MailService {
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;

    public static String MAIL_SUBJECT = "인증메일";

//    public UserResponse.AuthEmail sendEmail(SendEmailDTO sendEmailDTO){
//        //인증번호 생성 로직 -> 반환도 해야됨
//        String authNumber = createCode();
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(sendEmailDTO.getTo());
//        message.setSubject(MAIL_SUBJECT);
//        message.setText(authNumber);
//
//        javaMailSender.send(message);
//
//        return UserResponse.AuthEmail.from(authNumber);
//    }
//
//    @Transactional(readOnly = true)
//    public void checkEmail(UserRequest.CheckEmailDTO checkEmailDTO) {
//        //if(checkEmailDTO == null) throw new Exception400(checkEmailDTO.getUserEmail(), ErrorMessage.EMPTY_DATA_FOR_USER_CHECK_USEREMAIL);
//
//        validateEmail(checkEmailDTO);
//
//        Optional<User> userOptional = userRepository.findByUserEmail(checkEmailDTO.getUserEmail());
//    }
//
//    private static void validateEmail(UserRequest.CheckEmailDTO checkEmailDTO) {
//        //String regex = "^[A-Za-z0-9_\\.\\-]+@[A-Za-z0-9\\-]+\\.[A-Za-z0-9\\-]";
//        String regex =  "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
//        String email = checkEmailDTO.getUserEmail();
//
//        if(checkEmailDTO == null || checkEmailDTO.getUserEmail().isBlank())
//            throw new Exception400(email, ErrorMessage.EMPTY_DATA_FOR_USER_CHECK_USEREMAIL);
//        if (!email.matches(regex))
//            throw new Exception400(email, ErrorMessage.INVALID_EMAIL);
//    }
//
//    public String createCode() {
//        Random random = new Random();
//        StringBuffer key = new StringBuffer();
//
//        for (int i = 0; i < 6; i++) {
//            int index = random.nextInt(4);
//
//            switch (index) {
//                case 0: key.append((char) ((int) random.nextInt(26) + 97)); break;
//                case 1: key.append((char) ((int) random.nextInt(26) + 65)); break;
//                default: key.append(random.nextInt(9));
//            }
//        }
//        return key.toString();
//    }

}
