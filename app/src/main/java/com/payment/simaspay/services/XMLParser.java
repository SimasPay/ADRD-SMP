package com.payment.simaspay.services;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import org.kxml.Attribute;
import org.kxml.Xml;
import org.kxml.parser.ParseEvent;
import org.kxml.parser.XmlParser;

//import android.util.Log;

import com.payment.simpaspay.constants.EncryptedResponseDataContainer;

public class XMLParser {

	public EncryptedResponseDataContainer parse(String xml) throws Exception {
		EncryptedResponseDataContainer result = new EncryptedResponseDataContainer();
		ByteArrayInputStream inStream = new ByteArrayInputStream(xml.getBytes());
		InputStreamReader reader = new InputStreamReader(inStream);
		XmlParser parser = new XmlParser(reader);
		traverse(parser, result);
		// System.out.println("reson1 :"+xml);
		return result;
	}

	public void traverse(XmlParser parser, EncryptedResponseDataContainer result)
			throws Exception {
		boolean leave = false;
		do {
			ParseEvent event = parser.read();
			ParseEvent pe;
			switch (event.getType()) {
			case Xml.START_TAG:
				if (Constants.XML_MESSAGE.equals(event.getName())) {
					pe = parser.read();
					result.setMsg(pe.getText());
					try {
						result.setMsgCode(event.getAttribute("code").getValue());
					} catch (Exception e) {
					}
				} else if (Constants.XML_RESPONSE_CODE.equals(event.getName())) {
					pe = parser.read();
					result.setResponseCode(pe.getText());
				} else if (Constants.XML_RESPONSE_MESSAGE.equals(event
						.getName())) {
					pe = parser.read();
					result.setResponseMsg(pe.getText());
				} else if (Constants.XML_NFC_CARD_ALIAS_NAME.equals(event
						.getName())) {
					pe = parser.read();
					result.setNfcCardAliasName(pe.getText());
				} else if (Constants.XML_NICK_NAME.equals(event.getName())) {
					pe = parser.read();
					result.setNickName(pe.getText());
				} else if (Constants.XML_FIRST_NAME.equals(event.getName())) {
					pe = parser.read();
					result.setFirstName(pe.getText());
				} else if (Constants.XML_LAST_NAME.equals(event.getName())) {
					pe = parser.read();
					result.setLastName(pe.getText());
				} else if (Constants.XML_ADITIONAL_INFO.equals(event.getName())) {
					pe = parser.read();
					result.setAditionalInfo(pe.getText());
				} else if (Constants.XML_REC_KYC_NAME.equals(event.getName())) {
					pe = parser.read();
					result.setKycName(pe.getText());
				} else if (Constants.XML_DOWNLOAD_URL.equals(event.getName())) {
					pe = parser.read();
					result.setDownLoadUrl(pe.getText());
				} else if (Constants.XML_PUBLIC_MODULUS.equals(event.getName())) {
					pe = parser.read();
					result.setPublicKeyModulus(pe.getText());
				} else if (Constants.XML_PUBLIC_EXPONENT
						.equals(event.getName())) {
					pe = parser.read();
					result.setPublicKeyExponet(pe.getText());
				} else if (Constants.XML_SUCCESS.equals(event.getName())) {
					pe = parser.read();
					result.setSuccess(pe.getText());
				} else if (Constants.XML_DEST_MDN.equals(event.getName())) {
					pe = parser.read();
					result.setDestMDN(pe.getText());
				} else if (Constants.XML_DEST_CUST_NAME.equals(event.getName())) {
					pe = parser.read();
					result.setCustName(pe.getText());
				} else if (Constants.XML_DEST_BANK.equals(event.getName())) {
					pe = parser.read();
					result.setDestBank(pe.getText());
				} else if (Constants.XML_ACCOUNT_NUMBER.equals(event.getName())) {
					pe = parser.read();
					result.setAccountNumber(pe.getText());
				} else if (Constants.XML_ISBANK.equals(event.getName())) {
					pe = parser.read();
					result.setIsBank(pe.getText());
				} else if (Constants.XML_ISEMONEY.equals(event.getName())) {
					pe = parser.read();
					result.setIsEmoney(pe.getText());
				} else if (Constants.XML_ISKYC.equals(event.getName())) {
					pe = parser.read();
					result.setIsKyc(pe.getText());
				}else if (Constants.XML_ISLAKUPANDAI.equals(event.getName())) {
					pe = parser.read();
					result.setIsLakuPandai(pe.getText());
				}else if (Constants.XML_CUSTOMERTYPE.equals(event.getName())) {
					pe = parser.read();
					result.setCustomerType(pe.getText());
				}else if (Constants.XML_ACCOUNT_NUMBER.equals(event.getName())) {
					pe = parser.read();
					Log.e("1234567890----------","---------"+pe.getText());
					result.setDestinationAccountNumber(pe.getText());

					Log.e("1234567890----------","---------"+result.getDestinationAccountNumber());
				}else if (Constants.XML_AMOUNT_TRANSFER
						.equals(event.getName())) {
					pe = parser.read();
					result.setAmount(pe.getText());
				}else if (Constants.XML_DATE
						.equals(event.getName())) {
					pe = parser.read();
					result.setDate(pe.getText());
				}else if (Constants.XML_TIME
						.equals(event.getName())) {
					pe = parser.read();
					result.setTime(pe.getText());
				} else if (Constants.XML_TRANSACTION_TIME.equals(event
						.getName())) {
					pe = parser.read();
					result.setTransactionTime(pe.getText());
				} else if (Constants.XML_REFID.equals(event.getName())) {
					pe = parser.read();
					result.setEncryptedRefId(pe.getText());
				} else if (Constants.XML_TRANSFERID.equals(event.getName())) {
					pe = parser.read();
					result.setEncryptedTransferId(pe.getText());
				} else if (Constants.XML_PARENT_TXNID.equals(event.getName())) {
					pe = parser.read();
					result.setEncryptedParentTxnId(pe.getText());
				}  else if (Constants.XML_BANKACCOUNT_NUMBER.equals(event.getName())) {
					pe = parser.read();
					result.setBankAccountNumber(pe.getText());
				} else if (Constants.XML_ID_NUMBER.equals(event.getName())) {
					pe = parser.read();
					result.setIdNumber(pe.getText());
				} else if (Constants.XML_SCTL.equals(event.getName())) {
					pe = parser.read();
					// System.out.println("hieeeeeeeeeREF_ID_XML"+pe.getText());
					result.setSctl(pe.getText());
				} else if (Constants.XML_MFAMODE.equals(event.getName())) {
					pe = parser.read();
					result.setMfaMode(pe.getText());
				} else if (Constants.XML_QAEXIST.equals(event.getName())) {
					pe = parser.read();
					result.setSqexist(pe.getText());
				} else if (Constants.XML_INPUT.equals(event.getName())) {
					Attribute name = event.getAttribute(Constants.XML_NAME);
					Attribute value = event.getAttribute(Constants.XML_VALUE);

					if ((null != name)
							&& !("".equals(name.getValue()) && "ParentTransactionID"
									.equals(name.getValue()))) {
						if (value != null) {
							result.setEncryptedParentTxnId(value.getValue());
						}
					}

					if ((null != name)
							&& !("".equals(name.getValue()) && "TransferID"
									.equals(name.getValue()))) {
						if (value != null) {
							result.setEncryptedTransferId(value.getValue());
						}
					}
				} else if (Constants.XML_AMOUNT.equals(event.getName())) {
					pe = parser.read();
					// System.out.println("Amount>>>"+pe.getText());
					result.setAmount(pe.getText());
				} else if (Constants.XML_KEY.equals(event.getName())) {
					pe = parser.read();
					result.setEncryptedAESkey(pe.getText());
				} else if (Constants.XML_SALT.equals(event.getName())) {
					pe = parser.read();
					result.setSalt(pe.getText());
				} else if (Constants.XML_AUTHENTICATION.equals(event.getName())) {
					pe = parser.read();
					result.setAuthenticationString(pe.getText());
				} else if (Constants.XML_TRANSACTION_CHARGES.equals(event
						.getName())) {
					pe = parser.read();
					result.setEncryptedTransactionCharges(pe.getText());
				} else if (Constants.XML_DEBIT_AMOUNT.equals(event.getName())) {
					pe = parser.read();
					result.setEncryptedDebitAmount(pe.getText());
				} else if (Constants.XML_CREDIT_AMOUNT.equals(event.getName())) {
					pe = parser.read();
					result.setEncryptedCreditAmount(pe.getText());
				} else if (Constants.XML_APPUPDATEURL.equals(event.getName())) {
					pe = parser.read();
					result.setAppUpdateURL(pe.getText());
				} else if (Constants.XML_APPURL.equals(event.getName())) {
					pe = parser.read();
					result.setAppURL(pe.getText());
				} else if (Constants.XML_REGISTRATION_MEDIUM.equals(event
						.getName())) {
					pe = parser.read();
					result.setRegistrationMedium(pe.getText());
				} else if (Constants.XML_BANK_ID.equals(event.getName())) {
					pe = parser.read();
					result.setBankId(pe.getText());
				} else if (Constants.XML_RESET_PIN_REQUEST.equals(event
						.getName())) {
					pe = parser.read();
					result.setResetPinRequested(pe.getText());
				} else if (Constants.XML_IS_ALREADY_ACTIVATED.equals(event
						.getName())) {
					pe = parser.read();
					result.setIsActivated(pe.getText());
				} else if (Constants.XML_STATUS.equals(event.getName())) {
					pe = parser.read();
					result.setStatus(pe.getText());
				} else if (Constants.XML_IS_UNREGISTER.equals(event.getName())) {
					pe = parser.read();
					result.setIsUnRegistered(pe.getText());
				} else if (Constants.XML_NON_KYC_ALLOW.equals(event.getName())) {
					pe = parser.read();
					result.setNonKYCAllow(pe.getText());
				} else if (Constants.XML_KYC_LEVEL.equals(event.getName())) {
					pe = parser.read();
					result.setKycLevel(pe.getText());
				} else if (Constants.XML_KYC_STATUS.equals(event.getName())) {
					pe = parser.read();
					result.setKycStatus(pe.getText());
//					Log.e("---parser---" + pe.getText(),
//							"" + result.getKycStatus());
				} else if (Constants.XML_PROMO_IMAGE_URL
						.equals(event.getName())) {
					pe = parser.read();
					result.setPromoImageUrl(pe.getText());
				} else if (Constants.XML_TOTAL_RECORDS.equals(event.getName())) {
					pe = parser.read();
					result.setTotalRecords(Integer.parseInt(pe.getText()));
				} else if (Constants.XML_MORE_TRANSACTIONS_AVAILABLE
						.equals(event.getName())) {
					pe = parser.read();
					result.setMoreRecordsAvailable(Boolean.valueOf((pe
							.getText())));
				} else if (Constants.XML_USER_API_KEY.equals(event.getName())) {
					pe = parser.read();
					result.setUserApiKey(pe.getText());
				} else if (Constants.XML_NONKYC_ALERT.equals(event.getName())) {
					pe = parser.read();
					result.setNonKycAlert(pe.getText());
				} else if (Constants.XML_BILL_DATE.equals(event.getName())) {
					pe = parser.read();
					result.setBillDate(pe.getText());
				} else if (Constants.XML_INVOICE_NO.equals(event.getName())) {
					pe = parser.read();
					result.setInvoiceNo(pe.getText());
				} else if (Constants.XML_NOMINAL_AMOUNT.equals(event.getName())) {
					pe = parser.read();
					result.setNominalAmount(pe.getText());
				} else if (Constants.XML_REFERENCE_NO.equals(event.getName())) {
					pe = parser.read();
					result.setReferenceNo(pe.getText());
				} else if (Constants.XML_ERROR_CODE.equals(event.getName())) {
					pe = parser.read();
					result.setErrorCode(pe.getText());
				} else if (Constants.XML_PROFILE_ID.equals(event.getName())) {
					pe = parser.read();
					result.setProfileId(pe.getText());
				} else if (Constants.XML_CURRENTBALANCE.equals(event.getName())) {
					pe = parser.read();
					result.setCurrentBalance(pe.getText());
				} else if (Constants.XML_NAME.equalsIgnoreCase(event.getName())) {
					pe = parser.read();
					result.setName(pe.getText());
				} else if (Constants.XML_MAXBALANCE.equals(event.getName())) {
					pe = parser.read();
					result.setMaxbalance(pe.getText());
				} else if (Constants.XML_REMAINBALANCE.equals(event.getName())) {
					pe = parser.read();
					result.setRemainbalance(pe.getText());
				} else if (Constants.XML_SECURITY_QUESTION.equals(event
						.getName())) {
					pe = parser.read();
					result.setSecurityQuestion(pe.getText());
				} else if (Constants.XML_RESET_PIN_BY_EMAIL.equals(event
						.getName())) {
					pe = parser.read();
					result.setResetPinByEmail(pe.getText());
				} else if (Constants.XML_RESET_PIN_BY_CSR.equals(event
						.getName())) {
					pe = parser.read();
					result.setResetPinByCSR(pe.getText());
				} else if (Constants.XML_EMAIL_VERIFIED.equals(event.getName())) {
					pe = parser.read();
					result.setEmailVerified(pe.getText());
				} else if (Constants.XML_PROFILE_IMAGE.equals(event.getName())) {
					pe = parser.read();
					result.setProfileImage(pe.getText());
				} else if (Constants.XML_DATE_OF_BIRTH.equals(event.getName())) {
					pe = parser.read();
					result.setDateOfBirth(pe.getText());
				}else if (Constants.XML_SESSION_TIMEOUT.equals(event.getName())) {
					pe = parser.read();
					result.setSessionTime(pe.getText());
				}else if (Constants.XML_MAX_TRAILS.equals(event.getName())) {
					pe = parser.read();
					result.setMaxTrails(pe.getText());
				}else if (Constants.XML_MSISDN.equals(event.getName())) {
					pe = parser.read();
					result.setMsisdn(pe.getText());
				}else if (Constants.XML_PROVINCE.equals(event.getName())) {
					pe = parser.read();
					result.setProvince(pe.getText());
				}else if (Constants.XML_DISTRICT.equals(event.getName())) {
					pe = parser.read();
					result.setDistrict(pe.getText());
				}else if (Constants.XML_SUB_DISTRICT.equals(event.getName())) {
					pe = parser.read();
					result.setSubDistrict(pe.getText());
				}else if (Constants.XML_CITY.equals(event.getName())) {
					pe = parser.read();
					result.setCity(pe.getText());
				}else if (Constants.XML_MOTHERSMEIDENNAME.equals(event.getName())) {
					pe = parser.read();
					result.setMothersMaidenName(pe.getText());
				}else if (Constants.XML_TRANSACTIONID.equals(event.getName())) {
					pe = parser.read();
					result.setTransactionId(pe.getText());
				}else if (Constants.XML_ADDRESSLINE.equals(event.getName())) {
					pe = parser.read();
					result.setAddressLine(pe.getText());
				}else if (Constants.XML_RT.equals(event.getName())) {
					pe = parser.read();
					result.setRt(pe.getText());
				}else if (Constants.XML_RW.equals(event.getName())) {
					pe = parser.read();
					result.setRw(pe.getText());
				}else if (Constants.XML_POSTALCODE.equals(event.getName())) {
					pe = parser.read();
					result.setPostalCode(pe.getText());
				}else if (Constants.XML_DOB.equals(event.getName())) {
					pe = parser.read();
					result.setDob(pe.getText());
				}else if (Constants.XML_BIRTHPLACE.equals(event.getName())) {
					pe = parser.read();
					result.setBirthPlace(pe.getText());
				}else if (Constants.XML_PROFILEIMAGE.equals(event.getName())) {
					pe = parser.read();
					result.setProfpicString(pe.getText());
				}

				traverse(parser, result); // recursion call for each <tag></tag>
				break;

			case Xml.END_TAG:
				leave = true;
				break;

			case Xml.END_DOCUMENT:
				leave = true;
				break;

			case Xml.TEXT:
				break;

			case Xml.WHITESPACE:
				break;

			default:
			}
		} while (!leave);
	}
	// public static void main(String[] args) throws Exception {
	// XMLParser p = new XMLParser();
	// String str = new String("<?xml version='1.0'?><response>" +
	// "<message code='72'>You requested to transfer IDR 120000 to 629876543210 -- XYZ.</message>"
	// +
	// "<transactionTime>28/01/2011 12:22</transactionTime><transferID>1000123</transferID>"
	// +
	// "<parentTxnID>78911231</parentTxnID></response>");
	// ResponseDataContainer res = p.parse(str);
	// //System.out.println("*********** = " + res.toString());
	// }
}
