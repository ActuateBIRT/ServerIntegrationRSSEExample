package com.actuate.rsse.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.activation.DataHandler;

import org.apache.axis2.AxisFault;

import com.actuate.schemas.actuate11.rsse.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;


// ##########################################################################
// ################ class SampleRSSE ########################################
// ##########################################################################

public class SampleRSSE implements SampleRSSEInterface {
	
	// volume contents properties
	private static String USER_ADMINISTRATOR = "Administrator";
	private static String ROLE_ADMINISTRATOR = "Administrators";
	private static String ROLE_OPERATOR = "Operator";
	private static String ROLE_ALL = "All";
	private static boolean LICENSE_OPTIONS_EXTERNALIZED = false;
	private static Set<String> LICENSE_OPTIONS = new HashSet<String>( Arrays.asList(
		"BIRTReport",
		"BIRTInteractiveViewer",
		"ReportStudio",
		"MultiApp",
		"BIRTPageSecure",
		"Dashboards",
		"InteractiveCrosstabs",
		"eReportDataConnector",
		"AccessiblePDF"
	));
	private static String MAIL_DOMAIN = "@xyz.com";
	private static String JSON_STORAGE_FILE_NAME = "SampleRSSE.json";
	private static String DATA_FOLDER = "C:";
	private static String LOG_FILE_NAME = "SampleRSSE.log";
	private static String PROPERTIES_FILE_NAME = "SampleRSSE.properties";
	private static File LOG_FILE;

	// helper class members
	private static String serverPath;
	private static String version;
	private static PrintStream logStr;
	private static DateFormat df;
	
	// main volume contents storage
	private static volatile Map<String, RSSEVolume> volumes;
	
	// static block, which is executed only once when the class
	// gets loaded for the first time;
	// common initializations can be moved here from start() method
	static {
		SampleRSSE.volumes = new ConcurrentHashMap<String, RSSEVolume>();
	}

	// main class member functions
	synchronized private static void log(String strToLog) {
		if(SampleRSSE.df == null) {
			SampleRSSE.df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
		}
		SampleRSSE.df.format(new Date(System.currentTimeMillis()));
		System.out.println(SampleRSSE.df.format(new Date(System.currentTimeMillis())) + ": " + strToLog);
		//logStr.flush();
	}
	
	synchronized private static void log(Exception e, String strToLog) {
		if(SampleRSSE.df == null) {
			SampleRSSE.df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
		}
		System.out.println(SampleRSSE.df.format(new Date(System.currentTimeMillis())) + ": ERROR: " + strToLog);
		e.printStackTrace(logStr);
		//logStr.flush();
	}
	
	synchronized public RSSEVolume getVolume(String volumeName) {
		String key = volumeName.toUpperCase();
		if(SampleRSSE.volumes.containsKey(key)) {
			return SampleRSSE.volumes.get(key);
		}
		RSSEVolume vol = new RSSEVolume(volumeName);
		SampleRSSE.volumes.put(key, vol);
		return vol;
	}
	
	/**
	 * Make a list of externalized user properties supported in
	 * the current implementation. 
	 */
	private static ArrayOfString getExternalProperties() {
		Set<String> extProperties = new HashSet<String>();
		extProperties.add("EmailAddress");
		// Note that Description is not supported in iHub3 Fix1 and prior releases.
		// It has to be commented out in case if you want to use the sample there,
		// the server (iHub) will fail to initialize RSSE service otherwise.
		extProperties.add("Description");
		extProperties.add("HomeFolder");
		extProperties.add("ViewPreference");
		extProperties.add("MaxJobPriority");
//		extProperties.add("MaxNotices");
		extProperties.add("SendNoticeForSuccess");
		extProperties.add("SendNoticeForFailure");
		extProperties.add("SuccessNoticeExpiration");
		extProperties.add("FailureNoticeExpiration");
		extProperties.add("SendEmailForSuccess");
		extProperties.add("SendEmailForFailure");
		extProperties.add("AttachReportInEmail");
		extProperties.add("DefaultObjectPrivileges");
		extProperties.add("ChannelSubscriptionList");

		if (SampleRSSE.LICENSE_OPTIONS_EXTERNALIZED) {
			extProperties.add("LicenseOptions");
		}

		ArrayOfString array = new ArrayOfString();
		array.setString(extProperties.toArray(new String[] {}));
		return array;
	}
	
	// ##########################################################################
	// ############# RSSE interface methods #####################################
	// ##########################################################################
	
