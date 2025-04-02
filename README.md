# MovieWeb
A web-based movie streaming platform supporting HLS + Video.js, multi-language, light/dark themes, and AI-powered recommendations.

## Technologies
- **Backend**: Spring Boot, AWS Services  
- **Frontend**: Video.js, HLS  
- **Database**: MySQL  
- **Cloud Services**: AWS S3  

## Setup Instructions

### 1. Database Setup
Ensure MySQL is installed and running. Then, execute the provided SQL script:

```sh
mysql -u root -p < path/to/setup_script.sql
```

### 2. Build & Run the Project
Using Maven:

```sh
mvn clean install
```

If dependencies are missing, reload Maven:

```sh
mvn dependency:resolve
```

Running the application:

```sh
mvn spring-boot:run
```

### 3. AWS S3 Key Setup
This project requires AWS credentials to access S3. Contact the project maintainer to obtain the necessary keys.

After obtaining the keys, set them up in your `.env` file:

```env
AWS_ACCESS_KEY=your-access-key
AWS_SECRET_KEY=your-secret-key
```

## Security Notice
To ensure security and prevent accidental exposure of sensitive credentials:

- The `application.properties` file has been removed from the repository.
- Firebase credentials (`firebase-service-account.json`) have been excluded from version control.
- Make sure to configure these files locally before running the project.

## Contact
For credentials and support, reach out via GitHub: [NguyenDucTrung8149](https://github.com/NguyenDucTrung8149)
