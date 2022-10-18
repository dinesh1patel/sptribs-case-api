#!/usr/bin/env bash

set -eu

dir=$(dirname ${0})

#${dir}/utils/idam-add-role.sh "ccd-import"
#${dir}/utils/idam-add-role.sh "caseworker"
#${dir}/utils/idam-add-role.sh "caseworker-cic"
#${dir}/utils/idam-add-role.sh "caseworker-approver"
#
## User used during the CCD import and ccd-role creation
#${dir}/utils/idam-create-caseworker.sh "ccd.docker.default@hmcts.net" "ccd-import"
#
#${dir}/utils/ccd-add-role.sh "caseworker-cic"
#${dir}/utils/ccd-add-role.sh "caseworker-approver"
#
#roles=("solicitor" "systemupdate" "admin" "staff" "judge")
#for role in "${roles[@]}"
#do
#  ${dir}/utils/idam-add-role.sh "caseworker-civil-${role}"
#  ${dir}/utils/ccd-add-role.sh "caseworker-civil-${role}"
#done

accessprofiles=("judge-profile" "basic-access" "legal-adviser" "GS_profile" "caseworker-ras-validation" "full-access" "admin-access" "civil-administrator-basic" "civil-administrator-standard" "hearing-schedule-access" "APP-SOL-UNSPEC-PROFILE" "APP-SOL-SPEC-PROFILE" "RES-SOL-ONE-UNSPEC-PROFILE" "RES-SOL-ONE-SPEC-PROFILE" "RES-SOL-TWO-UNSPEC-PROFILE" "RES-SOL-TWO-SPEC-PROFILE")

for accessprofile in "${accessprofiles[@]}"
do
  ${dir}/utils/ccd-add-role.sh "${accessprofile}"
done

#roles=("caa" "case-manager" "finance-manager" "organisation-manager" "user-manager")
#for role in "${roles[@]}"
#do
#  ${dir}/utils/idam-add-role.sh "pui-${role}"
#done
#
#${dir}/utils/idam-add-role.sh "caseworker-probate"
#${dir}/utils/idam-add-role.sh "caseworker-probate-solicitor"
#
#${dir}/utils/idam-add-role.sh "caseworker-ia"
#${dir}/utils/idam-add-role.sh "caseworker-ia-legalrep-solicitor"
#
#${dir}/utils/idam-add-role.sh "caseworker-publiclaw"
#${dir}/utils/idam-add-role.sh "caseworker-publiclaw-solicitor"
#
#${dir}/utils/idam-add-role.sh "xui-approver-userdata"
#
#${dir}/utils/idam-add-role.sh "caseworker-divorce"
#${dir}/utils/idam-add-role.sh "caseworker-divorce-solicitor"
#${dir}/utils/idam-add-role.sh "caseworker-divorce-financialremedy"
#${dir}/utils/idam-add-role.sh "caseworker-divorce-financialremedy-solicitor"
#
#prdRoles=('"caseworker"','"caseworker-caa"','"caseworker-divorce"','"caseworker-divorce-solicitor"','"caseworker-divorce-financialremedy"','"caseworker-divorce-financialremedy-solicitor"','"caseworker-probate"','"caseworker-ia"','"caseworker-probate-solicitor"','"caseworker-publiclaw"','"caseworker-ia-legalrep-solicitor"','"caseworker-publiclaw-solicitor"','"caseworker-civil"','"caseworker-civil-solicitor"','"xui-approver-userdata"','"pui-caa"','"prd-admin"','"pui-case-manager"','"pui-finance-manager"','"pui-organisation-manager"','"pui-user-manager"')
#${dir}/utils/idam-add-role.sh "prd-admin" "${prdRoles[@]}"
#${dir}/utils/idam-add-role.sh "payments"
#${dir}/utils/ccd-add-role.sh "payments"