package com.actuate.rsse.example;

import java.util.Iterator;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.AxisFault;

/**
 * SampleRSSEReceiverInOut message receiver
 */

public class SampleRSSEReceiverInOut extends org.apache.axis2.receivers.AbstractInOutMessageReceiver {

	public void invokeBusinessLogic(org.apache.axis2.context.MessageContext msgContext,
			org.apache.axis2.context.MessageContext newMsgContext) throws org.apache.axis2.AxisFault {

		try {
			// get header element Volume
			//<axis2ns13927:Volume xmlns:axis2ns13927="http://schemas.actuate.com/rsse">test</axis2ns13927:Volume>
			String volume = null;
			SOAPHeader soapHeader = msgContext.getEnvelope().getHeader();
			Iterator<OMElement> it = soapHeader.getChildrenWithLocalName("Volume");
			if(!it.hasNext()) {
				it = soapHeader.getChildrenWithLocalName("TargetVolume");
			}
			if(it.hasNext()) {
				volume  = it.next().getText();
			} else {
				throw new AxisFault("Volume cannot be null.");
			}
//			while (it.hasNext()) {
//				//volume = "Default Volume";
//				code++;
//				OMElement el = it.next();
//				if(el.getLocalName().equals("Volume") || el.getLocalName().equals("TargetVolume")) {
//					volume = el.getText();
//					break;
//				}
//			}
			
			// get the implementation class for the Web Service
			Object obj = getTheImplementationObject(msgContext);

			SampleRSSEInterface skel = (SampleRSSEInterface) obj;
			// Out Envelop
			org.apache.axiom.soap.SOAPEnvelope envelope = null;
			// Find the axisOperation that has been set by the Dispatch phase.
			org.apache.axis2.description.AxisOperation op = msgContext.getOperationContext().getAxisOperation();
			if (op == null) {
				throw new org.apache.axis2.AxisFault(
						"Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
			}

			java.lang.String methodName;
			if ((op.getName() != null)
					&& ((methodName = org.apache.axis2.util.JavaUtils.xmlNameToJavaIdentifier(op.getName()
							.getLocalPart())) != null)) {

				if ("getTranslatedUserNames".equals(methodName)) {

					com.actuate.schemas.actuate11.rsse.GetTranslatedUserNamesResponseE getTranslatedUserNamesResponse33 = null;
					com.actuate.schemas.actuate11.rsse.GetTranslatedUserNamesE wrappedParam = (com.actuate.schemas.actuate11.rsse.GetTranslatedUserNamesE) fromOM(
							msgContext.getEnvelope().getBody().getFirstElement(),
							com.actuate.schemas.actuate11.rsse.GetTranslatedUserNamesE.class,
							getEnvelopeNamespaces(msgContext.getEnvelope()));

					getTranslatedUserNamesResponse33 =

					skel.getTranslatedUserNames(volume, wrappedParam);

					envelope = toEnvelope(getSOAPFactory(msgContext), getTranslatedUserNamesResponse33, false,
							new javax.xml.namespace.QName("http://schemas.actuate.com/actuate11/rsse/wsdl",
									"getTranslatedUserNames"));
				} else

				if ("stop".equals(methodName)) {

					com.actuate.schemas.actuate11.rsse.StopResponseE stopResponse35 = null;
					com.actuate.schemas.actuate11.rsse.StopE wrappedParam = (com.actuate.schemas.actuate11.rsse.StopE) fromOM(
							msgContext.getEnvelope().getBody().getFirstElement(),
							com.actuate.schemas.actuate11.rsse.StopE.class,
							getEnvelopeNamespaces(msgContext.getEnvelope()));

					stopResponse35 =

					skel.stop(wrappedParam);

					envelope = toEnvelope(getSOAPFactory(msgContext), stopResponse35, false,
							new javax.xml.namespace.QName("http://schemas.actuate.com/actuate11/rsse/wsdl", "stop"));
				} else

				if ("authenticate".equals(methodName)) {

					com.actuate.schemas.actuate11.rsse.AuthenticateResponseE authenticateResponse37 = null;
					com.actuate.schemas.actuate11.rsse.AuthenticateE wrappedParam = (com.actuate.schemas.actuate11.rsse.AuthenticateE) fromOM(
							msgContext.getEnvelope().getBody().getFirstElement(),
							com.actuate.schemas.actuate11.rsse.AuthenticateE.class,
							getEnvelopeNamespaces(msgContext.getEnvelope()));

					authenticateResponse37 =

					skel.authenticate(volume, wrappedParam);

					envelope = toEnvelope(getSOAPFactory(msgContext), authenticateResponse37, false,
							new javax.xml.namespace.QName("http://schemas.actuate.com/actuate11/rsse/wsdl",
									"authenticate"));
				} else

				if ("selectUsers".equals(methodName)) {

					com.actuate.schemas.actuate11.rsse.SelectUsersResponseE selectUsersResponse39 = null;
					com.actuate.schemas.actuate11.rsse.SelectUsersE wrappedParam = (com.actuate.schemas.actuate11.rsse.SelectUsersE) fromOM(
							msgContext.getEnvelope().getBody().getFirstElement(),
							com.actuate.schemas.actuate11.rsse.SelectUsersE.class,
							getEnvelopeNamespaces(msgContext.getEnvelope()));

					selectUsersResponse39 =

					skel.selectUsers(volume, wrappedParam);

					envelope = toEnvelope(getSOAPFactory(msgContext), selectUsersResponse39, false,
							new javax.xml.namespace.QName("http://schemas.actuate.com/actuate11/rsse/wsdl",
									"selectUsers"));
				} else

				if ("getConnectionProperties".equals(methodName)) {

					com.actuate.schemas.actuate11.rsse.GetConnectionPropertiesResponseE getConnectionPropertiesResponse41 = null;
					com.actuate.schemas.actuate11.rsse.GetConnectionPropertiesE wrappedParam = (com.actuate.schemas.actuate11.rsse.GetConnectionPropertiesE) fromOM(
							msgContext.getEnvelope().getBody().getFirstElement(),
							com.actuate.schemas.actuate11.rsse.GetConnectionPropertiesE.class,
							getEnvelopeNamespaces(msgContext.getEnvelope()));

					getConnectionPropertiesResponse41 =

					skel.getConnectionProperties(wrappedParam);

					envelope = toEnvelope(getSOAPFactory(msgContext), getConnectionPropertiesResponse41, false,
							new javax.xml.namespace.QName("http://schemas.actuate.com/actuate11/rsse/wsdl",
									"getConnectionProperties"));
				} else

				if ("getUsersToNotify".equals(methodName)) {

					com.actuate.schemas.actuate11.rsse.GetUsersToNotifyResponseE getUsersToNotifyResponse43 = null;
					com.actuate.schemas.actuate11.rsse.GetUsersToNotifyE wrappedParam = (com.actuate.schemas.actuate11.rsse.GetUsersToNotifyE) fromOM(
							msgContext.getEnvelope().getBody().getFirstElement(),
							com.actuate.schemas.actuate11.rsse.GetUsersToNotifyE.class,
							getEnvelopeNamespaces(msgContext.getEnvelope()));

					getUsersToNotifyResponse43 =

					skel.getUsersToNotify(volume, wrappedParam);

					envelope = toEnvelope(getSOAPFactory(msgContext), getUsersToNotifyResponse43, false,
							new javax.xml.namespace.QName("http://schemas.actuate.com/actuate11/rsse/wsdl",
									"getUsersToNotify"));
				} else

				if ("doesUserExist".equals(methodName)) {

					com.actuate.schemas.actuate11.rsse.DoesUserExistResponseE doesUserExistResponse45 = null;
					com.actuate.schemas.actuate11.rsse.DoesUserExistE wrappedParam = (com.actuate.schemas.actuate11.rsse.DoesUserExistE) fromOM(
							msgContext.getEnvelope().getBody().getFirstElement(),
							com.actuate.schemas.actuate11.rsse.DoesUserExistE.class,
							getEnvelopeNamespaces(msgContext.getEnvelope()));

					doesUserExistResponse45 =

					skel.doesUserExist(volume, wrappedParam);

					envelope = toEnvelope(getSOAPFactory(msgContext), doesUserExistResponse45, false,
							new javax.xml.namespace.QName("http://schemas.actuate.com/actuate11/rsse/wsdl",
									"doesUserExist"));
				} else

				if ("start".equals(methodName)) {

					com.actuate.schemas.actuate11.rsse.StartResponseE startResponse47 = null;
					com.actuate.schemas.actuate11.rsse.StartE wrappedParam = (com.actuate.schemas.actuate11.rsse.StartE) fromOM(
							msgContext.getEnvelope().getBody().getFirstElement(),
							com.actuate.schemas.actuate11.rsse.StartE.class,
							getEnvelopeNamespaces(msgContext.getEnvelope()));

					startResponse47 =

					skel.start(wrappedParam);

					envelope = toEnvelope(getSOAPFactory(msgContext), startResponse47, false,
							new javax.xml.namespace.QName("http://schemas.actuate.com/actuate11/rsse/wsdl", "start"));
				} else

				if ("passThrough".equals(methodName)) {

					com.actuate.schemas.actuate11.rsse.PassThroughResponseE passThroughResponse49 = null;
					com.actuate.schemas.actuate11.rsse.PassThroughE wrappedParam = (com.actuate.schemas.actuate11.rsse.PassThroughE) fromOM(
							msgContext.getEnvelope().getBody().getFirstElement(),
							com.actuate.schemas.actuate11.rsse.PassThroughE.class,
							getEnvelopeNamespaces(msgContext.getEnvelope()));

					passThroughResponse49 =

					skel.passThrough(wrappedParam);

					envelope = toEnvelope(getSOAPFactory(msgContext), passThroughResponse49, false,
							new javax.xml.namespace.QName("http://schemas.actuate.com/actuate11/rsse/wsdl",
									"passThrough"));
				} else

				if ("selectGroups".equals(methodName)) {

					com.actuate.schemas.actuate11.rsse.SelectGroupsResponseE selectGroupsResponse51 = null;
					com.actuate.schemas.actuate11.rsse.SelectGroupsE wrappedParam = (com.actuate.schemas.actuate11.rsse.SelectGroupsE) fromOM(
							msgContext.getEnvelope().getBody().getFirstElement(),
							com.actuate.schemas.actuate11.rsse.SelectGroupsE.class,
							getEnvelopeNamespaces(msgContext.getEnvelope()));

					selectGroupsResponse51 =

					skel.selectGroups(volume, wrappedParam);

					envelope = toEnvelope(getSOAPFactory(msgContext), selectGroupsResponse51, false,
							new javax.xml.namespace.QName("http://schemas.actuate.com/actuate11/rsse/wsdl",
									"selectGroups"));
				} else

				if ("doesRoleExist".equals(methodName)) {

					com.actuate.schemas.actuate11.rsse.DoesRoleExistResponseE doesRoleExistResponse53 = null;
					com.actuate.schemas.actuate11.rsse.DoesRoleExistE wrappedParam = (com.actuate.schemas.actuate11.rsse.DoesRoleExistE) fromOM(
							msgContext.getEnvelope().getBody().getFirstElement(),
							com.actuate.schemas.actuate11.rsse.DoesRoleExistE.class,
							getEnvelopeNamespaces(msgContext.getEnvelope()));

					doesRoleExistResponse53 =

					skel.doesRoleExist(volume, wrappedParam);

					envelope = toEnvelope(getSOAPFactory(msgContext), doesRoleExistResponse53, false,
							new javax.xml.namespace.QName("http://schemas.actuate.com/actuate11/rsse/wsdl",
									"doesRoleExist"));
				} else

				if ("selectRoles".equals(methodName)) {

					com.actuate.schemas.actuate11.rsse.SelectRolesResponseE selectRolesResponse55 = null;
					com.actuate.schemas.actuate11.rsse.SelectRolesE wrappedParam = (com.actuate.schemas.actuate11.rsse.SelectRolesE) fromOM(
							msgContext.getEnvelope().getBody().getFirstElement(),
							com.actuate.schemas.actuate11.rsse.SelectRolesE.class,
							getEnvelopeNamespaces(msgContext.getEnvelope()));

					selectRolesResponse55 =

					skel.selectRoles(volume, wrappedParam);

					envelope = toEnvelope(getSOAPFactory(msgContext), selectRolesResponse55, false,
							new javax.xml.namespace.QName("http://schemas.actuate.com/actuate11/rsse/wsdl",
									"selectRoles"));
				} else

				if ("getUserProperties".equals(methodName)) {

					com.actuate.schemas.actuate11.rsse.GetUserPropertiesResponseE getUserPropertiesResponse57 = null;
					com.actuate.schemas.actuate11.rsse.GetUserPropertiesE wrappedParam = (com.actuate.schemas.actuate11.rsse.GetUserPropertiesE) fromOM(
							msgContext.getEnvelope().getBody().getFirstElement(),
							com.actuate.schemas.actuate11.rsse.GetUserPropertiesE.class,
							getEnvelopeNamespaces(msgContext.getEnvelope()));

					getUserPropertiesResponse57 =

					skel.getUserProperties(volume, wrappedParam);

					envelope = toEnvelope(getSOAPFactory(msgContext), getUserPropertiesResponse57, false,
							new javax.xml.namespace.QName("http://schemas.actuate.com/actuate11/rsse/wsdl",
									"getUserProperties"));
				} else

				if ("doesGroupExist".equals(methodName)) {

					com.actuate.schemas.actuate11.rsse.DoesGroupExistResponseE doesGroupExistResponse59 = null;
					com.actuate.schemas.actuate11.rsse.DoesGroupExistE wrappedParam = (com.actuate.schemas.actuate11.rsse.DoesGroupExistE) fromOM(
							msgContext.getEnvelope().getBody().getFirstElement(),
							com.actuate.schemas.actuate11.rsse.DoesGroupExistE.class,
							getEnvelopeNamespaces(msgContext.getEnvelope()));

					doesGroupExistResponse59 =

					skel.doesGroupExist(volume, wrappedParam);

					envelope = toEnvelope(getSOAPFactory(msgContext), doesGroupExistResponse59, false,
							new javax.xml.namespace.QName("http://schemas.actuate.com/actuate11/rsse/wsdl",
									"doesGroupExist"));
				} else

				if ("getTranslatedRoleNames".equals(methodName)) {

					com.actuate.schemas.actuate11.rsse.GetTranslatedRoleNamesResponseE getTranslatedRoleNamesResponse61 = null;
					com.actuate.schemas.actuate11.rsse.GetTranslatedRoleNamesE wrappedParam = (com.actuate.schemas.actuate11.rsse.GetTranslatedRoleNamesE) fromOM(
							msgContext.getEnvelope().getBody().getFirstElement(),
							com.actuate.schemas.actuate11.rsse.GetTranslatedRoleNamesE.class,
							getEnvelopeNamespaces(msgContext.getEnvelope()));

					getTranslatedRoleNamesResponse61 =

					skel.getTranslatedRoleNames(volume, wrappedParam);

					envelope = toEnvelope(getSOAPFactory(msgContext), getTranslatedRoleNamesResponse61, false,
							new javax.xml.namespace.QName("http://schemas.actuate.com/actuate11/rsse/wsdl",
									"getTranslatedRoleNames"));
				} else

				if ("getUserACL".equals(methodName)) {

					com.actuate.schemas.actuate11.rsse.GetUserACLResponseE getUserACLResponse63 = null;
					com.actuate.schemas.actuate11.rsse.GetUserACLE wrappedParam = (com.actuate.schemas.actuate11.rsse.GetUserACLE) fromOM(
							msgContext.getEnvelope().getBody().getFirstElement(),
							com.actuate.schemas.actuate11.rsse.GetUserACLE.class,
							getEnvelopeNamespaces(msgContext.getEnvelope()));

					getUserACLResponse63 =

					skel.getUserACL(volume, wrappedParam);

					envelope = toEnvelope(getSOAPFactory(msgContext), getUserACLResponse63, false,
							new javax.xml.namespace.QName("http://schemas.actuate.com/actuate11/rsse/wsdl",
									"getUserACL"));

				} else {
					throw new java.lang.RuntimeException("method not found");
				}

				newMsgContext.setEnvelope(envelope);
			}
		} catch (java.lang.Exception e) {
			throw createAxisFault(e);
		}
	}

	//
	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.GetTranslatedUserNamesE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.GetTranslatedUserNamesE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(
			com.actuate.schemas.actuate11.rsse.GetTranslatedUserNamesResponseE param, boolean optimizeContent)
			throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.GetTranslatedUserNamesResponseE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.StopE param, boolean optimizeContent)
			throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.StopE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.StopResponseE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.StopResponseE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.AuthenticateE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.AuthenticateE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.AuthenticateResponseE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.AuthenticateResponseE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.SelectUsersE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.SelectUsersE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.SelectUsersResponseE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.SelectUsersResponseE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.GetConnectionPropertiesE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.GetConnectionPropertiesE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(
			com.actuate.schemas.actuate11.rsse.GetConnectionPropertiesResponseE param, boolean optimizeContent)
			throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.GetConnectionPropertiesResponseE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.GetUsersToNotifyE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.GetUsersToNotifyE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.GetUsersToNotifyResponseE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.GetUsersToNotifyResponseE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.DoesUserExistE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.DoesUserExistE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.DoesUserExistResponseE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.DoesUserExistResponseE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.StartE param, boolean optimizeContent)
			throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.StartE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.StartResponseE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.StartResponseE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.PassThroughE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.PassThroughE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.PassThroughResponseE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.PassThroughResponseE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.SelectGroupsE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.SelectGroupsE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.SelectGroupsResponseE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.SelectGroupsResponseE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.DoesRoleExistE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.DoesRoleExistE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.DoesRoleExistResponseE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.DoesRoleExistResponseE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.SelectRolesE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.SelectRolesE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.SelectRolesResponseE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.SelectRolesResponseE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.GetUserPropertiesE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.GetUserPropertiesE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.GetUserPropertiesResponseE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.GetUserPropertiesResponseE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.DoesGroupExistE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.DoesGroupExistE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.DoesGroupExistResponseE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.DoesGroupExistResponseE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.GetTranslatedRoleNamesE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.GetTranslatedRoleNamesE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(
			com.actuate.schemas.actuate11.rsse.GetTranslatedRoleNamesResponseE param, boolean optimizeContent)
			throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.GetTranslatedRoleNamesResponseE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.GetUserACLE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.GetUserACLE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(com.actuate.schemas.actuate11.rsse.GetUserACLResponseE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(com.actuate.schemas.actuate11.rsse.GetUserACLResponseE.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}

	}

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
			com.actuate.schemas.actuate11.rsse.GetTranslatedUserNamesResponseE param, boolean optimizeContent,
			javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {
		try {
			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();

			emptyEnvelope.getBody().addChild(
					param.getOMElement(com.actuate.schemas.actuate11.rsse.GetTranslatedUserNamesResponseE.MY_QNAME,
							factory));

			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}
	}

	private com.actuate.schemas.actuate11.rsse.GetTranslatedUserNamesResponseE wrapgetTranslatedUserNames() {
		com.actuate.schemas.actuate11.rsse.GetTranslatedUserNamesResponseE wrappedElement = new com.actuate.schemas.actuate11.rsse.GetTranslatedUserNamesResponseE();
		return wrappedElement;
	}

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
			com.actuate.schemas.actuate11.rsse.StopResponseE param, boolean optimizeContent,
			javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {
		try {
			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();

			emptyEnvelope.getBody().addChild(
					param.getOMElement(com.actuate.schemas.actuate11.rsse.StopResponseE.MY_QNAME, factory));

			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}
	}

	private com.actuate.schemas.actuate11.rsse.StopResponseE wrapstop() {
		com.actuate.schemas.actuate11.rsse.StopResponseE wrappedElement = new com.actuate.schemas.actuate11.rsse.StopResponseE();
		return wrappedElement;
	}

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
			com.actuate.schemas.actuate11.rsse.AuthenticateResponseE param, boolean optimizeContent,
			javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {
		try {
			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();

			emptyEnvelope.getBody().addChild(
					param.getOMElement(com.actuate.schemas.actuate11.rsse.AuthenticateResponseE.MY_QNAME, factory));

			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}
	}

	private com.actuate.schemas.actuate11.rsse.AuthenticateResponseE wrapauthenticate() {
		com.actuate.schemas.actuate11.rsse.AuthenticateResponseE wrappedElement = new com.actuate.schemas.actuate11.rsse.AuthenticateResponseE();
		return wrappedElement;
	}

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
			com.actuate.schemas.actuate11.rsse.SelectUsersResponseE param, boolean optimizeContent,
			javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {
		try {
			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();

			emptyEnvelope.getBody().addChild(
					param.getOMElement(com.actuate.schemas.actuate11.rsse.SelectUsersResponseE.MY_QNAME, factory));

			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}
	}

	private com.actuate.schemas.actuate11.rsse.SelectUsersResponseE wrapselectUsers() {
		com.actuate.schemas.actuate11.rsse.SelectUsersResponseE wrappedElement = new com.actuate.schemas.actuate11.rsse.SelectUsersResponseE();
		return wrappedElement;
	}

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
			com.actuate.schemas.actuate11.rsse.GetConnectionPropertiesResponseE param, boolean optimizeContent,
			javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {
		try {
			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();

			emptyEnvelope.getBody().addChild(
					param.getOMElement(com.actuate.schemas.actuate11.rsse.GetConnectionPropertiesResponseE.MY_QNAME,
							factory));

			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}
	}

	private com.actuate.schemas.actuate11.rsse.GetConnectionPropertiesResponseE wrapgetConnectionProperties() {
		com.actuate.schemas.actuate11.rsse.GetConnectionPropertiesResponseE wrappedElement = new com.actuate.schemas.actuate11.rsse.GetConnectionPropertiesResponseE();
		return wrappedElement;
	}

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
			com.actuate.schemas.actuate11.rsse.GetUsersToNotifyResponseE param, boolean optimizeContent,
			javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {
		try {
			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();

			emptyEnvelope.getBody().addChild(
					param.getOMElement(com.actuate.schemas.actuate11.rsse.GetUsersToNotifyResponseE.MY_QNAME, factory));

			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}
	}

	private com.actuate.schemas.actuate11.rsse.GetUsersToNotifyResponseE wrapgetUsersToNotify() {
		com.actuate.schemas.actuate11.rsse.GetUsersToNotifyResponseE wrappedElement = new com.actuate.schemas.actuate11.rsse.GetUsersToNotifyResponseE();
		return wrappedElement;
	}

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
			com.actuate.schemas.actuate11.rsse.DoesUserExistResponseE param, boolean optimizeContent,
			javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {
		try {
			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();

			emptyEnvelope.getBody().addChild(
					param.getOMElement(com.actuate.schemas.actuate11.rsse.DoesUserExistResponseE.MY_QNAME, factory));

			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}
	}

	private com.actuate.schemas.actuate11.rsse.DoesUserExistResponseE wrapdoesUserExist() {
		com.actuate.schemas.actuate11.rsse.DoesUserExistResponseE wrappedElement = new com.actuate.schemas.actuate11.rsse.DoesUserExistResponseE();
		return wrappedElement;
	}

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
			com.actuate.schemas.actuate11.rsse.StartResponseE param, boolean optimizeContent,
			javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {
		try {
			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();

			emptyEnvelope.getBody().addChild(
					param.getOMElement(com.actuate.schemas.actuate11.rsse.StartResponseE.MY_QNAME, factory));

			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}
	}

	private com.actuate.schemas.actuate11.rsse.StartResponseE wrapstart() {
		com.actuate.schemas.actuate11.rsse.StartResponseE wrappedElement = new com.actuate.schemas.actuate11.rsse.StartResponseE();
		return wrappedElement;
	}

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
			com.actuate.schemas.actuate11.rsse.PassThroughResponseE param, boolean optimizeContent,
			javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {
		try {
			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();

			emptyEnvelope.getBody().addChild(
					param.getOMElement(com.actuate.schemas.actuate11.rsse.PassThroughResponseE.MY_QNAME, factory));

			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}
	}

	private com.actuate.schemas.actuate11.rsse.PassThroughResponseE wrappassThrough() {
		com.actuate.schemas.actuate11.rsse.PassThroughResponseE wrappedElement = new com.actuate.schemas.actuate11.rsse.PassThroughResponseE();
		return wrappedElement;
	}

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
			com.actuate.schemas.actuate11.rsse.SelectGroupsResponseE param, boolean optimizeContent,
			javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {
		try {
			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();

			emptyEnvelope.getBody().addChild(
					param.getOMElement(com.actuate.schemas.actuate11.rsse.SelectGroupsResponseE.MY_QNAME, factory));

			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}
	}

	private com.actuate.schemas.actuate11.rsse.SelectGroupsResponseE wrapselectGroups() {
		com.actuate.schemas.actuate11.rsse.SelectGroupsResponseE wrappedElement = new com.actuate.schemas.actuate11.rsse.SelectGroupsResponseE();
		return wrappedElement;
	}

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
			com.actuate.schemas.actuate11.rsse.DoesRoleExistResponseE param, boolean optimizeContent,
			javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {
		try {
			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();

			emptyEnvelope.getBody().addChild(
					param.getOMElement(com.actuate.schemas.actuate11.rsse.DoesRoleExistResponseE.MY_QNAME, factory));

			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}
	}

	private com.actuate.schemas.actuate11.rsse.DoesRoleExistResponseE wrapdoesRoleExist() {
		com.actuate.schemas.actuate11.rsse.DoesRoleExistResponseE wrappedElement = new com.actuate.schemas.actuate11.rsse.DoesRoleExistResponseE();
		return wrappedElement;
	}

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
			com.actuate.schemas.actuate11.rsse.SelectRolesResponseE param, boolean optimizeContent,
			javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {
		try {
			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();

			emptyEnvelope.getBody().addChild(
					param.getOMElement(com.actuate.schemas.actuate11.rsse.SelectRolesResponseE.MY_QNAME, factory));

			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}
	}

	private com.actuate.schemas.actuate11.rsse.SelectRolesResponseE wrapselectRoles() {
		com.actuate.schemas.actuate11.rsse.SelectRolesResponseE wrappedElement = new com.actuate.schemas.actuate11.rsse.SelectRolesResponseE();
		return wrappedElement;
	}

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
			com.actuate.schemas.actuate11.rsse.GetUserPropertiesResponseE param, boolean optimizeContent,
			javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {
		try {
			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();

			emptyEnvelope.getBody()
					.addChild(
							param.getOMElement(com.actuate.schemas.actuate11.rsse.GetUserPropertiesResponseE.MY_QNAME,
									factory));

			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}
	}

	private com.actuate.schemas.actuate11.rsse.GetUserPropertiesResponseE wrapgetUserProperties() {
		com.actuate.schemas.actuate11.rsse.GetUserPropertiesResponseE wrappedElement = new com.actuate.schemas.actuate11.rsse.GetUserPropertiesResponseE();
		return wrappedElement;
	}

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
			com.actuate.schemas.actuate11.rsse.DoesGroupExistResponseE param, boolean optimizeContent,
			javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {
		try {
			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();

			emptyEnvelope.getBody().addChild(
					param.getOMElement(com.actuate.schemas.actuate11.rsse.DoesGroupExistResponseE.MY_QNAME, factory));

			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}
	}

	private com.actuate.schemas.actuate11.rsse.DoesGroupExistResponseE wrapdoesGroupExist() {
		com.actuate.schemas.actuate11.rsse.DoesGroupExistResponseE wrappedElement = new com.actuate.schemas.actuate11.rsse.DoesGroupExistResponseE();
		return wrappedElement;
	}

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
			com.actuate.schemas.actuate11.rsse.GetTranslatedRoleNamesResponseE param, boolean optimizeContent,
			javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {
		try {
			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();

			emptyEnvelope.getBody().addChild(
					param.getOMElement(com.actuate.schemas.actuate11.rsse.GetTranslatedRoleNamesResponseE.MY_QNAME,
							factory));

			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}
	}

	private com.actuate.schemas.actuate11.rsse.GetTranslatedRoleNamesResponseE wrapgetTranslatedRoleNames() {
		com.actuate.schemas.actuate11.rsse.GetTranslatedRoleNamesResponseE wrappedElement = new com.actuate.schemas.actuate11.rsse.GetTranslatedRoleNamesResponseE();
		return wrappedElement;
	}

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
			com.actuate.schemas.actuate11.rsse.GetUserACLResponseE param, boolean optimizeContent,
			javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {
		try {
			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();

			emptyEnvelope.getBody().addChild(
					param.getOMElement(com.actuate.schemas.actuate11.rsse.GetUserACLResponseE.MY_QNAME, factory));

			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw createAxisFault(e);
		}
	}

	private com.actuate.schemas.actuate11.rsse.GetUserACLResponseE wrapgetUserACL() {
		com.actuate.schemas.actuate11.rsse.GetUserACLResponseE wrappedElement = new com.actuate.schemas.actuate11.rsse.GetUserACLResponseE();
		return wrappedElement;
	}

	/**
	 * get the default envelope
	 */
	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory) {
		return factory.getDefaultEnvelope();
	}

	private java.lang.Object fromOM(org.apache.axiom.om.OMElement param, java.lang.Class type,
			java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault {

		try {

			if (com.actuate.schemas.actuate11.rsse.GetTranslatedUserNamesE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.GetTranslatedUserNamesE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.GetTranslatedUserNamesResponseE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.GetTranslatedUserNamesResponseE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.StopE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.StopE.Factory.parse(param.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.StopResponseE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.StopResponseE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.AuthenticateE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.AuthenticateE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.AuthenticateResponseE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.AuthenticateResponseE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.SelectUsersE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.SelectUsersE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.SelectUsersResponseE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.SelectUsersResponseE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.GetConnectionPropertiesE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.GetConnectionPropertiesE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.GetConnectionPropertiesResponseE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.GetConnectionPropertiesResponseE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.GetUsersToNotifyE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.GetUsersToNotifyE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.GetUsersToNotifyResponseE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.GetUsersToNotifyResponseE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.DoesUserExistE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.DoesUserExistE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.DoesUserExistResponseE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.DoesUserExistResponseE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.StartE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.StartE.Factory
						.parse(param.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.StartResponseE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.StartResponseE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.PassThroughE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.PassThroughE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.PassThroughResponseE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.PassThroughResponseE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.SelectGroupsE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.SelectGroupsE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.SelectGroupsResponseE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.SelectGroupsResponseE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.DoesRoleExistE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.DoesRoleExistE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.DoesRoleExistResponseE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.DoesRoleExistResponseE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.SelectRolesE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.SelectRolesE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.SelectRolesResponseE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.SelectRolesResponseE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.GetUserPropertiesE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.GetUserPropertiesE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.GetUserPropertiesResponseE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.GetUserPropertiesResponseE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.DoesGroupExistE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.DoesGroupExistE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.DoesGroupExistResponseE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.DoesGroupExistResponseE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.GetTranslatedRoleNamesE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.GetTranslatedRoleNamesE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.GetTranslatedRoleNamesResponseE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.GetTranslatedRoleNamesResponseE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.GetUserACLE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.GetUserACLE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

			if (com.actuate.schemas.actuate11.rsse.GetUserACLResponseE.class.equals(type)) {

				return com.actuate.schemas.actuate11.rsse.GetUserACLResponseE.Factory.parse(param
						.getXMLStreamReaderWithoutCaching());

			}

		} catch (java.lang.Exception e) {
			throw createAxisFault(e);
		}
		return null;
	}

	/**
	 * A utility method that copies the namepaces from the SOAPEnvelope
	 */
	private java.util.Map getEnvelopeNamespaces(org.apache.axiom.soap.SOAPEnvelope env) {
		java.util.Map returnMap = new java.util.HashMap();
		java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
		while (namespaceIterator.hasNext()) {
			org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator.next();
			returnMap.put(ns.getPrefix(), ns.getNamespaceURI());
		}
		return returnMap;
	}

	private org.apache.axis2.AxisFault createAxisFault(java.lang.Exception e) {
		org.apache.axis2.AxisFault f;
		Throwable cause = e.getCause();
		if (cause != null) {
			f = new org.apache.axis2.AxisFault(e.getMessage(), cause);
		} else {
			f = new org.apache.axis2.AxisFault(e.getMessage());
		}

		return f;
	}

}// end of class
