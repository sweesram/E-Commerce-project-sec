# Secure E-Commerce Web Application

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

