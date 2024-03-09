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

    @Override
    @Async("taskExecutor")
    public void sendMailToUser(Account account, String password) throws MessagingException {
        StringBuilder sb = new StringBuilder();
    }

    @Override
    public void sendMailNotifyOnBoard(Collection<String> email, String subject, InterviewSchedule interviewSchedule) throws MessagingException {
        String address = interviewSchedule.isLocation() ?
                "<a href=\"" + interviewSchedule.getMeeting() + "\"> Microsoft Teams: Click here to join the meeting </a>"
                : interviewSchedule.getMeeting();
        for (String e : email) {
            StringBuilder sb = new StringBuilder();
            sb.append("<div>")
                    .append(" <h1 style=\"color: blue; text-align: center;\">THƯ MỜI THAM GIA LÀM VIỆC TẠI IMS</h1>")
                    .append("<p>Thân gửi bạn,</p>")
                    .append("<p>Mình là Trang – Cán bộ Nhân Sự tại IMS. Trân trọng cảm ơn bạn đã quan tâm đến cơ hội nghề nghiệp tại</p>")
                    .append("<p>IMS và dành thời gian tìm hiểu - tham gia quy trình tuyển dụng của công ty.</p>")
                    .append("<p>Chúng tôi xin trân trọng thông báo và chúc bạn đã trúng tuyển tại IMS.</strong>")
                    .append("</br>")
                    .append("<p>Dưới đây là các thông tin quan trọng để bắt đầu trước khi làm việc tại IMS. Bạn vui lòng đọc kỹ email và</p>")
                    .append("<p>theo dõi các thông báo từ công ty. Nếu bạn “ĐỒNG Ý” với Thư mời làm việc này, vui lòng phản hồi qua email để</p>")
                    .append("<p>Phòng Nhân sự hỗ trợ thủ tục trước 17h ngày ")
                    .append(interviewSchedule.getSchedule().getDayOfMonth() + "/" + interviewSchedule.getSchedule().getMonthValue() + 1 + "/" + interviewSchedule.getSchedule().getYear() + "</p>")
                    .append("<table>")
                    .append("<tr>")
                    .append("<td style=\"width: 30%; border-bottom: 1px dotted #333;\"><strong>Ngày bắt đầu</strong></td>")
                    .append("<td style=\"border-bottom: 1px dotted #333;\">")
                    .append(interviewSchedule.getSchedule().plusDays(7).getDayOfMonth() + " tháng " + interviewSchedule.getSchedule().plusDays(7).getMonthValue() + " năm " + interviewSchedule.getSchedule().plusDays(7).getYear())
                    .append("</td>")
                    .append("</tr>")
                    .append("<tr>")
                    .append("<td style=\"width: 30%; border-bottom: 1px dotted #333;\"><strong>Địa điểm làm việc</strong></td>")
                    .append("<td style=\"border-bottom: 1px dotted #333;\">F-Ville 3, Khu CNC Hòa Lạc, Thạch Thất, Hà Nội</td>")
                    .append("</tr>")
                    .append("<tr>")
                    .append("<td style=\"width: 30%; border-bottom: 1px dotted #333;\"><strong>Thời gian làm việc</strong></td>")
                    .append("<td style=\"border-bottom: 1px dotted #333;\">Từ 8h30 – 17h30 và thứ 2 – thứ 6 hàng tuần.</td>")
                    .append("</tr>")
                    .append("<tr>")
                    .append("<td style=\"width: 30%; border-bottom: 1px dotted #333;\"><strong>Contact đón</strong></td>")
                    .append("<td style=\"border-bottom: 1px dotted #333;\">Nguyễn Văn A – 0903460087 – nva@gmail.com (Liên hệ trước 2 ngày làm việc)</td>")
                    .append("</tr>")
                    .append("<tr>")
                    .append("<td style=\"width: 30%; border-bottom: 1px dotted #333;\"><strong>Công việc</strong></td>")
                    .append("<td style=\"border-bottom: 1px dotted #333;\">Theo hướng dẫn từ quản lý trực tiếp tại đơn vị.</td>")
                    .append("</tr>")
                    .append("<tr>")
                    .append("<td style=\"width: 30%; border-bottom: 1px dotted #333;\"><strong>Thủ tục onboard</strong></td>")
                    .append("<td style=\"border-bottom: 1px dotted #333;\">Email về thủ tục onboard sẽ được gửi trước 3-5 ngày bắt đầu làm việc.</td>")
                    .append("</tr>")
                    .append("<tr>")
                    .append("<td style=\"width: 30%;\"><strong>Hỗ trợ nhân sự</strong></td>")
                    .append("<td>Nguyễn Thu Thảo – ntthao@gmail.com - Cán bộ Quản lý nhân sự</td>")
                    .append("</tr>")
                    .append("<tr><td colspan=\"2\" style=\"border-bottom: 1px dotted #333;\" ><br></td></tr>")
                    .append("</table>")
                    .append("<p>Chúng tôi tin rằng bạn sẽ có môi trường học tập và phát triển kỹ năng tuyệt vời tại IMS.</p>")
                    .append("<p>Chúc bạn tiếp tục nuôi dưỡng đam mê và gặt hái nhiều thành công trong thời gian tới!</p>")
                    .append("</div>");
            EmailDetail emailDetail = EmailDetail.builder()
                    .recipient(e)
                    .subject(subject)
                    .msgBody(sb.toString())
                    .build();
            sendMailHtml(emailDetail);
        }
    }
}