	/**
	 * Method start() is called once during iHub start. This is different from older releases when
	 * start() is called every time a volume is brought online.
	 * Volume parameter is irrelevant because the service is started once for all volumes.
	 * In the current implementation, most of the common initialization are done in start() method.
	 * Most/some of the initializations can be moved to static{} block if necessary.
	 */
	public com.actuate.schemas.actuate11.rsse.StartResponseE start(com.actuate.schemas.actuate11.rsse.StartE reqE) throws AxisFault {
		StartResponseE resE = new StartResponseE();
		StartResponse res = new StartResponse();
		res.setConnectionPropertyExternal(false);
		res.setExternalProperties(SampleRSSE.getExternalProperties());
		res.setIntegrationLevel(SampleRSSEConst.RSSE_INTEGRATION_LEVEL);
		res.setRSSEVersion(SampleRSSEConst.RSSE_VERSION);
		res.setSelectGroupsOfUser(false);
		res.setSelectUsersOfRole(true);
		res.setSupportGetTranslatedUserNames(true);
		res.setUserACLExternal(false);
		resE.setStartResponse(res);
		SampleRSSE.log("start()");
		
		// check if already started
		if(SampleRSSE.version != null) {
			return resE;
		}
		
		// read RSSE driver properties from config file
		try {
			InputStream propStream = this.getClass().getClassLoader().getResourceAsStream(SampleRSSE.PROPERTIES_FILE_NAME);
			if(propStream == null) {
				SampleRSSE.throwException("Property file " + SampleRSSE.PROPERTIES_FILE_NAME + " not found.");
			} else {
				Properties props = new Properties();
				props.load(this.getClass().getClassLoader().getResourceAsStream(SampleRSSE.PROPERTIES_FILE_NAME));
				if(props.size() <= 0) {
					SampleRSSE.throwException("Cannot read from " + SampleRSSE.PROPERTIES_FILE_NAME);
				}
				for (String p : props.stringPropertyNames()) {
					if (p.compareToIgnoreCase("USER_ADMINISTRATOR") == 0)
						SampleRSSE.USER_ADMINISTRATOR = (String) props.getProperty(p);
					else if (p.compareToIgnoreCase("ADMIN_ROLE") == 0)
						SampleRSSE.ROLE_ADMINISTRATOR = (String) props.getProperty(p);
					else if (p.compareToIgnoreCase("ROLE_OPERATOR") == 0)
						SampleRSSE.ROLE_OPERATOR = (String) props.getProperty(p);
					else if (p.compareToIgnoreCase("ROLE_ALL") == 0)
						SampleRSSE.ROLE_ALL = (String) props.getProperty(p);
					else if (p.compareToIgnoreCase("LICENSE_OPTIONS_EXTERNALIZED") == 0)
						SampleRSSE.LICENSE_OPTIONS_EXTERNALIZED = Boolean.parseBoolean((String) props.getProperty(p));
					else if (p.compareToIgnoreCase("LICENSE_OPTIONS") == 0)
						SampleRSSE.LICENSE_OPTIONS = new HashSet<String>(Arrays.asList(props.getProperty(p).toString()));
					else if (p.compareToIgnoreCase("DATA_FOLDER") == 0)
						SampleRSSE.DATA_FOLDER = (String) props.getProperty(p);
					else if (p.compareToIgnoreCase("LOG_FILE_NAME") == 0)
						SampleRSSE.LOG_FILE_NAME = (String) props.getProperty(p);
					else if (p.compareToIgnoreCase("JSON_STORAGE_FILE_NAME") == 0)
						SampleRSSE.JSON_STORAGE_FILE_NAME = (String) props.getProperty(p);
				}
			}
		} catch (IOException e) {
			SampleRSSE.throwException(e, "I/O exception while reading property file");
		} catch (Exception e) {
			SampleRSSE.throwException(e, "General exception while reading property file");
		}
		
		SampleRSSE.serverPath = reqE.getStart().getServerHome();
		SampleRSSE.version = reqE.getStart().getVersion();
		
		try {
			// To create a log file at the location provided by iHub switch the following two lines
//			SampleRSSE.LOG_FILE = new File(reqE.getStart().getLogFile());
			SampleRSSE.LOG_FILE = new File(SampleRSSE.DATA_FOLDER + "/" + SampleRSSE.LOG_FILE_NAME);
			if(SampleRSSE.LOG_FILE.exists()) {
				SampleRSSE.LOG_FILE.renameTo(new File(SampleRSSE.DATA_FOLDER + "/" + SampleRSSE.LOG_FILE_NAME + ".bak"));
			}
			SampleRSSE.LOG_FILE.createNewFile();
			SampleRSSE.LOG_FILE.setWritable(true);
			SampleRSSE.logStr = new PrintStream(SampleRSSE.DATA_FOLDER + "/" + SampleRSSE.LOG_FILE_NAME);
			System.setOut(SampleRSSE.logStr);
			System.setErr(SampleRSSE.logStr);
			SampleRSSE.df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
			SampleRSSE.log("Start log file");
		} catch (FileNotFoundException e) {
			SampleRSSE.log(e, "FileNotFoundException while creating LOG_FILE file");
		} catch (IOException e) {
			SampleRSSE.log(e, "I/O exception while creating LOG_FILE file");
		}

		// read users with all properties and roles from a JSON file, if provided
		this.readDataFromFile();

		if (SampleRSSE.LICENSE_OPTIONS_EXTERNALIZED) {
			SampleRSSE.log("User license options are externalized");
		}
		
		// Uncomment the following line to add sample contents
//		this.addSampleContents();

		SampleRSSE.log("start() complete");
		return resE;
	}
	
	/**
	 * Method stop() is called once when iHub is shutting down in iHub3 Fix1 and prior
	 * iHub builds. This method is not called in releases after iHub3 Fix1.
	 */
	public com.actuate.schemas.actuate11.rsse.StopResponseE stop(com.actuate.schemas.actuate11.rsse.StopE reqE) throws AxisFault {
		StopResponseE resE = new StopResponseE();
		resE.setStopResponse(new StopResponse());
		SampleRSSE.log("stop() complete");
		return resE;
	}
	
	/**
	 * PathThrough is a mechanism that allows IDAPI client to invoke RSSE driver functionality directly.
	 * The corresponding IDAPI request is CallOpenSecurityLibrary. Any data passed in this request is not
	 * interpreted by iHub in any way.
	 * The current example demonstrates several things:<br>
	 * - A way of altering externalized contents, which is not possible through IDAPI otherwise: deleting
	 * a user. A more complex operations like adding new users or updating user properties can be
	 * implemented in a similar way.<br>
	 * - Refreshing internal RSSE data. The driver reads data from a JSON file during iHub startup
	 * and keeps it in memory unchanged even if the external JSON file changes. The pass-through mechanism
	 * allows re-reading the data from file without restarting RSSE web service.<br>
	 * - Saving in-memory volume contents to a JSON file. You have to call it in order to save contents
	 * stored in memory if they have been modified.
	 */
	public com.actuate.schemas.actuate11.rsse.PassThroughResponseE passThrough(
			com.actuate.schemas.actuate11.rsse.PassThroughE reqE) throws AxisFault {
		String gsonPassThroughData = reqE.getPassThrough().getInput();
		if (gsonPassThroughData == null || gsonPassThroughData.length() <= 0) {
			SampleRSSE.throwException("PassThrough received an empty string");
		}
		String message = "Nothing was changed";
		try {
			// parse JSON string
			JsonElement jElement = new JsonParser().parse(gsonPassThroughData);
			JsonObject jObject = jElement.getAsJsonObject();
			JsonElement operation = jObject.getAsJsonPrimitive(SampleRSSEConst.PASSTHROUGH_OPERATION);
			
			if(operation == null) {
				SampleRSSE.throwException("Parameter \"operation\" is required");
			}
			
			String operationName = operation.getAsString();
			if(operationName.equalsIgnoreCase(SampleRSSEConst.PASSTHROUGH_OPERATION_DELETEUSER)) {
				JsonElement volume = jObject.get(SampleRSSEConst.PASSTHROUGH_VOLUME);
				JsonElement user = jObject.get(SampleRSSEConst.PASSTHROUGH_USER);
				if(volume == null || user == null) {
					SampleRSSE.throwException(
						"One or more required parameters are missing:" +
						"\nvolume=" + volume +
						"\nuser=" + user);
				}
				String userName = user.getAsString();
				String volumeName = volume.getAsString();
				if(this.getVolume(volumeName.toUpperCase()).getUsers().containsKey(userName.toUpperCase())) {
					this.getVolume(volumeName.toUpperCase()).getUsers().remove(userName.toUpperCase());
					message = "User " + user + " has been deleted";
				} else {
					message = "User " + user + " does not exist";
				}
			} else if(operationName.equalsIgnoreCase(SampleRSSEConst.PASSTHROUGH_OPERATION_REFRESH)) {
				this.readDataFromFile();
				message = "Loaded user data from file";
			} else if(operationName.equalsIgnoreCase(SampleRSSEConst.PASSTHROUGH_OPERATION_SAVE)) {
				this.saveDataToFile();
				message = "Saved user data to file";
			} else {
				SampleRSSE.throwException("Operation not supported: " + operation);
			}
		} catch (Exception e) {
			SampleRSSE.throwException(e, "Passthrough failed");
		}
		PassThroughResponseE resE = new PassThroughResponseE();
		PassThroughResponse res = new PassThroughResponse();
		res.setOutput(message);
		res.setReturnCode(0);
		resE.setPassThroughResponse(res);
		return resE;
	}

