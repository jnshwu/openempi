/**
 *
 * Copyright (C) 2002-2012 "SYSNET International, Inc."
 * support@sysnetint.com [http://www.sysnetint.com]
 *
 * This file is part of OpenEMPI.
 *
 * OpenEMPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package gov.hhs.fha.nhinc.common.nhinccommon;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.hhs.fha.nhinc.common.nhinccommon package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _PersonName_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "PersonName");
    private final static QName _SamlAuthnStatement_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "SamlAuthnStatement");
    private final static QName _User_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "User");
    private final static QName _NhinTargetCommunities_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "NhinTargetCommunities");
    private final static QName _SamlAuthzDecisionStatementEvidence_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "SamlAuthzDecisionStatementEvidence");
    private final static QName _AssigningAuthority_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "AssigningAuthority");
    private final static QName _Ce_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "Ce");
    private final static QName _NhinTargetSystem_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "NhinTargetSystem");
    private final static QName _QualifiedSubjectIdentifier_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "QualifiedSubjectIdentifier");
    private final static QName _SamlAuthzDecisionStatementEvidenceConditions_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "SamlAuthzDecisionStatementEvidenceConditions");
    private final static QName _AssigningAuthorites_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "assigningAuthorites");
    private final static QName _Assertion_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "Assertion");
    private final static QName _CreateEPRRequest_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "CreateEPRRequest");
    private final static QName _NhinTargetCommunity_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "NhinTargetCommunity");
    private final static QName _SamlAuthzDecisionStatementEvidenceAssertion_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "SamlAuthzDecisionStatementEvidenceAssertion");
    private final static QName _Addresses_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "Addresses");
    private final static QName _Phone_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "Phone");
    private final static QName _SamlSignatureKeyInfo_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "SamlSignatureKeyInfo");
    private final static QName _SamlSignature_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "SamlSignature");
    private final static QName _TokenRetrieveInfo_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "TokenRetrieveInfo");
    private final static QName _TokenCreationInfo_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "TokenCreationInfo");
    private final static QName _EPR_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "EPR");
    private final static QName _QualifiedSubjectIdentifiers_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "QualifiedSubjectIdentifiers");
    private final static QName _HomeCommunity_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "HomeCommunity");
    private final static QName _Response_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "Response");
    private final static QName _Address_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "Address");
    private final static QName _Acknowledgement_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "Acknowledgement");
    private final static QName _HomeCommunities_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "HomeCommunities");
    private final static QName _SamlAuthzDecisionStatement_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommon", "SamlAuthzDecisionStatement");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.hhs.fha.nhinc.common.nhinccommon
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AddressesType }
     * 
     */
    public AddressesType createAddressesType() {
        return new AddressesType();
    }

    /**
     * Create an instance of {@link SamlAuthzDecisionStatementEvidenceAssertionType }
     * 
     */
    public SamlAuthzDecisionStatementEvidenceAssertionType createSamlAuthzDecisionStatementEvidenceAssertionType() {
        return new SamlAuthzDecisionStatementEvidenceAssertionType();
    }

    /**
     * Create an instance of {@link NhinTargetCommunitiesType }
     * 
     */
    public NhinTargetCommunitiesType createNhinTargetCommunitiesType() {
        return new NhinTargetCommunitiesType();
    }

    /**
     * Create an instance of {@link SamlSignatureType }
     * 
     */
    public SamlSignatureType createSamlSignatureType() {
        return new SamlSignatureType();
    }

    /**
     * Create an instance of {@link SamlAuthnStatementType }
     * 
     */
    public SamlAuthnStatementType createSamlAuthnStatementType() {
        return new SamlAuthnStatementType();
    }

    /**
     * Create an instance of {@link TokenCreationInfoType }
     * 
     */
    public TokenCreationInfoType createTokenCreationInfoType() {
        return new TokenCreationInfoType();
    }

    /**
     * Create an instance of {@link SamlAuthzDecisionStatementType }
     * 
     */
    public SamlAuthzDecisionStatementType createSamlAuthzDecisionStatementType() {
        return new SamlAuthzDecisionStatementType();
    }

    /**
     * Create an instance of {@link AddressType }
     * 
     */
    public AddressType createAddressType() {
        return new AddressType();
    }

    /**
     * Create an instance of {@link CreateEPRRequestType }
     * 
     */
    public CreateEPRRequestType createCreateEPRRequestType() {
        return new CreateEPRRequestType();
    }

    /**
     * Create an instance of {@link QualifiedSubjectIdentifierType }
     * 
     */
    public QualifiedSubjectIdentifierType createQualifiedSubjectIdentifierType() {
        return new QualifiedSubjectIdentifierType();
    }

    /**
     * Create an instance of {@link SamlAuthzDecisionStatementEvidenceType }
     * 
     */
    public SamlAuthzDecisionStatementEvidenceType createSamlAuthzDecisionStatementEvidenceType() {
        return new SamlAuthzDecisionStatementEvidenceType();
    }

    /**
     * Create an instance of {@link CeType }
     * 
     */
    public CeType createCeType() {
        return new CeType();
    }

    /**
     * Create an instance of {@link NhinTargetCommunityType }
     * 
     */
    public NhinTargetCommunityType createNhinTargetCommunityType() {
        return new NhinTargetCommunityType();
    }

    /**
     * Create an instance of {@link EPRType }
     * 
     */
    public EPRType createEPRType() {
        return new EPRType();
    }

    /**
     * Create an instance of {@link HomeCommunitiesType }
     * 
     */
    public HomeCommunitiesType createHomeCommunitiesType() {
        return new HomeCommunitiesType();
    }

    /**
     * Create an instance of {@link QualifiedSubjectIdentifiersType }
     * 
     */
    public QualifiedSubjectIdentifiersType createQualifiedSubjectIdentifiersType() {
        return new QualifiedSubjectIdentifiersType();
    }

    /**
     * Create an instance of {@link NhinTargetSystemType }
     * 
     */
    public NhinTargetSystemType createNhinTargetSystemType() {
        return new NhinTargetSystemType();
    }

    /**
     * Create an instance of {@link HomeCommunityType }
     * 
     */
    public HomeCommunityType createHomeCommunityType() {
        return new HomeCommunityType();
    }

    /**
     * Create an instance of {@link AssigningAuthorityType }
     * 
     */
    public AssigningAuthorityType createAssigningAuthorityType() {
        return new AssigningAuthorityType();
    }

    /**
     * Create an instance of {@link AssertionType }
     * 
     */
    public AssertionType createAssertionType() {
        return new AssertionType();
    }

    /**
     * Create an instance of {@link UserType }
     * 
     */
    public UserType createUserType() {
        return new UserType();
    }

    /**
     * Create an instance of {@link TokenRetrieveInfoType }
     * 
     */
    public TokenRetrieveInfoType createTokenRetrieveInfoType() {
        return new TokenRetrieveInfoType();
    }

    /**
     * Create an instance of {@link SamlSignatureKeyInfoType }
     * 
     */
    public SamlSignatureKeyInfoType createSamlSignatureKeyInfoType() {
        return new SamlSignatureKeyInfoType();
    }

    /**
     * Create an instance of {@link ResponseType }
     * 
     */
    public ResponseType createResponseType() {
        return new ResponseType();
    }

    /**
     * Create an instance of {@link PhoneType }
     * 
     */
    public PhoneType createPhoneType() {
        return new PhoneType();
    }

    /**
     * Create an instance of {@link AcknowledgementType }
     * 
     */
    public AcknowledgementType createAcknowledgementType() {
        return new AcknowledgementType();
    }

    /**
     * Create an instance of {@link SamlAuthzDecisionStatementEvidenceConditionsType }
     * 
     */
    public SamlAuthzDecisionStatementEvidenceConditionsType createSamlAuthzDecisionStatementEvidenceConditionsType() {
        return new SamlAuthzDecisionStatementEvidenceConditionsType();
    }

    /**
     * Create an instance of {@link PersonNameType }
     * 
     */
    public PersonNameType createPersonNameType() {
        return new PersonNameType();
    }

    /**
     * Create an instance of {@link AssigningAuthoritiesType }
     * 
     */
    public AssigningAuthoritiesType createAssigningAuthoritiesType() {
        return new AssigningAuthoritiesType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersonNameType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "PersonName")
    public JAXBElement<PersonNameType> createPersonName(PersonNameType value) {
        return new JAXBElement<PersonNameType>(_PersonName_QNAME, PersonNameType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SamlAuthnStatementType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "SamlAuthnStatement")
    public JAXBElement<SamlAuthnStatementType> createSamlAuthnStatement(SamlAuthnStatementType value) {
        return new JAXBElement<SamlAuthnStatementType>(_SamlAuthnStatement_QNAME, SamlAuthnStatementType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "User")
    public JAXBElement<UserType> createUser(UserType value) {
        return new JAXBElement<UserType>(_User_QNAME, UserType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NhinTargetCommunitiesType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "NhinTargetCommunities")
    public JAXBElement<NhinTargetCommunitiesType> createNhinTargetCommunities(NhinTargetCommunitiesType value) {
        return new JAXBElement<NhinTargetCommunitiesType>(_NhinTargetCommunities_QNAME, NhinTargetCommunitiesType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SamlAuthzDecisionStatementEvidenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "SamlAuthzDecisionStatementEvidence")
    public JAXBElement<SamlAuthzDecisionStatementEvidenceType> createSamlAuthzDecisionStatementEvidence(SamlAuthzDecisionStatementEvidenceType value) {
        return new JAXBElement<SamlAuthzDecisionStatementEvidenceType>(_SamlAuthzDecisionStatementEvidence_QNAME, SamlAuthzDecisionStatementEvidenceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AssigningAuthorityType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "AssigningAuthority")
    public JAXBElement<AssigningAuthorityType> createAssigningAuthority(AssigningAuthorityType value) {
        return new JAXBElement<AssigningAuthorityType>(_AssigningAuthority_QNAME, AssigningAuthorityType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "Ce")
    public JAXBElement<CeType> createCe(CeType value) {
        return new JAXBElement<CeType>(_Ce_QNAME, CeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NhinTargetSystemType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "NhinTargetSystem")
    public JAXBElement<NhinTargetSystemType> createNhinTargetSystem(NhinTargetSystemType value) {
        return new JAXBElement<NhinTargetSystemType>(_NhinTargetSystem_QNAME, NhinTargetSystemType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QualifiedSubjectIdentifierType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "QualifiedSubjectIdentifier")
    public JAXBElement<QualifiedSubjectIdentifierType> createQualifiedSubjectIdentifier(QualifiedSubjectIdentifierType value) {
        return new JAXBElement<QualifiedSubjectIdentifierType>(_QualifiedSubjectIdentifier_QNAME, QualifiedSubjectIdentifierType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SamlAuthzDecisionStatementEvidenceConditionsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "SamlAuthzDecisionStatementEvidenceConditions")
    public JAXBElement<SamlAuthzDecisionStatementEvidenceConditionsType> createSamlAuthzDecisionStatementEvidenceConditions(SamlAuthzDecisionStatementEvidenceConditionsType value) {
        return new JAXBElement<SamlAuthzDecisionStatementEvidenceConditionsType>(_SamlAuthzDecisionStatementEvidenceConditions_QNAME, SamlAuthzDecisionStatementEvidenceConditionsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AssigningAuthoritiesType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "assigningAuthorites")
    public JAXBElement<AssigningAuthoritiesType> createAssigningAuthorites(AssigningAuthoritiesType value) {
        return new JAXBElement<AssigningAuthoritiesType>(_AssigningAuthorites_QNAME, AssigningAuthoritiesType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AssertionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "Assertion")
    public JAXBElement<AssertionType> createAssertion(AssertionType value) {
        return new JAXBElement<AssertionType>(_Assertion_QNAME, AssertionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateEPRRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "CreateEPRRequest")
    public JAXBElement<CreateEPRRequestType> createCreateEPRRequest(CreateEPRRequestType value) {
        return new JAXBElement<CreateEPRRequestType>(_CreateEPRRequest_QNAME, CreateEPRRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NhinTargetCommunityType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "NhinTargetCommunity")
    public JAXBElement<NhinTargetCommunityType> createNhinTargetCommunity(NhinTargetCommunityType value) {
        return new JAXBElement<NhinTargetCommunityType>(_NhinTargetCommunity_QNAME, NhinTargetCommunityType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SamlAuthzDecisionStatementEvidenceAssertionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "SamlAuthzDecisionStatementEvidenceAssertion")
    public JAXBElement<SamlAuthzDecisionStatementEvidenceAssertionType> createSamlAuthzDecisionStatementEvidenceAssertion(SamlAuthzDecisionStatementEvidenceAssertionType value) {
        return new JAXBElement<SamlAuthzDecisionStatementEvidenceAssertionType>(_SamlAuthzDecisionStatementEvidenceAssertion_QNAME, SamlAuthzDecisionStatementEvidenceAssertionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddressesType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "Addresses")
    public JAXBElement<AddressesType> createAddresses(AddressesType value) {
        return new JAXBElement<AddressesType>(_Addresses_QNAME, AddressesType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PhoneType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "Phone")
    public JAXBElement<PhoneType> createPhone(PhoneType value) {
        return new JAXBElement<PhoneType>(_Phone_QNAME, PhoneType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SamlSignatureKeyInfoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "SamlSignatureKeyInfo")
    public JAXBElement<SamlSignatureKeyInfoType> createSamlSignatureKeyInfo(SamlSignatureKeyInfoType value) {
        return new JAXBElement<SamlSignatureKeyInfoType>(_SamlSignatureKeyInfo_QNAME, SamlSignatureKeyInfoType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SamlSignatureType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "SamlSignature")
    public JAXBElement<SamlSignatureType> createSamlSignature(SamlSignatureType value) {
        return new JAXBElement<SamlSignatureType>(_SamlSignature_QNAME, SamlSignatureType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TokenRetrieveInfoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "TokenRetrieveInfo")
    public JAXBElement<TokenRetrieveInfoType> createTokenRetrieveInfo(TokenRetrieveInfoType value) {
        return new JAXBElement<TokenRetrieveInfoType>(_TokenRetrieveInfo_QNAME, TokenRetrieveInfoType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TokenCreationInfoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "TokenCreationInfo")
    public JAXBElement<TokenCreationInfoType> createTokenCreationInfo(TokenCreationInfoType value) {
        return new JAXBElement<TokenCreationInfoType>(_TokenCreationInfo_QNAME, TokenCreationInfoType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EPRType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "EPR")
    public JAXBElement<EPRType> createEPR(EPRType value) {
        return new JAXBElement<EPRType>(_EPR_QNAME, EPRType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QualifiedSubjectIdentifiersType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "QualifiedSubjectIdentifiers")
    public JAXBElement<QualifiedSubjectIdentifiersType> createQualifiedSubjectIdentifiers(QualifiedSubjectIdentifiersType value) {
        return new JAXBElement<QualifiedSubjectIdentifiersType>(_QualifiedSubjectIdentifiers_QNAME, QualifiedSubjectIdentifiersType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HomeCommunityType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "HomeCommunity")
    public JAXBElement<HomeCommunityType> createHomeCommunity(HomeCommunityType value) {
        return new JAXBElement<HomeCommunityType>(_HomeCommunity_QNAME, HomeCommunityType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "Response")
    public JAXBElement<ResponseType> createResponse(ResponseType value) {
        return new JAXBElement<ResponseType>(_Response_QNAME, ResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddressType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "Address")
    public JAXBElement<AddressType> createAddress(AddressType value) {
        return new JAXBElement<AddressType>(_Address_QNAME, AddressType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AcknowledgementType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "Acknowledgement")
    public JAXBElement<AcknowledgementType> createAcknowledgement(AcknowledgementType value) {
        return new JAXBElement<AcknowledgementType>(_Acknowledgement_QNAME, AcknowledgementType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HomeCommunitiesType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "HomeCommunities")
    public JAXBElement<HomeCommunitiesType> createHomeCommunities(HomeCommunitiesType value) {
        return new JAXBElement<HomeCommunitiesType>(_HomeCommunities_QNAME, HomeCommunitiesType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SamlAuthzDecisionStatementType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:nhinccommon", name = "SamlAuthzDecisionStatement")
    public JAXBElement<SamlAuthzDecisionStatementType> createSamlAuthzDecisionStatement(SamlAuthzDecisionStatementType value) {
        return new JAXBElement<SamlAuthzDecisionStatementType>(_SamlAuthzDecisionStatement_QNAME, SamlAuthzDecisionStatementType.class, null, value);
    }

}
