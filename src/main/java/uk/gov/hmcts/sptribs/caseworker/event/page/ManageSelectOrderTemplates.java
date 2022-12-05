package uk.gov.hmcts.sptribs.caseworker.event.page;

import uk.gov.hmcts.sptribs.caseworker.model.DraftOrderCIC;
import uk.gov.hmcts.sptribs.ciccase.model.CaseData;
import uk.gov.hmcts.sptribs.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.sptribs.common.ccd.PageBuilder;


public class ManageSelectOrderTemplates implements CcdPageConfiguration {


    @Override
    public void addTo(PageBuilder pageBuilder) {

        pageBuilder.page("dueDatesForOrders")
            .label("dueDatesForOrder", "<h1>Select an order\n</h1>")
            .complex(CaseData::getDraftOrderCIC)
            .mandatory(DraftOrderCIC::getOrderTemplate, "")
            .optional(DraftOrderCIC::getMainContentForCIC1Eligibility, "draftOrderTemplate = \"CIC1_Eligibility\"")
            .optional(DraftOrderCIC::getMainContentForCIC2Quantum, "draftOrderTemplate = \"CIC2_Quantum\"")
            .optional(DraftOrderCIC::getMainContentForCIC3Rule27, "draftOrderTemplate = \"CIC3_Rule_27\"")
            .optional(DraftOrderCIC::getMainContentForCIC4BlankDecisionNotice1, "draftOrderTemplate = \"CIC4_Blank_Decision_Notice_1\"")
            .optional(DraftOrderCIC::getMainContentForCIC6GeneralDirections, "draftOrderTemplate = \"CIC6_General_Directions\"")
            .optional(DraftOrderCIC::getMainContentForCIC7MEDmiReports, "draftOrderTemplate = \"CIC7_ME_Dmi_Reports\"")
            .optional(DraftOrderCIC::getMainContentForCIC8MEJointInstruction, "draftOrderTemplate = \"CIC8_ME_Joint_Instruction\"")
            .optional(DraftOrderCIC::getMainContentForCIC10StrikeOutWarning, "draftOrderTemplate = \"CIC10_Strike_Out_Warning\"")
            .optional(DraftOrderCIC::getMainContentForCIC11StrikeOutDecisionNotice,
                "draftOrderTemplate = \"CIC11_Strike_Out_Decision_Notice\"")
            .optional(DraftOrderCIC::getMainContentForCIC12DecisionAnnex, "draftOrderTemplate = \"CIC12_Decision_Annex\"")
            .optional(DraftOrderCIC::getMainContentForCIC13ProFormaSummons, "draftOrderTemplate = \"CIC13_Pro_Forma_Summons\"")
            .done();

    }


}