	/**
	 * Get translated user names
	 */
	public com.actuate.schemas.actuate11.rsse.GetTranslatedUserNamesResponseE getTranslatedUserNames(
			String volume,
			com.actuate.schemas.actuate11.rsse.GetTranslatedUserNamesE getTransUserNames) throws AxisFault {
		SampleRSSE.log("getTranslatedUserNames() for volume " + volume);
		GetTranslatedUserNamesResponse res = new GetTranslatedUserNamesResponse();
		TranslatedUserNames names = new TranslatedUserNames();
		names.setAdministrator(SampleRSSE.USER_ADMINISTRATOR);
		res.setTranslatedUserNames(names);
		GetTranslatedUserNamesResponseE resE = new GetTranslatedUserNamesResponseE();
		resE.setGetTranslatedUserNamesResponse(res);
		return resE;
	}

	/**
	 * Authenticate a user. Authentication is done using:<br>
	 * - Extended credentials first. The extended credentials are expected to contain encrypted user password passed by IPSE.<br>
	 * - If the extended credentials are null, then the authentication is done using regular password.
	 */
	public com.actuate.schemas.actuate11.rsse.AuthenticateResponseE authenticate(
			String volume,
			com.actuate.schemas.actuate11.rsse.AuthenticateE reqE) throws AxisFault {
		String userName = reqE.getAuthenticate().getUser();
		SampleRSSE.log("authenticate() user " + volume + "/" + userName);
		if (!this.userExists(volume, userName)) {
			SampleRSSE.throwException("authenticate(): User " + userName + " doesn't exist");
		}

		DataHandler handler = reqE.getAuthenticate().getCredentials();
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		if(handler != null) { 
			try {
				handler.writeTo(byteStream);
			} catch (Exception e) {
				SampleRSSE.throwException(e, "authenticate(): Failed to get user extended credentials from request");
			}
		}
		String passwordInStorage = nullIfEmpty(this.getVolume(volume).getUsers().get(userName.toUpperCase()).getPassword());
		if(byteStream.size() > 0) { // using user extended credentials
			byte[] credentials = byteStream.toByteArray();
		    if(!SampleRSSE.verifyEncryptedPassword(credentials, passwordInStorage.getBytes())) {
		    	// uncomment to see credentials in log file
//		    	SampleRSSE.log("authenticate(): Extended credentials do not match for user " + volume + "/" + userName + ": expected " + passwordInStorage + " , but received " + (new String(credentials)));
		    	SampleRSSE.throwException("authenticate(): Extended credentials do not match for user " + volume + "/" + userName);
		    }
		} else { // using regular password
			String passwordInRequest = nullIfEmpty(reqE.getAuthenticate().getPassword());
			if (!((passwordInRequest == null && passwordInStorage == null) || (passwordInRequest != null && passwordInRequest.equals(passwordInStorage)))) {
				// uncomment to see password in log file
//				SampleRSSE.log("authenticate(): Wrong password for user " + volume + "/" + userName + ": expected " + passwordInStorage + ", but received " + passwordInRequest);
				SampleRSSE.throwException("authenticate(): Wrong password for user " + volume + "/" + userName);
			}
		}
		
		AuthenticateResponseE resE = new AuthenticateResponseE();
		AuthenticateResponse res = new AuthenticateResponse();
		resE.setAuthenticateResponse(res);
		if (reqE.getAuthenticate().getUserSetting()) {
			res.setUserAndProperties(this.getVolume(volume.toUpperCase()).getUsers().get(userName.toUpperCase()).getUserAndProperties());
		}
		return resE;
	}
	
	/**
	 * Select users in a volume using SmartSearch conditions or user group name.
	 */
	public com.actuate.schemas.actuate11.rsse.SelectUsersResponseE selectUsers(
			String volume,
			com.actuate.schemas.actuate11.rsse.SelectUsersE reqE) throws AxisFault {
		SelectUsersResponseE resE = new SelectUsersResponseE();
		SelectUsersResponse res = new SelectUsersResponse();
//		try {
		int maxNumber = reqE.getSelectUsers().getFetchSize();
		Map<String, RSSEUser> users = this.getVolume(volume.toUpperCase()).getUsers();
		Set<String> list = new HashSet<String>();
		if(reqE.getSelectUsers().getSelectUsersChoice_type0().getQueryPattern() != null) { // select by search
			String query = reqE.getSelectUsers().getSelectUsersChoice_type0().getQueryPattern();
			String regex = convertQueryToRegex(query);
			Iterator<String> iterator = users.keySet().iterator();
			while (iterator.hasNext()) {
				String s = iterator.next();
				if(s.matches(regex)) {
					// if the key matches query then add actual user name to response
					list.add(users.get(s).getName());
					if(maxNumber >= 0 && list.size() >= maxNumber) {
						break;
					}
				}
			}
		} else if(reqE.getSelectUsers().getSelectUsersChoice_type0().getRoleName() != null) { // select by role name
			String role = reqE.getSelectUsers().getSelectUsersChoice_type0().getRoleName().toUpperCase();
			if (!this.roleExists(volume, role)) {
				SampleRSSE.throwException("Role doesn't exist: " + role);
			}
			for(RSSEUser user: users.values()) {
				if(user.getRoles() != null) {
					for(String r: user.getRoles()) {
						if(role.equals(r.toUpperCase())) {
							list.add(user.getName());
							break;
						}
					}
					if(maxNumber >= 0 && list.size() >= maxNumber) {
						break;
					}
				}
			}
		}

		res.setTotalCount(list.size());
		ArrayOfString array = new ArrayOfString();
		array.setString(list.toArray(new String[] {}));
		res.setUsers(array);
		resE.setSelectUsersResponse(res);
//		} catch (Exception e) {
//			SampleRSSE.throwException(e, "Caught unexpected exception");
//		}
		return resE;
	}

	/**
	 * Current implementation does not support external connection properties. 
	 */
	public com.actuate.schemas.actuate11.rsse.GetConnectionPropertiesResponseE getConnectionProperties(
			com.actuate.schemas.actuate11.rsse.GetConnectionPropertiesE reqE) throws AxisFault {
		SampleRSSE.throwException("Connection properties are not externalized");
		return new GetConnectionPropertiesResponseE();
	}

