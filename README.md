
# AWS Communication Application

## Overview
Spring boot application to interact with AWS SES and SNS components. Currently it exposes below functionality

* Send Email (Templated Email)
* Send SMS

## Code Build

To build the code, please run below command:
```
mvn clean install
```
This will prepare the JAR artifact to be deployed.
If the code analysis also needs to be done with the build, then please run below command
```
mvn clean install sonar:sonar
```

## How to Test
Once the application is deployed, API can be tested by triggering below sample requests:

### Send Email
**Sample Requests**
```
POST http://<ip:port>/aws-communication/v1/email/send
User-Agent: Apache-HttpClient/4.5.5 (Java/12.0.1)

{
	"templateName":"TestEmail",
	"sender": {
		"emailAddress":"test@gmail.com"
	},
	"receiver": {
		"emailAddress":"test@gmail.com"
	}
}
```

**Sample Response**
Success Scenario
```
HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8

{
	"messageId":"0107017a80d6fc75-877a9eaf-d85d-4533-bfe8-29e471c2ca2a-000000"
}
```
Template not found
```
HTTP/1.1 404 
Content-Type: application/json;charset=UTF-8

{
	"code":"404001",
	"reason":"Template not found for given input: dd"
}
```

### Send SMS
**Sample Request**
```
POST http://<ip:port>/aws-communication/v1/sms/send

Content-Type: application/json

{
	"message": "Hi There, test SMS",
	"messageType":"Transactional",
	"sender": {
		"senderId":"TS-TEST"
	},
	"receiver": {
		"phoneNumber":"+316133XXXXX"
	}
}
```

**Sample Response**
Success Scenario
```
HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8

{
	"messageId":"e62191fe-ce6a-566b-aec3-c8a15730a82a"
}
```
