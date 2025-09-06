# Secure E-Commerce Web Application - Assessment 2

A secure full-stack e-commerce application built with Spring Boot (Backend) and React.js (Frontend) implementing OWASP Top 10 security mitigations, OIDC authentication with Auth0, and comprehensive access control.

## 🔒 Security Features

### OWASP Top 10 Mitigations Implemented

1. **A01: Broken Access Control**
   - JWT-based authentication with Auth0
   - Role-based access control
   - User-specific data isolation
   - Access token validation on all protected endpoints

2. **A02: Cryptographic Failures**
   - HTTPS enforcement
   - Secure password hashing with BCrypt
   - JWT token security
   - Secure session management

3. **A03: Injection**
   - SQL injection prevention using JPA/Hibernate
   - Input validation and sanitization
   - Parameterized queries
   - XSS prevention with input filtering

4. **A04: Insecure Design**
   - Secure architecture with proper separation of concerns
   - Input validation at multiple layers
   - Business logic validation
   - Secure error handling

5. **A05: Security Misconfiguration**
   - Security headers implementation
   - CORS configuration
   - Rate limiting
   - Secure cookie settings

6. **A06: Vulnerable and Outdated Components**
   - Latest Spring Boot 3.3.3
   - Regular dependency updates
   - Security patches applied

7. **A07: Identification and Authentication Failures**
   - OIDC authentication with Auth0
   - Strong password policies
   - Session management
   - Multi-factor authentication support

8. **A08: Software and Data Integrity Failures**
   - Input validation
   - Data integrity checks
   - Secure file upload handling

9. **A09: Security Logging and Monitoring Failures**
   - Comprehensive logging
   - Security event monitoring
   - Audit trails

10. **A10: Server-Side Request Forgery (SSRF)**
    - Input validation for URLs
    - Whitelist-based URL filtering
    - Network security controls

## 🚀 Features

### Assessment Requirements
- ✅ **User Profile Display**: Display authenticated user information (username, name, email, contact number, country)
- ✅ **Purchase Management**: Insert, view, and manage product purchases with validation
- ✅ **OIDC Authentication**: Login/logout using Auth0 Identity Provider
- ✅ **Access Control**: Token-based access control for user-specific operations
- ✅ **OWASP Top 10 Security**: Comprehensive security mitigations implemented
- ✅ **HTTPS Configuration**: Secure communication with SSL/TLS
- ✅ **Input Validation**: Calendar selection, delivery time/location validation
- ✅ **Business Logic**: Sunday exclusion, predefined product/district lists

### Core Features
- ✅ **Product Listing**: View all available products with images and details
- ✅ **Product Detail View**: Detailed view of individual products
- ✅ **Shopping Cart**: Add, remove, and manage cart items
- ✅ **Search Functionality**: Search products by name or keyword
- ✅ **Category Filtering**: Filter products by categories
- ✅ **Responsive Design**: Mobile-friendly UI with Bootstrap
- ✅ **Purchase History**: View past and upcoming deliveries
- ✅ **User Authentication**: OIDC-based authentication with Auth0
- ✅ **Redux State Management**: 3rd-party state management using Redux Toolkit
- ✅ **RESTful API**: Complete backend API with proper HTTP methods
- ✅ **Database Integration**: MySQL database with JPA
- ✅ **Image Upload**: Product image management
- ✅ **Stock Management**: Real-time stock quantity tracking

## 🏗️ Architecture

### High-Level Architecture Diagram