	/**
	 * In current implementation, this method does not set any user notifications.
	 */
	public com.actuate.schemas.actuate11.rsse.GetUsersToNotifyResponseE getUsersToNotify(
			String volume,
			com.actuate.schemas.actuate11.rsse.GetUsersToNotifyE reqE) throws AxisFault {
		GetUsersToNotifyResponseE resE = new GetUsersToNotifyResponseE();
		GetUsersToNotifyResponse res = new GetUsersToNotifyResponse();
		res.setUsers(new ArrayOfString());
		resE.setGetUsersToNotifyResponse(res);
		return resE;
	}

	/**
	 * Check if a user exists in the specific volume.
	 */
	public com.actuate.schemas.actuate11.rsse.DoesUserExistResponseE doesUserExist(
			String volume,
			com.actuate.schemas.actuate11.rsse.DoesUserExistE reqE) throws AxisFault {
		String userName = reqE.getDoesUserExist().getUserName();
		DoesUserExistResponse res = new DoesUserExistResponse();
		boolean result = this.userExists(volume, userName);
		SampleRSSE.log("doesUserExist() " + volume + "/" + userName + ": " + result);
		res.setExists(result);
		DoesUserExistResponseE resE = new DoesUserExistResponseE();
		resE.setDoesUserExistResponse(res);
		return resE;
	}

	/**
	 * Old groups are not supported in iHub, hence throw an exception. 
	 */
	public com.actuate.schemas.actuate11.rsse.SelectGroupsResponseE selectGroups(
			String volume,
			com.actuate.schemas.actuate11.rsse.SelectGroupsE reqE) throws AxisFault {
		SampleRSSE.throwException("Groups are not supported iHub");
		return new SelectGroupsResponseE();
	}

	/**
	 * Check if a user group (role) exists in the specific volume.
	 */
	public com.actuate.schemas.actuate11.rsse.DoesRoleExistResponseE doesRoleExist(
			String volume,
			com.actuate.schemas.actuate11.rsse.DoesRoleExistE reqE) throws AxisFault {
		String roleName = reqE.getDoesRoleExist().getRoleName();
		DoesRoleExistResponse res = new DoesRoleExistResponse();
		boolean result = this.roleExists(volume, roleName);
		SampleRSSE.log("doesRoleExist() " + volume + "/" + roleName + " : " + result);
		res.setExists(result);
		DoesRoleExistResponseE resE = new DoesRoleExistResponseE();
		resE.setDoesRoleExistResponse(res);
		return resE;
	}

	/**
	 * Select roles in the specific volume using SmartSearch expressions or
	 * by user name.
	 */
	public com.actuate.schemas.actuate11.rsse.SelectRolesResponseE selectRoles(
			String volume,
			com.actuate.schemas.actuate11.rsse.SelectRolesE reqE) throws AxisFault {
		SelectRolesResponseE resE = new SelectRolesResponseE();
		SelectRolesResponse res = new SelectRolesResponse();
//		try {
		int maxNumber = reqE.getSelectRoles().getFetchSize();
		Map<String, String> roles = this.getVolume(volume.toUpperCase()).getRoles();
		Set<String> list = new HashSet<String>();
		if(reqE.getSelectRoles().getSelectRolesChoice_type0().getQueryPattern() != null) { //select by search
			String query = reqE.getSelectRoles().getSelectRolesChoice_type0().getQueryPattern();
			String regex = convertQueryToRegex(query);
			Iterator<String> iterator = roles.keySet().iterator();
			while (iterator.hasNext()) {
				String s = iterator.next();
				if (s.matches(regex)) {
					// if the key matches the pattern then add the real role name to response 
					list.add(roles.get(s));
					if (list.size() >= maxNumber) {
						break;
					}
				}
			}
		} else if (reqE.getSelectRoles().getSelectRolesChoice_type0().getUserName() != null) { // select by user name
			String user = reqE.getSelectRoles().getSelectRolesChoice_type0().getUserName().toUpperCase();
			if (!this.userExists(volume, user)) {
				SampleRSSE.throwException("User doesn't exist: " + user);
			}
			Set<String> userRoles = this.getVolume(volume.toUpperCase()).getUsers().get(user.toUpperCase()).getRoles();
			if(userRoles != null) {
				for(String r: userRoles) {
					list.add(roles.get(r.toUpperCase()));
					if(maxNumber >= 0 && list.size() >= maxNumber) {
						break;
					}
				}
			}
		}
		// System.out.println("List: " + list);
		res.setTotalCount(list.size());
		ArrayOfString array = new ArrayOfString();
		array.setString(list.toArray(new String[] {}));
		res.setRoles(array);
		resE.setSelectRolesResponse(res);
//		} catch (Exception e) {
//			SampleRSSE.throwException(e, "Caught unexpected exception");
//		}
		return resE;
	}

	/**
	 * Get externalized user properties.
	 * Currently returns all available properties independently of requested in ResultDef.
	 */
	public com.actuate.schemas.actuate11.rsse.GetUserPropertiesResponseE getUserProperties(
			String volume,
			com.actuate.schemas.actuate11.rsse.GetUserPropertiesE reqE) throws AxisFault {
		// TODO: add ResultDef handling
		String[] users = reqE.getGetUserProperties().getUsers().getString();
		List<UserAndProperties> props = new ArrayList<UserAndProperties>();
		for (int i = 0; i < users.length; i++) {
			if (!this.userExists(volume, users[i])) {
				SampleRSSE.throwException("User doesn't exist: " + users[i]);
			}
			props.add(this.getVolume(volume).getUsers().get(users[i].toUpperCase()).getUserAndProperties());
		}
		GetUserPropertiesResponseE resE = new GetUserPropertiesResponseE();
		GetUserPropertiesResponse res = new GetUserPropertiesResponse();
		ArrayOfUserAndProperties array = new ArrayOfUserAndProperties();
		array.setUserAndProperties(props.toArray(new UserAndProperties[] {}));
		res.setArrayOfUserAndProperties(array);
		resE.setGetUserPropertiesResponse(res);
		return resE;
	}

	/**
	 * Check if a group ("old" group, not user group) exists in a volume.
	 * Old groups are not supported in iHub. The current method should never be
	 * called by iHub/server, so it throws an exception now.
	 */
	public com.actuate.schemas.actuate11.rsse.DoesGroupExistResponseE doesGroupExist(
			String volume,
			com.actuate.schemas.actuate11.rsse.DoesGroupExistE reqE) throws AxisFault {
		SampleRSSE.throwException("Groups are not supported in iHub");
		return new DoesGroupExistResponseE();
	}

