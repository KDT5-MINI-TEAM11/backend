package com.fastcampus.scheduling.email.service;

import com.fastcampus.scheduling._core.common.Constants;
import com.fastcampus.scheduling._core.errors.ErrorMessage;
import com.fastcampus.scheduling._core.errors.exception.DuplicateUserEmailException;
import com.fastcampus.scheduling._core.errors.exception.Exception400;
import com.fastcampus.scheduling._core.errors.exception.Exception500;
import com.fastcampus.scheduling.email.dto.EmailRequest;
import com.fastcampus.scheduling.email.dto.EmailRequest.CheckEmailAuthDTO;
import com.fastcampus.scheduling.email.dto.EmailRequest.SendEmailDTO;
import com.fastcampus.scheduling.email.repository.RedisRepository;
import com.fastcampus.scheduling.user.model.User;
import com.fastcampus.scheduling.user.repository.UserRepository;
import java.util.Random;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MailService {
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final RedisRepository emailRepository;

    public boolean sendEmail(SendEmailDTO sendEmailDTO) {
        if(sendEmailDTO == null)
            throw new Exception500(ErrorMessage.INVALID_SEND_EMAILAUTH);

        String authNumber = createCode();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

        try {
            helper.setFrom("ahdzlq12@naver.com");
            helper.setTo(sendEmailDTO.getTo());
            helper.setSubject(Constants.MAIL_SUBJECT);
            helper.setText(authNumber);
            javaMailSender.send(message);
            emailRepository.save(CheckEmailAuthDTO.builder()
                .userEmail(sendEmailDTO.getTo())
                .userEmailAuth(authNumber)
                .build());
        }catch (MailException | MessagingException e){
            System.out.println(e);
            throw new Exception500(ErrorMessage.FAILED_TO_SEND_EMAIL);
        }

        return true;
    }

    @Transactional(readOnly = true)
    public void checkEmail(EmailRequest.CheckEmailDTO checkEmailDTO) {
        if(checkEmailDTO == null) throw new Exception500(ErrorMessage.EMPTY_DATA_FOR_USER_CHECK_USEREMAIL);

        validateEmail(checkEmailDTO);
    }

    @Transactional(readOnly = true)
    public boolean checkEmailAuth(EmailRequest.CheckEmailAuthDTO checkEmailAuthDTO) {
        if(checkEmailAuthDTO == null) throw new Exception500(ErrorMessage.EMPTY_DATA_FOR_USER_CHECK_USEREMAILAUTH);
        Optional<User> userOptional = userRepository.findByUserEmail(checkEmailAuthDTO.getUserEmail());
        String email = checkEmailAuthDTO.getUserEmail();
        String emailAuth = checkEmailAuthDTO.getUserEmailAuth();

        if(this.emailAuth.get(email).equals(emailAuth)){
            this.emailAuth.remove(email);
            return true;
        }

        throw new Exception400(ErrorMessage.INVALID_SEND_EMAILAUTH);
    }

    @Transactional(readOnly = true)
    public void validateEmail(EmailRequest.CheckEmailDTO checkEmailDTO) {
        String email = checkEmailDTO.getUserEmail();
        Optional<User> userOptional = userRepository.findByUserEmail(email);

        userOptional.ifPresent(user -> {throw new DuplicateUserEmailException();});

        if(email.isBlank())
            throw new Exception400(ErrorMessage.EMPTY_DATA_FOR_USER_CHECK_USEREMAIL);
    }

    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0: key.append((char) ((int) random.nextInt(26) + 97)); break;
                case 1: key.append((char) ((int) random.nextInt(26) + 65)); break;
                default: key.append(random.nextInt(9));
            }
        }
        return key.toString();
    }

}
