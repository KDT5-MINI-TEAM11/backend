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
    public boolean checkEmail(EmailRequest.CheckEmailDTO checkEmailDTO) {
        if(checkEmailDTO == null)
            throw new Exception400(ErrorMessage.EMPTY_DATA_FOR_USER_CHECK_USEREMAIL);

        User duplicatedEmail = userRepository.findByUserEmail(checkEmailDTO.getUserEmail())
            .orElse(null);

        if(duplicatedEmail != null)
            throw new DuplicateUserEmailException();

        return true;
    }

    public boolean checkEmailAuth(EmailRequest.CheckEmailAuthDTO checkEmailAuthDTO) {
        if(checkEmailAuthDTO == null)
            throw new Exception400(ErrorMessage.EMPTY_DATA_FOR_USER_CHECK_USEREMAILAUTH);

        CheckEmailAuthDTO checkEmailAuthDTOPersistance = emailRepository.findById(checkEmailAuthDTO.getUserEmail()).orElseThrow(()
            -> new Exception400(ErrorMessage.EMPTY_DATA_FOR_USER_CHECK_USEREMAILAUTH));

        if(checkEmailAuthDTO.getUserEmailAuth().isEmpty() ||
            !checkEmailAuthDTOPersistance.getUserEmailAuth().equals(checkEmailAuthDTO.getUserEmailAuth()))
            throw new Exception400(ErrorMessage.INVALID_SEND_EMAILAUTH);

        emailRepository.delete(checkEmailAuthDTOPersistance);
        return true;
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