	/**
	 * Get translated role names. This method allows to map names of the following built-in user
	 * groups to names in internal driver storage: Administrators, Operator, All.
	 */
	public com.actuate.schemas.actuate11.rsse.GetTranslatedRoleNamesResponseE getTranslatedRoleNames(
			String volume,
			com.actuate.schemas.actuate11.rsse.GetTranslatedRoleNamesE reqE) throws AxisFault {
		SampleRSSE.log("getTranslatedRoleNames() for volume: " + volume);
		GetTranslatedRoleNamesResponseE resE = new GetTranslatedRoleNamesResponseE();
		GetTranslatedRoleNamesResponse res = new GetTranslatedRoleNamesResponse();
		TranslatedRoleNames roles = new TranslatedRoleNames();
		roles.setAdministrator(SampleRSSE.ROLE_ADMINISTRATOR);
		roles.setAll(SampleRSSE.ROLE_ALL);
		roles.setOperator(SampleRSSE.ROLE_OPERATOR);
		res.setTranslatedRoleNames(roles);
		resE.setGetTranslatedRoleNamesResponse(res);
		return resE;
	}

	/**
	 * Get file creation ACL for a user in the specific volume. The method throws
	 * an exception because ACL is not externalized in the current implementation.
	 * It can be easily added though since internal data storage already supports/
	 * contains required data structure: see user's privilegeTemplate.
	 */
	public com.actuate.schemas.actuate11.rsse.GetUserACLResponseE getUserACL(
			String volume,
			com.actuate.schemas.actuate11.rsse.GetUserACLE reqE) throws AxisFault {
		SampleRSSE.throwException("User ACLs are not externalized");
		return new GetUserACLResponseE();
	}
	
	
	// ##########################################################################
	// ################ class RSSEVolume ########################################
	// ##########################################################################
	
	class RSSEVolume {
		private String name;
		// The main lists of users,roles and channels. Due to IDAPI case-insensitivity,
		// all of the objects are identified by upper-case values. All other simple user,
		// role or channel lists in the current implementation are actual (non-upper-case)
		// values.
		private Map<String, RSSEUser> users;
		private Map<String, String> roles;
		private Map<String, String> channels;
		
		// constructors
		public RSSEVolume(String name) {
			this.setName(name.toUpperCase());
			this.setUsers(new HashMap<String, RSSEUser>());
			this.setRoles(new HashMap<String, String>());
			this.setChannels(new HashMap<String, String>());
			this.addRole(SampleRSSE.ROLE_ADMINISTRATOR);
			RSSEUser u = new RSSEUser(this.getName());
			u.setName(SampleRSSE.USER_ADMINISTRATOR);
			u.setPassword("");
			u.setEmailAddress(SampleRSSE.USER_ADMINISTRATOR + SampleRSSE.MAIL_DOMAIN);
			u.setSuccessNoticeExpiration(300L);
			u.setFailureNoticeExpiration(-1L);
			u.setSendEmailForSuccess(false);
			u.setSendNoticeForSuccess(false);
			u.setSendEmailForFailure(true);
			u.setSendNoticeForFailure(true);
			u.setAttachReportInEmail(true);
			u.setMaxJobPriority(1000L);
			u.setHomeFolder("/Home/" + SampleRSSE.USER_ADMINISTRATOR);
			this.addUser(u);
		}
		
		// getters/setters
		public String getName() {
			return this.name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Map<String, RSSEUser> getUsers() {
			return this.users;
		}
		public void setUsers(Map<String, RSSEUser> users) {
			this.users = users;
		}
		public void addUser(RSSEUser user) {
			if(!this.users.containsKey(user.getName().toUpperCase())) {
				// user names are cases insensitive, so store the key as uppercase
				this.users.put(user.getName().toUpperCase(), user);
			}
		}
		public Map<String, String> getRoles() {
			return this.roles;
		}
		public void setRoles(Map<String, String> roles) {
			this.roles = roles;
		}
		public String addRole(String role) {
			String key = role.toUpperCase();
			String added = null;
			if(!this.getRoles().containsKey(key)) {
				this.getRoles().put(key, role);
				added = key;
			}
			return added;
		}
		public Map<String, String> getChannels() {
			return this.channels;
		}
		public void setChannels(Map<String, String> channels) {
			this.channels = channels;
		}
		public String addChannel(String channel) {
			String key = channel.toUpperCase();
			String added = null;
			if(!this.getChannels().containsKey(key)) {
				this.getChannels().put(key, channel);
				added = key;
			}
			return added;
		}
	}

	
	// ##########################################################################
	// ################ class RSSEPermission ####################################
	// ##########################################################################
	
	private class RSSEPermission {
		private String volume;
		private String accessRight;
		private String roleName;
		private String userName;
		public RSSEPermission(String volume) {
			this.setVolume(volume.toUpperCase());
		}
		public RSSEPermission(String volume, Permission p) throws AxisFault {
			this.setVolume(volume);
			this.setAccessRight(p.getAccessRight());
			if(p.getPermissionChoice_type0().isRoleNameSpecified()) {
				String role = p.getPermissionChoice_type0().getRoleName();
				String key = role.toUpperCase();
				Map<String, String> roles = SampleRSSE.volumes.get(volume.toUpperCase()).getRoles();
				if(!roles.containsKey(key)) {
					SampleRSSE.throwException("Role " + role + " is not found on the main role list");
				}
				this.setRoleName(roles.get(key));
			} else {
				String user = p.getPermissionChoice_type0().getUserName();
				String key = user.toUpperCase();
				Map<String, RSSEUser> users = SampleRSSE.volumes.get(volume.toUpperCase()).getUsers();
				if(!users.containsKey(key)) {
					SampleRSSE.throwException("User " + user + " is not found on the main user list");
				}
				this.setRoleName(users.get(key).getName());
			}
		}
		public String getVolume() {
			return this.volume;
		}
		public void setVolume(String volume) {
			this.volume = volume;
		}
		public String getAccessRight() {
			return this.accessRight;
		}
		public void setAccessRight(String accessRight) {
			this.accessRight = accessRight;
		}
		public String getRoleName() {
			return this.roleName;
		}
		public void setRoleName(String roleName) throws AxisFault {
			Map<String, String> roles = SampleRSSE.volumes.get(this.getVolume()).getRoles();
			if(!roles.containsKey(roleName.toUpperCase())) {
				SampleRSSE.throwException("Role " + roleName + " is not found in the main role list");
			}
			this.roleName = roles.get(roleName.toUpperCase());
		}
		public String getUserName() {
			return this.userName;
		}
		public void setUserName(String userName) throws AxisFault {
			Map<String, RSSEUser> users = SampleRSSE.volumes.get(this.getVolume().toUpperCase()).getUsers();
			if(!users.containsKey(userName.toUpperCase())) {
				SampleRSSE.throwException("User " + userName + " is not found in the main user list");
			}
			this.userName = users.get(userName.toUpperCase()).getName();
		}
	}
	
	// ##########################################################################
	// ################ class RSSEUser ##########################################
	// ##########################################################################
	
	private class RSSEUser {

