# Backend-Exercise Job Data API

This project is a backend application designed to expose APIs for querying job data. 
The API supports filtering, sorting, and dynamic field selection based on user-provided query parameters.
It is implemented using **Spring Boot** and follows RESTful principles.

**I have attached the PostMan collection within this project, you can test it**

## Features
- **Retrieve Job Data**: Fetch job data with optional filtering and sorting.
- **Dynamic Filtering**: Filter job data based on multiple criteria such as salary, job title, and gender.
- **Sparse Fieldsets**: Retrieve only the specified fields for each record, reducing payload size.
- **Data Initialization**: Load initial data from a CSV file using **Liquibase**.
- **Data Cleanup**: Messy data from the CSV file is processed and cleaned before being imported into the database.
- **Dynamic Queries**: Use of JPA Criteria API for building flexible, dynamic queries.

## How It Works
- **Dynamic Query Construction**: APIs leverage JPA Criteria API for flexible filtering and field selection.
This ensures that only relevant records and fields are fetched dynamically based on user input.
- **Field Mapping**: The system uses a regular expression to transform user-provided field names (e.g., job_title) to match database column names (e.g., JOB TITLE).
- **Error Handling**: Graceful handling of invalid query parameters or unexpected server errors.Returns appropriate HTTP status codes and error messages.

## **Tech Stack**

- **Spring Boot**: Backend framework for building RESTful APIs.
- **Liquibase**: Database migration and data initialization.
- **H2 Database**: In-memory database for local testing.
- **JPA/Hibernate**: ORM for interacting with the database.
- **Jackson**: For JSON serialization and deserialization.

### **How to Run the Application**

1. Clone this repository:
    ```bash
    https://github.com/Boat718/Backend-Exercise.git
    cd Backend-Exercise
    ```

2. Build the project:
    ```bash
    ./mvnw clean package
    ```

3. Run the application:
    ```bash
    java -jar target/job-data-api-0.0.1-SNAPSHOT.jar
    ```

    Access the APIs on [http://localhost:8080](http://localhost:8080)

## API Endpoints
#### **`GET /api/v1/job_data`**
- Filter by one or more fields/attributes (e.g. /job_data?salary[gte]=120000) (Show only filtered row. Expected filter able column: job title, salary, gender )
- Filter by a sparse fields/attributes (e.g. /job_data?fields=job_title,gender,salary) (Show only filtered column)
- Sort by one or more fields/attributes (e.g. /job_data?sort=job_title&sort_type=DESC)
  
| column in CSV | fields for Params |
| ------ | ------ |
| Employer | employer |
| Location | location |
| Job Title | job_title |
| Years at Employer | years_at_employer |
| Years of Experience| years_of_experience |
| Salary | salary (Filter by one or more fields salary[get], salary[lte]) |
| Signing Bonus | signing_bonus |
| Annual Bonus | annual_bonus |
| Annual Stock Value/Bonus | annual_stock_value_bonus |
| Gender | gender |
| Additional Comments | additional_comments |

