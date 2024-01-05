# PhysioAssistant Development Guide
## Table of Contents
- [Introduction](#introduction)
- [File Structure](#file-structure)
- [Activities](#activities)
  - [LoginActivity](#loginactivity)
  - [MainActivity](#mainactivity)
  - [SplashActivity](#splashactivity)
  - [AdminActivity](#adminactivity)
  - [DoctorActivity](#doctoractivity)
  - [PatientActivity](#patientactivity)
- [Resources](#resources)
  - [Logo](#logo)
  - [Colors](#colors)
  - [Strings](#strings)

## Introduction
This guide will help the development team to build an Android application for a physiotherapy center. The application will have three types of users: Admin, Doctor, and Patient. The Admin will be responsible for creating the physiotherapy center and adding new services, the Doctor will have access to the patient history, appointment plan, and appointment requests, and the Patient will be able to request appointments and view financial transactions.

## File Structure

The file structure for the PhysioAssistant app is as follows:

```
├───main
│       ├───java
│       │   └───uom
│       │       └───android
│       │           └───physioassistant
│       │               └───activities
│       │                   ├───admin
│       │                   ├───doctor
│       │                   ├───login
│       │                   └───patient
│       └───res
│           ├───drawable
│           ├───drawable-v24
│           ├───layout
│           ├───mipmap-hdpi
│           ├───values
│           ├───values-night
│           └───xml
```

The `java` folder contains the source code for the app, while the `res` folder contains various resources such as layouts, images, and strings.

## Activities

### LoginActivity
The `LoginActivity` activity will be used for the login process. All users will be validated using a username and password. When the user logs in, the backend will return the role of the user. Based on the role, the corresponding activity will be opened.

### MainActivity
The `MainActivity` activity contains three buttons for each type of user login. If we end up not using username and password for all the users and each user is verified using something different, we will make three different login activities and use them. To select what kind of login we want, we will use `MainActivity`.

### SplashActivity
The `SplashActivity` activity is a simple splash screen activity.

### AdminActivity
The `AdminActivity` activity is the home activity for the admin user. It contains buttons for creating a physiotherapy center and adding new services. All activities or fragments used for the admin functionality will be placed in the `admin` package.

### DoctorActivity
The `DoctorActivity` activity is the home activity for the doctor user. It contains buttons for viewing patient history, search and select patient, displaying the weekly appointment plan, viewing and managing appointment requests, and adding visits and recording actions. All activities or fragments used for the doctor functionality will be placed in the `doctor` package.

### PatientActivity

The `PatientActivity` activity is the home activity for the patient user. It contains buttons for requesting appointments and viewing detailed financial transactions. All activities or fragments used for the patient functionality will be placed in the `patient` package.

## Resources
### Logo
The logo for the PhysioAssistant app is located in:
```
res/drawable/logo_white.png
```

### Colors
The app's color scheme is defined in the `colors.xml` file located in the `values` folder. The colors can be referenced using their respective color names. \
```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="black">#FF000000</color>
    <color name="white">#FFFFFFFF</color>
    <color name="primary_dark">#131626</color>
    <color name="secondary_light">#FFFFFFFF</color>
</resources>

```
It is good to ensure that these colors are used consistently throughout the app.

### Strings
The text strings used in each activity are located in separate `.xml` files in the `values` folder. For example, the strings used in the `MainActivity` activity are located in the `strings_main.xml` file. Similarly, the strings used in the `CreateServiceActivity` activity are located in the `strings_create_service.xml` file.

## Authentication

Currently, our app does not have a backend to handle authentication. As a temporary solution, we have implemented a simple authentication mechanism in the LoginActivity that checks if the entered username matches one of the predefined roles - `admin`, `doctor`, or `patient`. If the entered username is one of these roles, the user is redirected to the corresponding activity. Otherwise, the authentication fails and an error message is displayed.

### Login Back-End
For the backend what I have in mind is: \
We implement a login endpoint that will require a username and password to authenticate the user and return the user's role if the login is successful. If the login fails, the backend will return an error message. This will replace the current dummy authentication mechanism that accepts any username as the authentication token.

On the front-end, we will make a call to the login endpoint and retrieve the response. We will then extract the user's role from the response. Based on the user's role, we will open the corresponding activity. If the login fails, we will display the error message returned by the backend by setting the errorMsg attribute to the error message and setting it to visible.

Here is an example of how the front-end code might be look to handle the new login process:
```java
public void handleLogin(View view) {
    String username = String.valueOf(usernameInput.getText());
    String password = String.valueOf(passwordInput.getText());
    Log.i("Login", "Username: " + username + " Password: " + password);

    // Call login endpoint
    LoginResponse response = api.login(username, password);

    if (response.isSuccess()) {
        String role = response.getRole();
        Intent next_activity;

        if (role.equalsIgnoreCase("admin")) {
            // Start Admin Activity
        }
        else if (role.equalsIgnoreCase("patient")) {
            // Start Patient Activity
        }
        else if (role.equalsIgnoreCase("doctor")) {
            // Start Patient Activity
        }
    } 
    // Failed Authentication
    else { 
        // Show error message
    }
}
```