		// class members
		private String volume;
		private String name;
		private String password;
		private String emailAddress;
		private String homeFolder;
		private Set<String> licenseOptions;
		private String viewPreference;
		private Long maxJobPriority;
		private Boolean sendNoticeForSuccess;
		private Boolean sendNoticeForFailure;
		private Long successNoticeExpiration;
		private Long failureNoticeExpiration;
		private Boolean sendEmailForSuccess;
		private Boolean sendEmailForFailure;
		private Boolean attachReportInEmail;
		private List<RSSEPermission> privilegeTemplate;
		private Set<String> channels;
		private Set<String> roles;

		//constructors
		public RSSEUser(String volume) {
			this.setVolume(volume.toUpperCase());
		}
		public RSSEUser(String volume, UserAndProperties p, String password) throws AxisFault {
			this.setVolume(volume.toUpperCase());
			
			User u = p.getUser();
			this.setName(u.getName());
			this.setPassword(password);
			this.setEmailAddress(u.getEmailAddress());
			this.setHomeFolder(u.getHomeFolder());
			if(u.isLicenseOptionsSpecified()) this.setLicenseOptions(u.getLicenseOptions());
			this.setViewPreference(u.getViewPreference());
			if(u.isMaxJobPrioritySpecified()) this.setMaxJobPriority(u.getMaxJobPriority());
			if(u.isSendNoticeForSuccessSpecified()) this.setSendNoticeForSuccess(u.getSendNoticeForSuccess());
			if(u.isSendNoticeForFailureSpecified()) this.setSendNoticeForFailure(u.getSendNoticeForFailure());
			if(u.isSuccessNoticeExpirationSpecified()) this.setSuccessNoticeExpiration(u.getSuccessNoticeExpiration());
			if(u.isFailureNoticeExpirationSpecified()) this.setFailureNoticeExpiration(u.getFailureNoticeExpiration());
			if(u.isSendEmailForSuccessSpecified()) this.setSendEmailForSuccess(u.getSendEmailForSuccess());
			if(u.isSendEmailForFailureSpecified()) this.setSendEmailForFailure(u.getSendEmailForFailure());
			if(u.isAttachReportInEmailSpecified()) this.setAttachReportInEmail(u.getAttachReportInEmail());
			
			if(p.isRolesSpecified()) {
				this.setRoles(new HashSet<String>(Arrays.asList(p.getRoles().getString())));
			}
			if(p.isChannelsSpecified()) {
				this.setChannels(new HashSet<String>(Arrays.asList(p.getChannels().getString())));
			}
			if(p.isPrivilegeTemplateSpecified()) {
				this.setPrivilegeTemplate(p.getPrivilegeTemplate());
			}
		}
		