```
┌─────────────────┐    HTTP/REST    ┌─────────────────┐    Database    ┌─────────────────┐
│   React.js      │ ◄────────────► │   Spring Boot   │ ◄────────────► │   PostgreSQL    │
│   Frontend      │                 │   Backend       │                 │   Database      │
│                 │                 │                 │                 │                 │
│ ┌─────────────┐ │                 │ ┌─────────────┐ │                 │ ┌─────────────┐ │
│ │   Redux     │ │                 │ │ Controllers │ │                 │ │   Users     │ │
│ │   Store     │ │                 │ │             │ │                 │ │             │ │
│ └─────────────┘ │                 │ └─────────────┘ │                 │ │   Products  │ │
│ ┌─────────────┐ │                 │ ┌─────────────┐ │                 │ │             │ │
│ │ Components  │ │                 │ │  Services   │ │                 │ │   Cart      │ │
│ │             │ │                 │ │             │ │                 │ │             │ │
│ │ - Home      │ │                 │ │ - Auth      │ │                 │ └─────────────┘ │
│ │ - Product   │ │                 │ │ - Product   │ │                 │                 │
│ │ - Cart      │ │                 │ │ - Cart      │ │                 │                 │
│ │ - Login     │ │                 │ │ - User       │ │                 │                 │
│ │ - Register  │ │                 │ └─────────────┘ │                 │                 │
│ └─────────────┘ │                 │ ┌─────────────┐ │                 │                 │
│ ┌─────────────┐ │                 │ │ Repositories│ │                 │                 │
│ │   Axios     │ │                 │ │             │ │                 │                 │
│ │ HTTP Client │ │                 │ └─────────────┘ │                 │                 │
│ └─────────────┘ │                 │ ┌─────────────┐ │                 │                 │
└─────────────────┘                 │ │   Models    │ │                 │                 │
                                   │ │             │ │                 │                 │
                                   │ │ - User      │ │                 │                 │
                                   │ │ - Product   │ │                 │                 │
                                   │ │ - Cart      │ │                 │                 │
                                   │ └─────────────┘ │                 │                 │
                                   └─────────────────┘                 └─────────────────┘
```

### Technology Stack

**Frontend:**
- React.js 18
- Redux Toolkit (State Management)
- React Router DOM
- Axios (HTTP Client)
- Bootstrap 5 (UI Framework)

**Backend:**
- Spring Boot 3.3.3
- Spring Data JPA
- Spring Security
- PostgreSQL/H2 Database
- Maven (Build Tool)

## 📦 Installation & Setup

### Prerequisites
- Java 21 or higher
- Node.js 16 or higher
- npm or yarn
- MySQL 8.0 or higher
- Auth0 account (for OIDC authentication)
- SSL certificate (for HTTPS)

### Auth0 Configuration

