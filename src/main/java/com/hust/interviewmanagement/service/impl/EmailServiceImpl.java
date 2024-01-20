package com.hust.interviewmanagement.service.impl;

import com.hust.interviewmanagement.entities.*;
import com.hust.interviewmanagement.service.EmailService;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public void sendMailSimple(EmailDetail emailDetail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(emailDetail.getRecipient());
        message.setSubject(emailDetail.getSubject());
        message.setText(emailDetail.getMsgBody());
        javaMailSender.send(message);
    }

    @Override
    public void sendMailHtml(EmailDetail emailDetail) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
        messageHelper.setFrom(sender);
        messageHelper.setTo(emailDetail.getRecipient());
        messageHelper.setSubject(emailDetail.getSubject());
        messageHelper.setText(emailDetail.getMsgBody(), true);
        javaMailSender.send(mimeMessage);
    }

    @Override
    @Async("taskExecutor")
    public void sendMailNotificationInterviewSchedule(Collection<String> email, String subject, InterviewSchedule interviewSchedule) throws MessagingException {
        String address = interviewSchedule.isLocation() ?
                "<a href=\"" + interviewSchedule.getMeeting() + "\"> Microsoft Teams: Click here to join the meeting </a>"
                : interviewSchedule.getMeeting();
        for (String e : email) {
            StringBuilder sb = new StringBuilder();
            sb.append("<div>")
                    .append(" <h1 style=\"color: blue; text-align: center;\">THƯ MỜI PHỎNG VẤN</h1>")
                    .append("<p>Lời đầu tiên, chúng tôi xin cảm ơn bạn đã quan tâm đến cơ hội nghề nghiệp tại IMS.</p>")
                    .append("<p>Sau khi xem xét hồ sơ của bạn, phòng Tuyển dụng trân trọng mời bạn tham dự Vòng Phỏng vấn ")
                    .append(interviewSchedule.getTitle())
                    .append(" của công ty.</p>")
                    .append("Thông tin buổi phỏng vấn như sau:")
                    .append("<ul>")
                    .append(" <li style=\"margin-bottom: 10px;\">Thời gian:  <strong>")
                    .append(interviewSchedule.getSchedule().toString().replace("T", " - "))
                    .append(" </strong> </li>")
                    .append("<li style=\"margin-bottom: 10px;\">Địa điểm: <strong>")
                    .append(interviewSchedule.isLocation() ? "Online - " : "Offline - ")
                    .append(address)
                    .append("</li>")
                    .append("<li style=\"margin-bottom: 10px;\">Người hỗ trợ: <strong> ")
                    .append(interviewSchedule.getRecruiter().getFullName())
                    .append(" - ")
                    .append(interviewSchedule.getRecruiter().getPhoneNumber())
                    .append(" </strong> </li>")
                    .append(" <li style=\"margin-bottom: 10px;\">Nội dung và trọng số điểm:\n" +
                            "                <strong>Knowledge (20%), Comunication (30%), Language(20%), Critical Thinking(30%)…</strong>\n" +
                            "            </li>")
                    .append("</ul>")
                    .append("<p>Bạn vui lòng trả lời xác nhận lại mail này Tham dự/ Không tham dự để chúng tôi sắp xếp.</p>")
                    .append("<p>Tham gia phỏng vấn bạn nên chuẩn bị tinh thần và kiến thức tốt để đạt kết quả tốt nhé!</p>")
                    .append("<p>Chúng tôi mong chờ cơ hội hơp tác và làm việc với bạn trong thời gian tới.</p>")
                    .append("</div>");
            EmailDetail emailDetail = EmailDetail.builder()
                    .recipient(e)
                    .subject(subject)
                    .msgBody(sb.toString())
                    .build();
            sendMailHtml(emailDetail);
        }
    }

    @Override
    public void sendMailCancelInterviewSchedule(Collection<String> email, String subject, InterviewSchedule interviewSchedule) throws MessagingException {
        for (String e : email) {
            StringBuilder sb = new StringBuilder();
            sb.append("<div>")
                    .append(" <h1 style=\"color: blue; text-align: center;\">THƯ HỦY PHỎNG VẤN</h1>")
                    .append(" <p>Lời đầu tiên, chúng tôi xin gửi lời xin lỗi bạn vì sự bất tiện này.</p>")
                    .append("<p>Do có chút thay đổi về kế hoạch chúng tôi xin phép được hủy buổi phỏng vấn.</p>")
                    .append("Thông tin buổi phỏng vấn bị hủy như sau:")
                    .append("<ul>")
                    .append(" <li style=\"margin-bottom: 10px;\">Thời gian:  <strong>")
                    .append(interviewSchedule.getSchedule().toString().replace("T", " - "))
                    .append(" </strong> </li>")
                    .append("<li style=\"margin-bottom: 10px;\">Địa điểm: <strong>")
                    .append(interviewSchedule.isLocation() ? "Online - " : "Offline - ")
                    .append(interviewSchedule.isLocation() ? " <a>" + interviewSchedule.getMeeting() + "</a>" : interviewSchedule.getMeeting())
                    .append("</li>")
                    .append("<li style=\"margin-bottom: 10px;\">Người hỗ trợ: <strong> ")
                    .append(interviewSchedule.getRecruiter().getFullName())
                    .append(" - ")
                    .append(interviewSchedule.getRecruiter().getPhoneNumber())
                    .append(" </strong> </li>")
                    .append(" <li style=\"margin-bottom: 10px;\">Nội dung và trọng số điểm:\n" +
                            "                <strong>Knowledge (20%), Comunication (30%), Language(20%), Critical Thinking(30%)…</strong>\n" +
                            "            </li>")
                    .append("</ul>")
                    .append("<p>Chúng tôi sẽ sớm sắp xếp cho bạn một buổi phỏng vấn vào ngày khác.</p>")
                    .append(" <p>Chúng tôi một lần nữa xin lỗi vì sự bất tiện này.</p>")
                    .append("</div>");
            EmailDetail emailDetail = EmailDetail.builder()
                    .recipient(e)
                    .subject(subject)
                    .msgBody(sb.toString())
                    .build();
            sendMailHtml(emailDetail);
        }
    }

    @Override
    public void sendMailChangeInterviewSchedule(Collection<String> email, String subject, InterviewSchedule interviewSchedule) throws MessagingException {
        String address = interviewSchedule.isLocation() ?
                "<a href=\"" + interviewSchedule.getMeeting() + "\"> Microsoft Teams: Click here to join the meeting </a>"
                : interviewSchedule.getMeeting();
        for (String e : email) {
            StringBuilder sb = new StringBuilder();
            sb.append("<div>")
                    .append(" <h1 style=\"color: blue; text-align: center;\">THƯ THAY ĐỔI LỊCH PHỎNG VẤN</h1>")
                    .append("<p>Lời đầu tiên, chúng tôi xin lỗi bạn vì sự bất tiện này.</p>")
                    .append("<p>Do có chút thay đổi về kế hoạch chúng tôi có chút thay đổi về lịch phỏng vấn")
                    .append("Thông tin buổi phỏng vấn như sau:")
                    .append("<ul>")
                    .append(" <li style=\"margin-bottom: 10px;\">Thời gian:  <strong>")
                    .append(interviewSchedule.getSchedule().toString().replace("T", " - "))
                    .append(" </strong> </li>")
                    .append("<li style=\"margin-bottom: 10px;\">Địa điểm: <strong>")
                    .append(interviewSchedule.isLocation() ? "Online - " : "Offline - ")
                    .append(address)
                    .append("</li>")
                    .append("<li style=\"margin-bottom: 10px;\">Người hỗ trợ: <strong> ")
                    .append(interviewSchedule.getRecruiter().getFullName())
                    .append(" - ")
                    .append(interviewSchedule.getRecruiter().getPhoneNumber())
                    .append(" </strong> </li>")
                    .append(" <li style=\"margin-bottom: 10px;\">Nội dung và trọng số điểm:\n" +
                            "                <strong>Knowledge (20%), Comunication (30%), Language(20%), Critical Thinking(30%)…</strong>\n" +
                            "            </li>")
                    .append("</ul>")
                    .append("<p>Bạn vui lòng trả lời xác nhận lại mail này Tham dự/ Không tham dự để chúng tôi sắp xếp.</p>")
                    .append("<p>Tham gia phỏng vấn bạn nên chuẩn bị tinh thần và kiến thức tốt để đạt kết quả tốt nhé!</p>")
                    .append("<p>Chúng tôi mong chờ cơ hội hơp tác và làm việc với bạn trong thời gian tới.</p>")
                    .append("</div>");
            EmailDetail emailDetail = EmailDetail.builder()
                    .recipient(e)
                    .subject(subject)
                    .msgBody(sb.toString())
                    .build();
            sendMailHtml(emailDetail);
        }
    }


    @Override
    @Async("taskExecutor")
    public void sendMailToUser(Account account, String password) throws MessagingException {
        StringBuilder sb = new StringBuilder();
    }


}
