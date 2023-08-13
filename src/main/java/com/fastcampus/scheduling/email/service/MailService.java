package com.fastcampus.scheduling.email.service;

import com.fastcampus.scheduling._core.common.Constants;
import com.fastcampus.scheduling._core.errors.ErrorMessage;
import com.fastcampus.scheduling._core.errors.exception.DuplicateUserEmailException;
import com.fastcampus.scheduling._core.errors.exception.Exception400;
import com.fastcampus.scheduling._core.errors.exception.Exception500;
import com.fastcampus.scheduling.email.dto.EmailRequest;
import com.fastcampus.scheduling.email.dto.EmailRequest.CheckEmailAuthDTO;
import com.fastcampus.scheduling.email.dto.EmailRequest.SendEmailDTO;
import com.fastcampus.scheduling.user.model.User;
import com.fastcampus.scheduling.user.repository.UserRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailService {
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final TemplateEngine templateEngine;

    private final Map<String, String> authMap = new ConcurrentHashMap<>();
    private String authCode;

    public boolean sendEmail(SendEmailDTO sendEmailDTO) {
        if(sendEmailDTO == null)
            throw new Exception500(ErrorMessage.INVALID_SEND_EMAILAUTH);

        authCode = createCode();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        Context context = new Context();

        try {
            context.setVariable("authNumber", authCode);
            String emailContent = templateEngine.process("Email", context);
            helper.setFrom(Constants.MAIL_FROM);
            helper.setTo(sendEmailDTO.getTo());
            helper.setSubject(Constants.MAIL_SUBJECT);
            helper.setText(emailContent, true);
            javaMailSender.send(message);
        }catch (MailException | MessagingException e){
            System.out.println(e);
            throw new Exception500(ErrorMessage.FAILED_TO_SEND_EMAIL);
        }

        setAuthCode();
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

    public String checkEmailAuth(EmailRequest.CheckEmailAuthDTO checkEmailAuthDTO) {
        if(checkEmailAuthDTO == null)
            throw new Exception400(ErrorMessage.EMPTY_DATA_FOR_USER_CHECK_USEREMAILAUTH);

        if(checkEmailAuthDTO.getUserEmailAuth().isEmpty())
            throw new Exception400(ErrorMessage.INVALID_SEND_EMAILAUTH);

        return checkExpiredNumber(checkEmailAuthDTO);
    }

    public String checkExpiredNumber(CheckEmailAuthDTO checkEmailAuthDTO) {
        String startDateTime = authMap.get(checkEmailAuthDTO.getUserEmailAuth());

        if (startDateTime == null)
            throw new IllegalArgumentException(ErrorMessage.EMPTY_DATA_FOR_USER_CHECK_USEREMAILAUTH);

        LocalDateTime startTime = LocalDateTime.parse(startDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime currentTime = LocalDateTime.now();

        Duration timeDifference = Duration.between(startTime, currentTime);

        if(timeDifference.toMinutes() > Constants.MAIL_AUTH_TIME){
            removeAuthCode();
            throw new Exception400(Constants.EXPIRED_CODE);
        }

        return Constants.CODE_SUCCESS;
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

    private static String getDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return currentDateTime.format(formatter);
    }

    private void setAuthCode() {
        try {
            authMap.put(authCode, getDateTime());
        }catch (UnsupportedOperationException | NullPointerException | IllegalArgumentException e){
            throw new Exception400(ErrorMessage.FAILED_TO_SAVE_CODE);
        }
    }

    private void removeAuthCode(){
        try {
            authMap.remove(authCode);
        }catch (UnsupportedOperationException | NullPointerException e){
            log.info(ErrorMessage.FAILED_TO_REMOVE_CODE);
        }

    }

}
