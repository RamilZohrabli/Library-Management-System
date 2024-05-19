# Book Library Management System

This project is a Book Library Management System implemented in Java with a graphical user interface (GUI) using Swing. It allows users to interact with a database of books and manage personal libraries. The application includes different interfaces for admins and regular users, with functionality such as adding, editing, and deleting books, managing personal libraries, rating books, writing reviews, and more.

## Table of Contents
- [Project Overview](#project-overview)
- [Features](#features)
- [Classes and Structure](#classes-and-structure)
- [Getting Started](#getting-started)
- [Usage](#usage)

## Project Overview
The system has two primary users: admins and regular users. Admins have elevated permissions to manage the general book database, while regular users can maintain their personal libraries and interact with the general database. The project is designed with multiple Java classes that define the behavior and relationships among different components.

## Features
- **Admin Interface**: Admins can add, edit, and delete books in the general book database.
- **General Database**: Contains a collection of books, accessible by both admins and regular users.
- **Personal Database**: Each regular user has their personal library to manage their book collection.
- **Book Management**: Ability to add, edit, and delete books in the general database (for admins) and the personal database (for regular users).
- **Ratings and Reviews**: Users can rate and review books in their personal library.
- **Search and Sort**: Users can search for books in the general database and sort the results.
- **Login and Registration**: Secure login and user registration functionality, with admin and regular user roles.

## Classes and Structure
Below is a brief description of the key classes and their roles in the system:

- **GeneralBook**: Represents a book in the general database. Contains information like title, author, average rating, and reviews.
- **PersonalBook**: Inherits from `GeneralBook` and adds additional attributes specific to a personal library, such as status, time spent reading, user ratings, and user reviews.
- **GeneralDatabase**: Manages the collection of `GeneralBook` objects. Provides methods to add, remove, and update books, as well as to load and save data from/to a CSV file.
- **PersonalDatabase**: Manages the collection of `PersonalBook` objects for a specific user. Supports saving and loading data from/to a CSV file based on the current user.
- **AdminInterface**: The admin interface for managing the general book database. Allows admins to add, edit, and delete books.
- **GeneralDatabaseGUI**: GUI for interacting with the general book database. Allows users to search, sort, and add books to their personal library.
- **PersonalDatabaseGUI**: GUI for interacting with a user's personal library. Allows users to rate, review, change book status, and delete books from their library.
- **LoginAndRegistrationPage**: Interface for user login and registration, with support for admin and regular user roles.
- **MainApp**: Entry point for the application, managing transitions between login, registration, and the main interfaces.

## Getting Started
To run the project, follow these steps:

1. **Clone the Repository**: Download the project from its repository.
2. **Compile the Java Files**: Use your preferred Java IDE or command line to compile all Java files.
3. **Run the Application**: Execute the `MainApp` class to start the application.

## Usage
- **Admins**: After logging in as an admin, you can manage the general book database using the Admin Interface.
- **Regular Users**: After logging in as a regular user, you can view the general database and manage your personal library. You can add books to your library, rate them, write reviews, and track your reading progress.
- **Logout**: To log out, use the "Logout" button in the Main Interface.