1. **Create Auth0 Application**
   - Sign up at [Auth0](https://auth0.com)
   - Create a new Single Page Application
   - Note down your Domain, Client ID, and Client Secret

2. **Configure Auth0 Settings**
   ```
   Allowed Callback URLs: http://localhost:5173/callback
   Allowed Logout URLs: http://localhost:5173
   Allowed Web Origins: http://localhost:5173
   ```

3. **Update Application Properties**
   ```properties
   # Replace with your Auth0 domain
   spring.security.oauth2.resourceserver.jwt.issuer-uri=https://your-domain.auth0.com/
   spring.security.oauth2.resourceserver.jwt.audiences=your-api-identifier
   ```

### Database Setup

1. **Create MySQL Database**
   ```bash
   mysql -u root -p
   ```
   
2. **Run Database Creation Script**
   ```bash
   mysql -u root -p < database_creation_script.sql
   ```

3. **Verify Database Creation**
   ```sql
   USE ecommerce_swees;
   SHOW TABLES;
   SELECT COUNT(*) FROM products;
   ```

### Backend Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd E-Com
   ```

2. **Navigate to backend directory**
   ```bash
   cd Ecommerce-Backend
   ```

3. **Configure application properties**
   Edit `src/main/resources/application.properties`:
   ```properties
   # Database Configuration
   spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_swees?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
   spring.datasource.username=your_mysql_username
   spring.datasource.password=your_mysql_password
   
   # Auth0 Configuration (Replace with your values)
   spring.security.oauth2.resourceserver.jwt.issuer-uri=https://your-domain.auth0.com/
   spring.security.oauth2.resourceserver.jwt.audiences=your-api-identifier
   ```

4. **Build and run the application**
   ```bash
   # Using Maven
   ./mvnw clean package
   ./mvnw spring-boot:run
   
   # Or using IDE
   # Run EcomProjApplication.java
   ```

   The backend will start on `http://localhost:8080`

### Frontend Setup

1. **Navigate to frontend directory**
   ```bash
   cd Ecommerce-Frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Start the development server**
   ```bash
   npm run dev
   ```

   The frontend will start on `http://localhost:5173`

## 🎯 API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login

### Products
- `GET /api/products` - Get all products
- `GET /api/product/{id}` - Get product by ID
- `POST /api/product` - Add new product
- `PUT /api/product/{id}` - Update product
- `DELETE /api/product/{id}` - Delete product
- `GET /api/products/search?keyword={keyword}` - Search products
- `GET /api/product/{id}/image` - Get product image

### Cart
- `GET /api/cart/{userId}` - Get user's cart
- `POST /api/cart/add` - Add item to cart
- `PUT /api/cart/{userId}/product/{productId}` - Update cart quantity
- `DELETE /api/cart/{userId}/product/{productId}` - Remove item from cart
- `DELETE /api/cart/{userId}` - Clear cart
- `GET /api/cart/{userId}/total` - Get cart total

## 🔐 Authentication Flow

1. **Registration**: Users can create new accounts with email, password, and personal details
2. **Login**: Users authenticate with email and password
3. **Session Management**: JWT tokens for secure authentication
4. **Protected Routes**: Cart and user-specific features require authentication

## 🛒 Shopping Cart Features

- Add products to cart with quantity selection
- Real-time stock validation
- Cart persistence across sessions
- Quantity management (increase/decrease)
- Remove items from cart
- Cart total calculation
- Checkout process with stock updates

## 🎨 UI/UX Features

- **Responsive Design**: Works on desktop, tablet, and mobile
- **Dark/Light Theme**: Toggle between themes
- **Search Functionality**: Real-time product search
- **Category Filtering**: Filter products by category
- **Product Images**: High-quality product images
- **Loading States**: Smooth loading animations
- **Error Handling**: User-friendly error messages

## 📁 Project Structure

```
SpringBoot-Reactjs-Ecommerce/
├── Ecommerce-Backend/
│   ├── src/main/java/com/cart/ecom_proj/
│   │   ├── controller/
│   │   │   ├── ProductController.java
│   │   │   ├── AuthController.java
│   │   │   └── CartController.java
│   │   ├── service/
│   │   │   ├── ProductService.java
│   │   │   ├── UserService.java
│   │   │   └── CartService.java
│   │   ├── model/
│   │   │   ├── Product.java
│   │   │   ├── User.java
│   │   │   └── Cart.java
│   │   ├── repo/
│   │   │   ├── ProductRepo.java
│   │   │   ├── UserRepo.java
│   │   │   └── CartRepo.java
│   │   └── config/
│   │       └── SecurityConfig.java
│   └── src/main/resources/
│       └── application.properties
├── Ecommerce-Frontend/
│   ├── src/
│   │   ├── components/
│   │   │   ├── Home.jsx
│   │   │   ├── Product.jsx
│   │   │   ├── Cart.jsx
│   │   │   ├── Login.jsx
│   │   │   ├── Register.jsx
│   │   │   └── Navbar.jsx
│   │   ├── store/
│   │   │   ├── store.js
│   │   │   └── slices/
│   │   │       ├── authSlice.js
│   │   │       ├── cartSlice.js
│   │   │       └── productSlice.js
│   │   └── App.jsx
│   └── package.json
└── README.md
```

## 🚀 Deployment

### Production Deployment on Tomcat Server

#### Backend Deployment

1. **Build WAR file for Tomcat**
   ```bash
   cd Ecommerce-Backend
   ./mvnw clean package -Pproduction
   ```

2. **Configure for Tomcat**
   - Copy `target/ecom-proj-0.0.1-SNAPSHOT.war` to Tomcat's `webapps` directory
   - Rename to `ecommerce.war`

3. **Configure SSL/HTTPS**
   ```bash
   # Generate SSL certificate (for development)
   keytool -genkey -alias tomcat -keyalg RSA -keystore tomcat.keystore
   
   # Configure Tomcat server.xml
   <Connector port="8443" protocol="org.apache.coyote.http11.Http11NioProtocol"
              maxThreads="150" SSLEnabled="true">
       <SSLHostConfig>
           <Certificate certificateKeystoreFile="conf/tomcat.keystore"
                        certificateKeystorePassword="changeit"
                        type="RSA" />
       </SSLHostConfig>
   </Connector>
   ```

4. **Environment Configuration**
   ```properties
   # Production application.properties
   spring.profiles.active=production
   spring.datasource.url=jdbc:mysql://your-db-host:3306/ecommerce_swees
   spring.datasource.username=${DB_USERNAME}
   spring.datasource.password=${DB_PASSWORD}
   spring.security.oauth2.resourceserver.jwt.issuer-uri=${AUTH0_ISSUER_URI}
   spring.security.oauth2.resourceserver.jwt.audiences=${AUTH0_AUDIENCE}
   server.ssl.enabled=true
   server.port=8443
   ```

#### Frontend Deployment

1. **Build for production**
   ```bash
   cd Ecommerce-Frontend
   npm run build
   ```

2. **Configure Auth0 for production**
   ```javascript
   // Update Auth0 configuration
   const auth0Config = {
     domain: 'your-domain.auth0.com',
     clientId: 'your-client-id',
     redirectUri: 'https://your-domain.com/callback',
     audience: 'your-api-identifier'
   };
   ```

3. **Deploy to web server**
   - Copy `dist` folder contents to Tomcat's `webapps/ecommerce` directory
   - Configure reverse proxy if needed

### Docker Deployment

1. **Backend Dockerfile**
   ```dockerfile
   FROM openjdk:21-jdk-slim
   COPY target/ecom-proj-0.0.1-SNAPSHOT.jar app.jar
   EXPOSE 8080
   ENTRYPOINT ["java", "-jar", "/app.jar"]
   ```

2. **Frontend Dockerfile**
   ```dockerfile
   FROM nginx:alpine
   COPY dist/ /usr/share/nginx/html/
   COPY nginx.conf /etc/nginx/nginx.conf
   EXPOSE 80
   ```

3. **Docker Compose**
   ```yaml
   version: '3.8'
   services:
     backend:
       build: ./Ecommerce-Backend
       ports:
         - "8080:8080"
       environment:
         - SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/ecommerce_swees
         - SPRING_DATASOURCE_USERNAME=root
         - SPRING_DATASOURCE_PASSWORD=password
     
     frontend:
       build: ./Ecommerce-Frontend
       ports:
         - "80:80"
       depends_on:
         - backend
     
     db:
       image: mysql:8.0
       environment:
         - MYSQL_ROOT_PASSWORD=password
         - MYSQL_DATABASE=ecommerce_swees
       volumes:
         - ./database_creation_script.sql:/docker-entrypoint-initdb.d/init.sql
   ```

## 🧪 Testing

### Backend Testing
```bash
cd Ecommerce-Backend
./mvnw test
```

### Frontend Testing
```bash
cd Ecommerce-Frontend
npm test
```

## 🔐 Security Considerations

### Configuration Security
- **Environment Variables**: Store sensitive data (DB credentials, Auth0 secrets) in environment variables
- **SSL/TLS**: Always use HTTPS in production
- **Database Security**: Use strong passwords and limit database access
- **Auth0 Configuration**: Regularly rotate client secrets and review permissions

### Runtime Security
- **Input Validation**: All user inputs are validated and sanitized
- **Rate Limiting**: API endpoints are protected against abuse
- **Security Headers**: Comprehensive security headers implemented
- **Error Handling**: Secure error messages without sensitive information exposure

### Monitoring and Logging
- **Security Logging**: All authentication and authorization events are logged
- **Audit Trails**: User actions are tracked for security analysis
- **Health Checks**: Application health monitoring with Spring Actuator

## 📝 Assessment Requirements Checklist

### Core Requirements
- ✅ **User Profile Display** - Display authenticated user information from JWT token
- ✅ **Purchase Management** - Insert, view, and manage product purchases
- ✅ **OIDC Authentication** - Login/logout using Auth0 Identity Provider
- ✅ **Access Control** - Token-based access control for user-specific operations
- ✅ **OWASP Top 10 Security** - Comprehensive security mitigations implemented
- ✅ **HTTPS Configuration** - Secure communication with SSL/TLS
- ✅ **Input Validation** - Calendar selection, delivery time/location validation
- ✅ **Business Logic** - Sunday exclusion, predefined product/district lists

### Technical Requirements
- ✅ **Database Integration** - MySQL database with comprehensive schema
- ✅ **RESTful API** - Complete backend API with proper HTTP methods
- ✅ **Frontend Integration** - React.js with Redux state management
- ✅ **Security Headers** - OWASP security headers implementation
- ✅ **Rate Limiting** - API protection against abuse
- ✅ **Input Sanitization** - XSS and injection prevention
- ✅ **Error Handling** - Secure error messages and logging

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Harish Kumar Gatti**
- LinkedIn: [Harish Kumar Gatti](https://www.linkedin.com/in/harish-kumar-gatti-663066249/)
- GitHub: [Your GitHub Profile]

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- React.js team for the frontend library
- Redux Toolkit team for state management
- Bootstrap team for the UI framework

