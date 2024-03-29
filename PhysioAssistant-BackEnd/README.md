<h1 align="center">
  <img src="https://user-images.githubusercontent.com/77233507/236688721-4d6af24e-d128-448d-90fa-198ef66e5f3d.png" width="500" height="250" />
</h1>

<p align="center">
  <img src="https://img.shields.io/badge/Back--End-blue" alt="Front-End" />
</p>

This is a Github repository created to store our assignment for the Android Development course at University of Macdenia. \
The API was developed using SpringBoot framework and IntelliJ as the IDE. The API provides the ability to perform various CRUD (Create, Read, Update, Delete) operations on a database.

# Getting Started
## Prerequisites

- Java 11 or higher
- MySQL
- IntelliJ or any other Java IDE

## Installing

1. Clone this repository
2. Open the project in IntelliJ or your preferred IDE
3. Follow the steps on `spring-xampp-db-connection.md`
4. Run the application

## Setting Up the Database Connection

To connect the Spring Boot backend with XAMPP MySQL, follow the instructions provided in the `spring-xampp-db-connection.md` file located in the `docs` directory of this repository. The guide explains how to download XAMPP, start MySQL, create a new database, and configure the database credentials in the `application.properties` file.
Once you have completed the steps in the guide, you can run the Spring Boot application and verify that the tables are automatically generated by Spring Boot. To do this, simply run the application and navigate to [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui/index.html#/) to access the API documentation.

## API Documentation

The API documentation is available at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) once the application is running.

## Cloning the Repository

To clone this repository to your local machine, you can use the following command:

```
git clone https://github.com/your-username/PhysioAssistant-FrontEnd.git
```

You can also open the repository directly in Github Desktop from the Github website. Here's how to do it:
1. Go to the repository on Github.
2. Click on the green "Code" button in the top right corner.
3. Select "Open with Github Desktop".
4. Github Desktop will open and prompt you to choose the location on your local machine where you want to save the repository.
5. Click "Clone".

Once you have cloned the repository, you can follow the instructions provided earlier to create a new branch and contribute to the project.

## Writing to the Repository
Everyone has to make changes to his own branch. Please do not work directly on the master branch.
To create a new branch, follow these steps:

1. Open Github Desktop and select the "Current Branch" dropdown.
2. Select "New Branch".
3. Give your new branch a name like your username.
4. Click "Create Branch".
We can make branches on own branches

Once you have created your branch, you can make changes to the code and commit them to your branch.
When we finish with changes on the code the team will evaluate it and merge the bracnhes on the master branch.

## Built With

<div style="display: flex; flex-direction: row; align-items: center; justify-content: space-evenly;">
    <img src="https://raw.githubusercontent.com/ArchontisKostis/devicon/1119b9f84c0290e0f0b38982099a2bd027a48bf1/icons/spring/spring-original.svg" height="50" alt="Spring Boot" />
    <img src="https://raw.githubusercontent.com/ArchontisKostis/devicon/1119b9f84c0290e0f0b38982099a2bd027a48bf1/icons/mysql/mysql-original-wordmark.svg" height="100" alt="MySQL" />
    <img src="https://raw.githubusercontent.com/ArchontisKostis/devicon/1119b9f84c0290e0f0b38982099a2bd027a48bf1/icons/intellij/intellij-original-wordmark.svg" width="100" alt="IntelliJ IDEA" />
</div>

