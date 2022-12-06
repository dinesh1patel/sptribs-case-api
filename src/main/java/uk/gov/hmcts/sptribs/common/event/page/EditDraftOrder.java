package uk.gov.hmcts.sptribs.common.event.page;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.sptribs.ciccase.model.CaseData;
import uk.gov.hmcts.sptribs.ciccase.model.CicCase;
import uk.gov.hmcts.sptribs.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.sptribs.common.ccd.PageBuilder;


@Slf4j
@Component
public class EditDraftOrder implements CcdPageConfiguration {



    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder
            .page("createDraftOrder")
            .pageLabel("Create order")
            .label("createDraftOrder", "Draft to be created")
            .complex(CaseData::getCicCase)
            .label("edit", "<hr>" + "\n<h3>Header</h3>" + "\n<h4>First tier tribunal Health lists</h4>\n\n"
                + "<h3>IN THE MATTER OF THE NATIONAL HEALTH SERVICES (PERFORMERS LISTS)(ENGLAND) REGULATIONS 2013</h2>\n\n"
                + "&lt; &lt; CaseNumber &gt; &gt; \n"
                + "\nBETWEEN\n"
                + "\n&lt; &lt; SubjectName &gt; &gt; \n"
                + "\nApplicant\n"
                + "\n<RepresentativeName>"
                + "\nRespondent<hr>"
                + "\n<h3>Main content</h3>\n\n ")
            .optional(CicCase::getDraftOrderCICList)
            .label("footer", "<h2>Footer</h2>\n First-tier Tribunal (Health,Education and Social Care)\n\n"
                + "Date Issued &lt; &lt;  SaveDate &gt; &gt;")
            .done();
    }


}