package uk.gov.hmcts.sptribs.common.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.sptribs.caseworker.model.CloseCase;
import uk.gov.hmcts.sptribs.caseworker.model.CloseReason;
import uk.gov.hmcts.sptribs.ciccase.model.CaseData;
import uk.gov.hmcts.sptribs.ciccase.model.CicCase;
import uk.gov.hmcts.sptribs.ciccase.model.NotificationResponse;
import uk.gov.hmcts.sptribs.common.CommonConstants;
import uk.gov.hmcts.sptribs.notification.NotificationHelper;
import uk.gov.hmcts.sptribs.notification.NotificationServiceCIC;
import uk.gov.hmcts.sptribs.notification.PartiesNotification;
import uk.gov.hmcts.sptribs.notification.TemplateName;
import uk.gov.hmcts.sptribs.notification.model.NotificationRequest;

import java.util.Map;

@Component
@Slf4j
public class CaseCloseNotification implements PartiesNotification {

    @Autowired
    private NotificationServiceCIC notificationService;

    @Autowired
    private NotificationHelper notificationHelper;

    @Override
    public void sendToSubject(final CaseData caseData, final String caseNumber) {
        CicCase cicCase = caseData.getCicCase();
        final Map<String, Object> templateVarsSubject = notificationHelper.getSubjectCommonVars(caseNumber, cicCase);
        setCloseCaseReasonDetails(templateVarsSubject, caseData.getCloseCase());
        NotificationResponse notificationResponse = null;
        if (cicCase.getContactPreferenceType().isEmail()) {
            // Send Email
            notificationResponse =  sendEmailNotification(templateVarsSubject,
                cicCase.getEmail(),
                TemplateName.CASE_CLOSED_EMAIL);
            cicCase.setSubjectNotifyList(notificationResponse);
        } else {
            notificationHelper.addAddressTemplateVars(cicCase.getAddress(), templateVarsSubject);
            //SEND POST
            notificationResponse = sendLetterNotification(templateVarsSubject, TemplateName.CASE_CLOSED_LETTER);
            cicCase.setSubjectLetterNotifyList(notificationResponse);
        }
    }

    @Override
    public void sendToRepresentative(final CaseData caseData, final String caseNumber) {
        CicCase cicCase = caseData.getCicCase();
        NotificationResponse notificationResponse = null;
        final Map<String, Object> templateVarsRepresentative  = notificationHelper.getRepresentativeCommonVars(caseNumber, cicCase);
        templateVarsRepresentative.put(CommonConstants.CIC_CASE_REPRESENTATIVE_NAME, cicCase.getRepresentativeFullName());
        setCloseCaseReasonDetails(templateVarsRepresentative, caseData.getCloseCase());

        if (cicCase.getRepresentativeContactDetailsPreference().isEmail()) {
            // Send Email
            notificationResponse = sendEmailNotification(templateVarsRepresentative,
                cicCase.getRepresentativeEmailAddress(), TemplateName.CASE_CLOSED_EMAIL);
        } else {
            notificationHelper.addAddressTemplateVars(cicCase.getRepresentativeAddress(), templateVarsRepresentative);
            notificationResponse = sendLetterNotification(templateVarsRepresentative,
                TemplateName.CASE_CLOSED_LETTER);
        }
        cicCase.setRepNotificationResponse(notificationResponse);
    }

    @Override
    public void sendToRespondent(final CaseData caseData, final String caseNumber) {
        CicCase cicCase = caseData.getCicCase();
        final Map<String, Object> templateVarsRespondent = notificationHelper.getRespondentCommonVars(caseNumber, cicCase);
        templateVarsRespondent.put(CommonConstants.CIC_CASE_RESPONDENT_NAME, caseData.getCicCase().getRespondantName());
        setCloseCaseReasonDetails(templateVarsRespondent, caseData.getCloseCase());

        // Send Email
        NotificationResponse notificationResponse = sendEmailNotification(templateVarsRespondent,
            cicCase.getRespondantEmail(), TemplateName.CASE_CLOSED_EMAIL);
        cicCase.setResNotificationResponse(notificationResponse);
    }

    private NotificationResponse sendEmailNotification(final Map<String, Object> templateVars,
                                                       String toEmail,
                                                       TemplateName emailTemplateName) {
        NotificationRequest request = notificationHelper.buildEmailNotificationRequest(toEmail, templateVars, emailTemplateName);
        notificationService.setNotificationRequest(request);
        return notificationService.sendEmail();
    }

    private NotificationResponse sendLetterNotification(Map<String, Object> templateVarsLetter, TemplateName emailTemplateName) {
        NotificationRequest letterRequest = notificationHelper.buildLetterNotificationRequest(templateVarsLetter, emailTemplateName);
        notificationService.setNotificationRequest(letterRequest);
        return notificationService.sendLetter();
    }

    private void setCloseCaseReasonDetails(Map<String, Object> templateVarsLetter, CloseCase closeCase){
        if(null != closeCase) {
            CloseReason closeReason = closeCase.getCloseCaseReason();
            if(null != closeReason) {
                templateVarsLetter.put(CommonConstants.CIC_CASE_CLOSURE_REASON, closeReason.getLabel());
            } else {
                templateVarsLetter.put(CommonConstants.CIC_CASE_CLOSURE_REASON, "None provided");
            }
            String additionalDetails = closeCase.getAdditionalDetail();
            if(null != additionalDetails) {
                templateVarsLetter.put(CommonConstants.CIC_CASE_CLOSURE_INFORMATION, additionalDetails);
            } else {
                templateVarsLetter.put(CommonConstants.CIC_CASE_CLOSURE_INFORMATION, "None provided");
            }
        } else {
            templateVarsLetter.put(CommonConstants.CIC_CASE_CLOSURE_REASON, "None provided");
            templateVarsLetter.put(CommonConstants.CIC_CASE_CLOSURE_INFORMATION, "None provided");
        }
    }
}
