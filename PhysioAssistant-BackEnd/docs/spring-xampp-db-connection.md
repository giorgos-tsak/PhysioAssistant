# Connecting a Spring Boot Backend with XAMPP MySQL

To connect a Spring Boot backend with XAMPP MySQL, follow these steps:

1. Download XAMPP from the official website and install it on your computer.
2. Open XAMPP Control Panel and start MySQL by clicking on the "Start" button next to "MySQL".
3. Create a new database by clicking on the "phpMyAdmin" button in XAMPP Control Panel. This will open a new tab in your browser with the phpMyAdmin interface. Click on the "Databases" tab, enter a name for your database and click on the "Create" button.
4. In your Spring Boot project, navigate to the `application.properties` file and locate the following lines: \
   (You can find the `application.properties` file in the Spring Boot project's root directory under `src/main/resources/`)
```
spring.datasource.url=jdbc:mysql://localhost:3306/physio_assistant
spring.datasource.username=root
spring.datasource.password=
```

1. Replace the values with the following, making sure to replace `your_db_name`, `your_username`, and `your_password` with the actual values:

```
spring.datasource.url=jdbc:mysql://localhost:3306/your_db_name
spring.datasource.username=your_username
spring.datasource.password=your_password
```

Note that if the database you created in phpMyAdmin is named `physio_assistant`, you don't need to change the first line.

1. Save the changes to the `application.properties` file.
2. Make sure that the port that MySQL is running on is correct. By default, MySQL runs on port 3306.
3. Start your Spring Boot project and verify that everything is working correctly. Spring Boot will automatically generate the required tables in your MySQL database based on your entity classes.

> **Note:** If you named your database `physio_assistant` in step 5, you can leave the `spring.datasource.url` property as is.
>

> **Note:** If you didn't set a username or password when installing XAMPP, you can leave the `spring.datasource.username` and `spring.datasource.password` properties as is (they have the xampp default values - root and empty pass).
>