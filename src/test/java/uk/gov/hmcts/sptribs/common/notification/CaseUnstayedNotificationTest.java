package uk.gov.hmcts.sptribs.common.notification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.type.AddressGlobalUK;
import uk.gov.hmcts.sptribs.ciccase.model.CaseData;
import uk.gov.hmcts.sptribs.ciccase.model.CicCase;
import uk.gov.hmcts.sptribs.ciccase.model.ContactPreferenceType;
import uk.gov.hmcts.sptribs.notification.EmailTemplateName;
import uk.gov.hmcts.sptribs.notification.NotificationHelper;
import uk.gov.hmcts.sptribs.notification.NotificationServiceCIC;
import uk.gov.hmcts.sptribs.notification.model.NotificationRequest;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.sptribs.common.CommonConstants.CONTACT_NAME;

@ExtendWith(MockitoExtension.class)
class CaseUnstayedNotificationTest {
    @Mock
    private NotificationServiceCIC notificationService;

    @Mock
    private NotificationHelper notificationHelper;

    @InjectMocks
    private CaseUnstayedNotification caseUnstayedNotification;

    @Captor
    ArgumentCaptor<NotificationRequest> notificationRequestArgumentCaptor;

    @Test
    void shouldNotifySubjectOfApplicationReceivedWithEmail() {
        //Given
        final CaseData data = getMockCaseData();
        data.getCicCase().setContactPreferenceType(ContactPreferenceType.EMAIL);
        data.getCicCase().setEmail("testrepr@outlook.com");

        Map<String, Object> templateVars = new HashMap<>();
        templateVars.put(CONTACT_NAME, data.getCicCase().getFullName());

        //When
        when(notificationHelper.commonTemplateVars(any(CicCase.class), anyString())).thenReturn(new HashMap<>());
        caseUnstayedNotification.sendToSubject(data, "CN1");

        //Then
        verify(notificationService).setNotificationRequest(notificationRequestArgumentCaptor.capture());
        NotificationRequest notificationRequest = notificationRequestArgumentCaptor.getValue();
        assert (notificationRequest.getDestinationAddress().equals(data.getCicCase().getEmail()));
        assert (notificationRequest.getTemplateVars().equals(templateVars));
        assert (notificationRequest.getTemplate().equals(EmailTemplateName.CASE_UNSTAYED_EMAIL));

        verify(notificationService).sendEmail();
    }

    @Test
    void shouldNotifySubjectOfApplicationReceivedWithPost() {
        //Given
        final CaseData data = getMockCaseData();
        data.getCicCase().setContactPreferenceType(ContactPreferenceType.POST);
        data.getCicCase().setAddress(AddressGlobalUK.builder().build());

        Map<String, Object> templateVars = new HashMap<>();
        templateVars.put(CONTACT_NAME, data.getCicCase().getFullName());

        //When
        when(notificationHelper.commonTemplateVars(any(CicCase.class), anyString())).thenReturn(new HashMap<>());
        doNothing().when(notificationHelper).addAddressTemplateVars(any(AddressGlobalUK.class), anyMap());
        caseUnstayedNotification.sendToSubject(data, "CN1");

        //Then
        verify(notificationService).setNotificationRequest(notificationRequestArgumentCaptor.capture());
        NotificationRequest notificationRequest = notificationRequestArgumentCaptor.getValue();
        assert (notificationRequest.getTemplateVars().equals(templateVars));
        assert (notificationRequest.getTemplate().equals(EmailTemplateName.CASE_UNSTAYED_POST));

        verify(notificationService).sendLetter();
    }

    @Test
    void shouldNotifyApplicantOfApplicationReceivedWithEmail() {
        //Given
        final CaseData data = getMockCaseData();
        data.getCicCase().setApplicantFullName("appFullName");
        data.getCicCase().setApplicantContactDetailsPreference(ContactPreferenceType.EMAIL);
        data.getCicCase().setApplicantEmailAddress("testrepr@outlook.com");

        Map<String, Object> templateVars = new HashMap<>();
        templateVars.put(CONTACT_NAME, data.getCicCase().getApplicantFullName());

        //When
        when(notificationHelper.commonTemplateVars(any(CicCase.class), anyString())).thenReturn(new HashMap<>());
        caseUnstayedNotification.sendToApplicant(data, "CN1");

        //Then
        verify(notificationService).setNotificationRequest(notificationRequestArgumentCaptor.capture());
        NotificationRequest notificationRequest = notificationRequestArgumentCaptor.getValue();
        assert (notificationRequest.getDestinationAddress().equals(data.getCicCase().getApplicantEmailAddress()));
        assert (notificationRequest.getTemplateVars().equals(templateVars));
        assert (notificationRequest.getTemplate().equals(EmailTemplateName.CASE_UNSTAYED_EMAIL));

        verify(notificationService).sendEmail();
    }

