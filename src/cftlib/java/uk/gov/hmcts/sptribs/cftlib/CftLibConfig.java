package uk.gov.hmcts.sptribs.cftlib;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.CCDDefinitionGenerator;
import uk.gov.hmcts.rse.ccd.lib.api.CFTLib;
import uk.gov.hmcts.rse.ccd.lib.api.CFTLibConfigurer;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Component
public class CftLibConfig implements CFTLibConfigurer {

    @Value("ccd-CIC-${CCD_DEF_NAME:dev}.xlsx")
    String defName;


    @Autowired
    CCDDefinitionGenerator configWriter;

    @Override
    public void configure(CFTLib lib) throws Exception {
        var users = Map.of(
//            "DivCaseWorkerUser@AAT.com", List.of("caseworker", "caseworker-divorce", "caseworker-divorce-courtadmin_beta"),
            "TEST_CASE_WORKER_USER@mailinator.com", List.of("caseworker", "caseworker-divorce", "casework-divorce-courtadmin_beta"),
            "TEST_SOLICITOR@mailinator.com", List.of("caseworker", "caseworker-divorce", "caseworker-divorce-solicitor"),
            "role.assignment.admin@gmail.com", List.of("caseworker")
        );

        for(var entry : users.entrySet()){
            lib.createIdamUser(entry.getKey(), entry.getValue().toArray(new String[0]));
            lib.createProfile(entry.getKey(), "CIC", "NO_FAULT_DIVORCE", "Submitted");
        }

        lib.createRoles(
            "caseworker-divorce-superuser",
            "caseworker-divorce-solicitor",
            "caseworker-divorce-systemupdate",
            "caseworker-sptribs-superuser",
            "caseworker-sptribs-cic-courtadmin",
            "citizen-sptribs-cic-dss",
            "caseworker-sptribs-cic-caseofficer",
            "caseworker-sptribs-cic-districtregistrar",
            "caseworker-sptribs-cic-districtjudge",
            "caseworker-sptribs-cic-respondent",
            "caseworker",
            "payments"
        );

        ResourceLoader resourceLoader = new DefaultResourceLoader();
        var json = IOUtils.toString(resourceLoader.getResource("classpath:cftlib-am-role-assignments.json")
            .getInputStream(), Charset.defaultCharset());
        lib.configureRoleAssignments(json);

                // Generate and import CCD definitions
        generateCCDDefinition();

        var nfdDefinition = Files.readAllBytes(Path.of("build/ccd-config/" + defName));
        lib.importDefinition(nfdDefinition);
    }

    /**
     * Generate our JSON ccd definition and convert it to xlsx.
     * Doing this at runtime in the CftlibConfig allows use of spring boot devtool's
     * live reload functionality to rapidly edit and test code & definition changes.
     */
    private void generateCCDDefinition() throws Exception {
        // Export the JSON config.
        configWriter.generateAllCaseTypesToJSON(new File("build/definitions"));
        // Run the gradle task to convert to xlsx.
        var code = new ProcessBuilder("./gradlew", "buildCCDXlsx")
            .inheritIO()
            .start()
            .waitFor();
        if (code != 0) {
            throw new RuntimeException("Error converting ccd json to xlsx");
        }
    }
}
