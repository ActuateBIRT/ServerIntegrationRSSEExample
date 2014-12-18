#Server Integration RSSE Example

##Overview

This is a sample Reporting Server Security Extension (RSSE) implementation. It uses Axis2 for messaging and JSON-formatted data for defining user properties, roles, and channels. This sample implementation can be used in conjuction with the  Sample IPSE implementation.

##Setup

1. Open the file 'src\SampleRSSE.properties' in an editor. This file contains config properties that override default values in the example driver code:

  * DATA_FOLDER - Folder that contains the JSON-formatted data file containing the user name/password mappings. A sample JSON file is provided.
  * LOG_FILE_NAME - Name of the log file the driver creates in DATA_FOLDER.
  * JSON_STORAGE_FILE_NAME - JSON-formatted data file that contains user properties.
  * USER_ADMINISTRATOR - Translated administrative user name. Can be different from the default user Administrator if you require a different admin user. Note that starting with iHub3, the default user Administrator always has  admin privileges.
  * ADMIN_ROLE - Translated administrative role name. Supports specifying a role which must be assigned to all administrative users.
  * ROLE_ALL  - Translated role All. All users are assigned to the All role implicitly. 
  * LICENSE_OPTIONS_EXTERNALIZED - Reserved for future use. The current sample implementation does not support externalized license options, but the implementation can be changed to support them.
2. Create the folder that DATA_FOLDER specifies.
3. Copy the file that JSON_STORAGE_FILE_NAME specifies to the new folder. A log file will also be created in this folder later on.

###Data File Structure

Data file has the following structure, which reflects the class hierarchy in the sample driver. Do not change the data file structure without changing the respective class implementation:

```
{
  "DEFAULT VOLUME": {						<== capitalized volume name; used as a hash key
    "name": "DEFAULT VOLUME",				<== actual volume name
    "users": {								<== user list in the volume
      "USER1": {							<== capitalized name of the user; used as a key
        "volume": "DEFAULT VOLUME",
        "name": "user1",					<== user name and other properties
        "password": "pass",
        "emailAddress": "user1@devmail.com",
        "homeFolder": "/Home/user1",
        "viewPreference": "HTML",
        "maxJobPriority": 500,
        "sendNoticeForSuccess": true,
        "sendNoticeForFailure": true,
        "successNoticeExpiration": 7,
        "failureNoticeExpiration": 5,
        "sendEmailForSuccess": true,
        "sendEmailForFailure": true,
        "attachReportInEmail": true,
        "privilegeTemplate": [				<== default user privilege template, which is represented as FileCreationACL in IDAPI
          {
            "volume": "DEFAULT VOLUME",
            "accessRight": "RW",
            "roleName": "role1"
          },
          {
            "volume": "DEFAULT VOLUME",
            "accessRight": "VR",
            "roleName": "role2"
          }
        ],
        "roles": [							<== user groups assigned to the user (roles in older releases)
          "role1",
          "role2"
        ]
      }
	  ...
```

###Build
Build the sample RSSE implementation by running the command "ant" using the provided Ant script 'build.xml'. The build will produce a file named 'SampleRSSE.aar' in the project folder '/bin'. Note that the build will generate proxy classes based on the included WSDL file. You can replace the file with your own version if necessary.

###Deploy

The build generates an archive ready to deploy on Tomcat 7. Please follow instructions for deploying Axis2-based applications on Tomcat. Some guidelines can be found here: https://axis.apache.org/axis2/java/core/docs/userguide-buildingservices.html#deployrun. However, deploying is as simple as copying the generated SampleRSSE.aar file to the Axis2 services folder and restarting Tomcat.

Add the included gson-x.x.jar library to the Axis2 WEB-INF/lib folder.  Restart Tomcat after applying this.

###Configure iHub

Please follow directions in the official Actuate documentation. In short: you will either have to use System Console or add RSSE related parameters to acserverconfig.xml manually. If you are deploying the sample RSSE implementation on Tomcat with Axis2 deployed, the Context Path in the RSSE settings on iHub will be '/axis2/services/SampleRSSE'.

##Usage

Use the sample RSSE implementation as you would normally use an RSSE web service. There are certain features specific to this particular implementation:

* All data is stored in a JSON file. The data is loaded into memory during web service startup. It can be reloaded or saved back to the same JSON file on demand (see below). The in-memory data can even be altered using IDAPI commands sent via the IDAPI pass-through mechanism (see below).
* The driver utilizes a mechanism typically called 'pass-through'. It is represented by the CallOpenSecurityLibrary IDAPI request and the PassThrough RSSE API request . It allows passing any Base64-encoded data to the RSSE web service for further interpretation. Currently the sample driver supports two operations:
  - Reload data from a JSON file. This is similar to retrieving updated contents from LDAP or DB. It is still necessary to send the UpdateOpenSecurityCache IDAPI request to refresh user information in the volume(s).
  - Save to file. Allows saving in-memory data to the same JSON file. This may be useful if the data has been altered since it was originally loaded.
* Authentication can be done using extended credentials passed in the IDAPI Login request instead of password. This mechanism is also implemented in the Sample IPSE example. Both examples, RSSE and IPSE, use simple reverse for "encrypting" a user password passed as extended credentials (e.g. pass=>ssap).

##Troubleshooting

1. Track contents of DATA_FOLDER: a log file will be created when the SampleRSSE class gets loaded for the first time, which happens when user authorization is required.
2. If you see no log file created after you start the web service, check the log files in your iHub installation for errors indicating that RSSE has not been initialized.
3. Read comments in the code. They may provide understanding if you encounter difficulty.
4. Setup a TCP tunnel between iHub and the RSSE web service to capture all traffic.