		// getters/setters
		public UserAndProperties getUserAndProperties() {
			UserAndProperties prop = new UserAndProperties();
			User u = new User();
			u.setName(this.getName());
			u.setEmailAddress(this.getEmailAddress());
			u.setHomeFolder(this.getHomeFolder());
			if(u.getLicenseOptions() != null) this.setLicenseOptions(u.getLicenseOptions());
			u.setViewPreference(this.getViewPreference());
			if(this.getMaxJobPriority() != null) u.setMaxJobPriority(this.getMaxJobPriority());
			if(this.getSendNoticeForSuccess() != null) u.setSendNoticeForSuccess(this.getSendNoticeForSuccess());
			if(this.getSendNoticeForFailure() != null) u.setSendNoticeForFailure(this.getSendNoticeForFailure());
			if(this.getSuccessNoticeExpiration() != null) u.setSuccessNoticeExpiration(this.getSuccessNoticeExpiration());
			if(this.getFailureNoticeExpiration() != null) u.setFailureNoticeExpiration(this.getFailureNoticeExpiration());
			if(this.getSendEmailForSuccess() != null) u.setSendEmailForSuccess(this.getSendEmailForSuccess());
			if(this.getSendEmailForFailure() != null) u.setSendEmailForFailure(this.getSendEmailForFailure());
			if(this.getAttachReportInEmail() != null) u.setAttachReportInEmail(this.getAttachReportInEmail());
			
			prop.setUser(u);
			if(this.getRoles() != null) {
				ArrayOfString a = new ArrayOfString();
				a.setString(this.getRoles().toArray(new String[]{}));
				prop.setRoles(a);
			}
			if(this.getChannels() != null) {
				ArrayOfString a = new ArrayOfString();
				a.setString(this.getChannels().toArray(new String[]{}));
				prop.setChannels(a);
			}
			if(this.getPrivilegeTemplate() != null) {
				ArrayOfPermission a = new ArrayOfPermission();
				for(RSSEPermission perm: this.getPrivilegeTemplate()) {
					Permission p = new Permission();
					p.setAccessRight(perm.getAccessRight());
					PermissionChoice_type0 choice = new PermissionChoice_type0();
					if(perm.getRoleName() != null) {
						choice.setRoleName(perm.getRoleName());
					} else {
						choice.setUserName(perm.getUserName());
					}
					p.setPermissionChoice_type0(choice);
					a.addPermission(p);
				}
				prop.setPrivilegeTemplate(a);
			}
			
			return prop;
		}
		public String getVolume() {
			return this.volume;
		}
		public void setVolume(String volume) {
			this.volume = volume;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getEmailAddress() {
			return emailAddress;
		}
		public void setEmailAddress(String emailAddress) {
			this.emailAddress = emailAddress;
		}
		public String getHomeFolder() {
			return homeFolder;
		}
		public void setHomeFolder(String homeFolder) {
			this.homeFolder = homeFolder;
		}
		public Set<String> getLicenseOptions() {
			return licenseOptions;
		}
		public void setLicenseOptions(ArrayOfString licenseOptions) {
			this.licenseOptions = new HashSet<String>();
			if(licenseOptions == null) {
				return;
			}
			String[] l = licenseOptions.getString();
			for(int i = 0; i < l.length; i++) {
				this.licenseOptions.add(l[i]);
			}
		}
		public String getViewPreference() {
			return viewPreference;
		}
		public void setViewPreference(String viewPreference) {
			this.viewPreference = viewPreference;
		}
		public Long getMaxJobPriority() {
			return maxJobPriority;
		}
		public void setMaxJobPriority(Long maxJobPriority) {
			this.maxJobPriority = maxJobPriority;
		}
		public Boolean getSendNoticeForSuccess() {
			return sendNoticeForSuccess;
		}
		public void setSendNoticeForSuccess(Boolean sendNoticeForSuccess) {
			this.sendNoticeForSuccess = sendNoticeForSuccess;
		}
		public Boolean getSendNoticeForFailure() {
			return sendNoticeForFailure;
		}
		public void setSendNoticeForFailure(Boolean sendNoticeForFailure) {
			this.sendNoticeForFailure = sendNoticeForFailure;
		}
		public Long getSuccessNoticeExpiration() {
			return successNoticeExpiration;
		}
		public void setSuccessNoticeExpiration(Long successNoticeExpiration) {
			this.successNoticeExpiration = successNoticeExpiration;
		}
		public Long getFailureNoticeExpiration() {
			return failureNoticeExpiration;
		}
		public void setFailureNoticeExpiration(Long failureNoticeExpiration) {
			this.failureNoticeExpiration = failureNoticeExpiration;
		}
		public Boolean getSendEmailForSuccess() {
			return sendEmailForSuccess;
		}
		public void setSendEmailForSuccess(Boolean sendEmailForSuccess) {
			this.sendEmailForSuccess = sendEmailForSuccess;
		}
		public Boolean getSendEmailForFailure() {
			return sendEmailForFailure;
		}
		public void setSendEmailForFailure(Boolean sendEmailForFailure) {
			this.sendEmailForFailure = sendEmailForFailure;
		}
		public Boolean getAttachReportInEmail() {
			return attachReportInEmail;
		}
		public void setAttachReportInEmail(Boolean attachReportInEmail) {
			this.attachReportInEmail = attachReportInEmail;
		}
		public List<RSSEPermission> getPrivilegeTemplate() {
			return this.privilegeTemplate;
		}
		public void setPrivilegeTemplate(ArrayList<RSSEPermission> privilegeTemplate) {
			if(privilegeTemplate != null) {
				this.privilegeTemplate = privilegeTemplate;
			} else {
				this.privilegeTemplate = new ArrayList<RSSEPermission>();
			}
		}
		public void addPermission(RSSEPermission permission) {
			if(this.getPrivilegeTemplate() == null) {
				this.setPrivilegeTemplate(new ArrayList<RSSEPermission>());
			}
			if(permission != null) {
				this.getPrivilegeTemplate().add(permission);
			}
		}
		public void setPrivilegeTemplate(ArrayOfPermission privilegeTemplate) throws AxisFault {
			this.privilegeTemplate = new ArrayList<RSSEPermission>();
			if(privilegeTemplate == null) {
				return;
			}
			Permission[] p = privilegeTemplate.getPermission();
			for(int i = 0; i < p.length; i++) {
				RSSEPermission perm = new RSSEPermission(this.getVolume());
				perm.setAccessRight(p[i].getAccessRight());
				if(p[i].getPermissionChoice_type0().isRoleNameSpecified()) {
					perm.setRoleName(p[i].getPermissionChoice_type0().getRoleName());
				} else {
					perm.setUserName(p[i].getPermissionChoice_type0().getUserName());
				}
				this.privilegeTemplate.add(perm);
			}
		}
		public Set<String> getChannels() {
			return channels;
		}
		public void setChannels(Set<String> channels) {
			if(channels != null) {
				this.channels = channels;
			} else {
				this.channels = new HashSet<String>();
			}
		}
		/**
		 * @param channel A key from main volume channel list
		 * @return the key that was added, null if nothing was added
		 * @throws AxisFault 
		 */
		public String addChannel(String channel) throws AxisFault {
			String key = channel.toUpperCase();
			Map<String, String> channels = SampleRSSE.volumes.get(this.getVolume()).getChannels();
			if(!channels.containsKey(key)) {
				SampleRSSE.throwException("Channel " + channel + " is not found on the main channel list");
			}
			if(this.getChannels() == null) {
				this.setChannels(new HashSet<String> ());
			}
			String added = null;
			boolean channelFound = false;
			for(String c: this.getChannels()) {
				if(key.equals(c.toUpperCase())) {
					channelFound = true;
					break;
				}
			}
			if(!channelFound) {
				String name = channels.get(key);
				this.getChannels().add(name);
				added = name;
			}
			return added;
		}
		public Set<String> getRoles() {
			return roles;
		}
		public void setRoles(Set<String> roles) {
			if(roles != null) {
				this.roles = roles;
			} else {
				this.roles = new HashSet<String>();
			}
		}
		/**
		 * @param role  
		 * @return The key that was added, null if nothing was added
		 * @throws AxisFault 
		 */
		public String addRole(String role) throws AxisFault {
			String key = role.toUpperCase();
			Map<String, String> roles = SampleRSSE.volumes.get(this.getVolume()).getRoles();
			if(!roles.containsKey(key)) {
				SampleRSSE.throwException("Role " + role + " is not found on the main role list");
			}
			if(this.getRoles() == null) {
				this.setRoles(new HashSet<String> ());
			}
			String added = null;
			boolean roleFound = false;
			for(String c: this.getRoles()) {
				if(key.equals(c.toUpperCase())) {
					roleFound = true;
					break;
				}
			}
			if(!roleFound) {
				String name = roles.get(key);
				this.getRoles().add(name);
				added = name;
			}
			return added;
		}
	}

	
	// ##########################################################################
	// ############# Helper Methods #############################################
	// ##########################################################################
	
	/**
	 * Select users in a volume using SmartSearch conditions.
	 * @return A list of users matching the search criteria
	 */
	private String[] selectUsers(String volume, String query, int maxNumber) throws AxisFault {
		SelectUsersE reqE = new SelectUsersE();
		SelectUsers req = new SelectUsers();
		req.setFetchSize(maxNumber);
		SelectUsersChoice_type0 choice0 = new SelectUsersChoice_type0();
		choice0.setQueryPattern(query);
		req.setSelectUsersChoice_type0(choice0);
		reqE.setSelectUsers(req);
		SelectUsersResponseE resE = this.selectUsers(volume.toUpperCase(), reqE);

		if(resE.getSelectUsersResponse().getUsers() != null) {
			return resE.getSelectUsersResponse().getUsers().getString();
		}

		return new String[]{};
	}
	
	/**
	 * Check is user exists in specified volume.
	 */
	private boolean userExists(
			String volume,
			String userName) {
		return this.getVolume(volume.toUpperCase()).getUsers().containsKey(userName.toUpperCase());
	}
	
	/**
	 * Check if user group (role) exists in the specific volume.
	 */
	private boolean roleExists(
			String volume,
			String roleName) {
		return this.getVolume(volume.toUpperCase()).getRoles().containsKey(roleName.toUpperCase());
	}
	
	/**
	 * Old groups are not supported in iHub. Hence throw an exception.
	 */
	private boolean groupExists(
			String volume,
			String groupName) throws AxisFault {
		SampleRSSE.throwException("Groups are not supported in iHub");
		return false;
	}