    @Test
    void shouldNotifyApplicantOfApplicationReceivedWithPost() {
        //Given
        final CaseData data = getMockCaseData();
        data.getCicCase().setApplicantFullName("appFullName");
        data.getCicCase().setApplicantContactDetailsPreference(ContactPreferenceType.POST);
        data.getCicCase().setApplicantAddress(AddressGlobalUK.builder().build());

        Map<String, Object> templateVars = new HashMap<>();
        templateVars.put(CONTACT_NAME, data.getCicCase().getApplicantFullName());

        //When
        when(notificationHelper.commonTemplateVars(any(CicCase.class), anyString())).thenReturn(new HashMap<>());
        doNothing().when(notificationHelper).addAddressTemplateVars(any(AddressGlobalUK.class), anyMap());
        caseUnstayedNotification.sendToApplicant(data, "CN1");

        //Then
        verify(notificationService).setNotificationRequest(notificationRequestArgumentCaptor.capture());
        NotificationRequest notificationRequest = notificationRequestArgumentCaptor.getValue();
        assert (notificationRequest.getTemplateVars().equals(templateVars));
        assert (notificationRequest.getTemplate().equals(EmailTemplateName.CASE_UNSTAYED_POST));

        verify(notificationService).sendLetter();
    }

    @Test
    void shouldNotifyRepresentativeOfApplicationReceivedWithEmail() {
        //Given
        final CaseData data = getMockCaseData();
        data.getCicCase().setRepresentativeFullName("repFullName");
        data.getCicCase().setRepresentativeContactDetailsPreference(ContactPreferenceType.EMAIL);
        data.getCicCase().setRepresentativeEmailAddress("testrepr@outlook.com");

        Map<String, Object> templateVars = new HashMap<>();
        templateVars.put(CONTACT_NAME, data.getCicCase().getRepresentativeFullName());

        //When
        when(notificationHelper.commonTemplateVars(any(CicCase.class), anyString())).thenReturn(new HashMap<>());
        caseUnstayedNotification.sendToRepresentative(data, "CN1");

        //Then
        verify(notificationService).setNotificationRequest(notificationRequestArgumentCaptor.capture());
        NotificationRequest notificationRequest = notificationRequestArgumentCaptor.getValue();
        assert (notificationRequest.getDestinationAddress().equals(data.getCicCase().getRepresentativeEmailAddress()));
        assert (notificationRequest.getTemplateVars().equals(templateVars));
        assert (notificationRequest.getTemplate().equals(EmailTemplateName.CASE_UNSTAYED_EMAIL));

        verify(notificationService).sendEmail();
    }

    @Test
    void shouldNotifyRepresentativeOfApplicationReceivedWithPost() {
        //Given
        final CaseData data = getMockCaseData();
        data.getCicCase().setRepresentativeFullName("repFullName");
        data.getCicCase().setRepresentativeContactDetailsPreference(ContactPreferenceType.POST);
        data.getCicCase().setRepresentativeAddress(AddressGlobalUK.builder().build());

        Map<String, Object> templateVars = new HashMap<>();
        templateVars.put(CONTACT_NAME, data.getCicCase().getRepresentativeFullName());

        //When
        when(notificationHelper.commonTemplateVars(any(CicCase.class), anyString())).thenReturn(new HashMap<>());
        doNothing().when(notificationHelper).addAddressTemplateVars(any(AddressGlobalUK.class), anyMap());
        caseUnstayedNotification.sendToRepresentative(data, "CN1");

        //Then
        verify(notificationService).setNotificationRequest(notificationRequestArgumentCaptor.capture());
        NotificationRequest notificationRequest = notificationRequestArgumentCaptor.getValue();
        assert (notificationRequest.getTemplateVars().equals(templateVars));
        assert (notificationRequest.getTemplate().equals(EmailTemplateName.CASE_UNSTAYED_POST));

        verify(notificationService).sendLetter();
    }

    private CaseData getMockCaseData() {
        CicCase cicCase = CicCase.builder()
            .fullName("fullName").caseNumber("CN1")
            .build();
        CaseData caseData = CaseData.builder().cicCase(cicCase).build();

        return caseData;
    }

}