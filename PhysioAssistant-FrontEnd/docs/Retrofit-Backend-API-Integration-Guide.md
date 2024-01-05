# **Connecting the Android App to the Spring Boot REST API**

To connect the Android app to the Spring Boot REST API, follow these steps:

## Step 1: Use the RetrofitService

The `RetrofitService` is used to access the backend API. To use it, add the following attribute to the activity:

```
private RetrofitService retrofitService;
```

Then, create an instance of the `RetrofitService` and assign it to the attribute:

```
this.retrofitService = new RetrofitService();
```

## Step 2: Use the API Interfaces

**To use the existing API interfaces, follow these steps:**

1. Add the necessary methods to the API interface. Use the HTTP method annotations such as `@GET`, `@POST`, `@PUT`, `@DELETE`, etc., to specify the HTTP method of the request. Use the `@Path` annotation to substitute dynamic parts of the URL. Use the `@Query` annotation to add query parameters to the URL. Use the `@Body` annotation to send JSON or XML payload in the request body.
2. Add an attribute of the API interface you want to use to the activity. For example:

    ```python
    private DoctorApi doctorApi;
    ```

3. Initialize the API interface using the `RetrofitService`. For example:

    ```python
    this.doctorApi = retrofitService.getRetrofit().create(DoctorApi.class);
    ```


**To create a new API interface, follow these steps:**

1. Create a new interface for the API you want to access. For example:

    ```python
    public interface DoctorApi { 
    	@POST("api/doctors/create") 
    	Call<Doctor> createDoctor(@Body Doctor doctor);
    }
    ```

   > Note: The URL path will vary depending on the backend API.
>
2. Add an attribute of the new API interface to the activity. For example:

    ```python
    private DoctorApi doctorApi;
    ```

3. Initialize the new API interface using the `RetrofitService`. For example:

    ```python
    this.doctorApi = retrofitService.getRetrofit().create(DoctorApi.class);
    ```

   > Note: Make sure to use the correct class name for the new API interface.
>

Once you have added the necessary methods to the API interface and initialized it using the `RetrofitService`, you can call the API using the method defined in the API interface and pass the necessary parameters. For example:

```
this.doctorApi.createDoctor(doctor)
        .enqueue(new Callback<Doctor>() {
            @Override
            public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                // Handle the response
            }

            @Override
            public void onFailure(Call<Doctor> call, Throwable t) {
                // Handle the failure
            }
        });
```

## Backend Repository

The repository for the backend code can be found at [insert repository link here]. This can be used as reference when working with the backend API.

## Handling Responses

When making requests to the API, we need to handle both successful and unsuccessful responses. In the example, the `onFailure` callback is used to handle cases where no response is received from the API (eg the backend is not running). E

ven if a response is received, it may not be a successful response. For example, the server may return an HTTP error code such as 404 or 500.

To handle these cases, we need to check the response status code in the `onResponse` callback.

```java
this.doctorApi.createDoctor(doctor)
        .enqueue(new Callback<Doctor>() {
            @Override
            public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                if (response.isSuccessful()) {
                    // Handle successful response
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<Doctor> call, Throwable t) {
                // Handle failure
            }
        });
```

## Running the Backend

In order to use the API, the backend Spring Boot project must be running. We need to clone the project from the repository and run it locally. You can do this by following these steps:

1. Clone the project repository from [here](https://github.com/Android-Development-UoM/PhysioAssistant-BackEnd).
2. Follow the instructions provided on `docs/[spring-xampp-db-connection.md](http://spring-xampp-db-connection.md/)`
3. Open the project in the IDE of your choice (I use IntelliJ).
4. Build the project using the build tool (e.g. Maven or Gradle).
5. Run the project.

Once the project is running, you can access the API by specifying the base URL of the API in the `RetrofitService` class (package `backend.retrofit`).

The default base URL in the class is `http://192.168.2.5:8080`.

If the API is running on a different ip, we will need to update this URL accordingly.

To find the IP address of the machine running the backend, we can use the `ipconfig` command.

```
private static String API_URL = "http://<backend_ip_address>:8080";
```

By default, the port number for the backend is set to 8080 and should not be changed unless it is modified in the backend code.