	/**
	 * Read user, role and channel data for all volumes from a JSON file.
	 */
	private synchronized void readDataFromFile() throws AxisFault {
		try {
			File jsonFile = new File(SampleRSSE.DATA_FOLDER + "/" + SampleRSSE.JSON_STORAGE_FILE_NAME);
			if (jsonFile.exists()) {
				SampleRSSE.log("Bulk load JSON file is found: " + SampleRSSE.JSON_STORAGE_FILE_NAME);
				Gson gson = new Gson();
				//BufferedReader br = new BufferedReader(new FileReader(jsonFile));
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(SampleRSSE.DATA_FOLDER + "/" + SampleRSSE.JSON_STORAGE_FILE_NAME), "UTF8"));
				//DataSerialization data = gson.fromJson(br, DataSerialization.class);
				//SampleRSSE.volumes = data.getVolumes();
				SampleRSSE.volumes = gson.fromJson(br, (new TypeToken<ConcurrentHashMap<String, RSSEVolume>>() {}).getType());
				br.close();
			} else {
				SampleRSSE.log("JSON file " + SampleRSSE.JSON_STORAGE_FILE_NAME + " not found. Creating vanilla instance.");
			}
		} catch (IOException e) {
			SampleRSSE.throwException(e, "I/O exception while reading JSON file");
		} catch (Exception e) {
			SampleRSSE.throwException(e, "General exception while reading JSON file");
		}
	}
	
	/**
	 * Save user properties, roles and channels to a JSON file.
	 */
	private synchronized void saveDataToFile() throws AxisFault {
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			File file = new File(SampleRSSE.DATA_FOLDER + "/" + SampleRSSE.JSON_STORAGE_FILE_NAME);
 			Writer out = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(file), "UTF8") );
//	 			DataSerialization data = new DataSerialization();
//	 			data.setVolumes(SampleRSSE.volumes);
			out.write(gson.toJson(SampleRSSE.volumes, (new TypeToken<ConcurrentHashMap<String, RSSEVolume>>() {}).getType()));
			out.flush();
			out.close();
			SampleRSSE.log("stop() contents were saved to JSON file");
		} catch (UnsupportedEncodingException e) {
			SampleRSSE.throwException("Cannot save JSON data to a file");
		} catch (IOException e) {
			SampleRSSE.throwException("Cannot save JSON data to a file");
		}
	}
	
	/**
	 * Add several users to the in-memory data structure. Useful for testing and learning.
	 */
	private void addSampleContents() {
		try {
			String volumeName = "DEFAULT VOLUME";
			int roleCount = 3;
			int channelCount = 4;
			RSSEVolume vol = this.getVolume(volumeName);
			if(vol == null) {
				SampleRSSE.volumes.put(volumeName, new RSSEVolume(volumeName));
				vol = SampleRSSE.volumes.get(volumeName.toUpperCase());
			}
			for(int i = 1; i <= roleCount; i++) {
				vol.addRole("role" + i);
			}
			for(int i = 1; i <= channelCount; i++) {
				vol.addChannel("channel" + i);
			}
			
			// add user1
			RSSEUser u = new RSSEUser(volumeName);
			u.attachReportInEmail = true;
			u.emailAddress = "user1@devmail.com";
			u.failureNoticeExpiration = 5L;
			u.homeFolder = "/Home/user1";
			u.maxJobPriority = 500L;
			u.name = "user1";
			u.password = "pass";
			u.sendEmailForFailure = true;
			u.sendEmailForSuccess = true;
			u.sendNoticeForFailure = true;
			u.sendNoticeForSuccess = true;
			u.successNoticeExpiration = 7L;
			u.viewPreference = "HTML";
			u.addRole("Role1");
			u.addRole("Role2");
//			u.addChannel("Channel2");
//			u.addChannel("Channel3");
			RSSEPermission p = new RSSEPermission(volumeName);
			p.setAccessRight("RW");
			p.setRoleName("Role1");
			u.addPermission(p);
			p = new RSSEPermission(volumeName);
			p.setAccessRight("VR");
			p.setRoleName("Role2");
			u.addPermission(p);
			vol.addUser(u);
			
			// add user2
			u = new RSSEUser(volumeName);
			u.attachReportInEmail = true;
			u.emailAddress = "user1@devmail.com";
			u.failureNoticeExpiration = 500L;
			u.homeFolder = "/Home/user1";
			u.maxJobPriority = 300L;
			u.name = "user2";
			u.password = "pass";
			u.sendEmailForFailure = true;
			u.sendEmailForSuccess = false;
			u.sendNoticeForFailure = true;
			u.sendNoticeForSuccess = false;
			u.successNoticeExpiration = 100L;
			u.addRole("Role2");
//			u.addChannel("Channel1");
//			u.addChannel("Channel3");
			p = new RSSEPermission(volumeName);
			p.setAccessRight("RWE");
			p.setUserName("User1");
			u.addPermission(p);
			p = new RSSEPermission(volumeName);
			p.setAccessRight("VREG");
			p.setRoleName("Role2");
			u.addPermission(p);
			vol.addUser(u);
			
		} catch (Exception e) {
			SampleRSSE.log(e, "Sample content population failed");
		}
	}
	
	/**
	 * Return null for empty string 
	 */
	private String nullIfEmpty(String str) {
		return (str == null || str.length() <= 0) ? null : str;
	}

	/**
	 * Convert SmartSearch expressions supported in IDAPI to RegEx syntax.
	 */
	private String convertQueryToRegex(String query) {
		// Due to IDAPI case-insensitivity, this method will compare keys, not actual values. 
		// All keys are stored in uppercase, so make all literals in the query uppercase.
		String q = query.toUpperCase();
		if(q == null || q.length() <= 0) {
			return ".*";
		}
		String regex = new String();
		for (int i = 0; i < q.length(); i++) {
			if (q.charAt(i) == '*')
				regex += ".*";
			else if (q.charAt(i) == '#')
				regex += "[0-9]";
			else if (q.charAt(i) == '?')
				regex += ".";
			else
				regex += q.charAt(i);
		}
		return regex;
	}
	
	/**
	 * Log a message and throw an AxisFault
	 */
	private static void throwException(String message) throws AxisFault {
		SampleRSSE.log(message);
		throw new AxisFault(message);
	}
	
	/**
	 * Log original exception details and throw an AxisFault
	 */
	private static void throwException(Exception e, String message) throws AxisFault {
		SampleRSSE.log(e, message);
		throw new AxisFault(message, e);
	}
	
	/**
	 * Verify if the encrypted password matches the actual user password.
	 * The algorithm below matches direct (actual) and reversed (encrypted)
	 * strings. 
	 */
	private static boolean verifyEncryptedPassword(final byte[] encrypted, final byte[] actual) {
		if(encrypted == null && actual == null) {
			return true;
		}
		if((encrypted == null && actual != null) ||
			(encrypted != null && actual == null) ||
			(actual.length != encrypted.length)) {
			return false;
		}
	    // compare using "decryption" algorithm, which is a simple reverse in this case
	    for(int i = 0; i < encrypted.length; i++) {
	    	if(actual[actual.length - i - 1] != encrypted[i]) {
	    		return false;
	    	}
	    }
	    
	    return true;
	}
	
}
