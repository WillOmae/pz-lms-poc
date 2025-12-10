# Pezesha LMS POC

## Overview

Build a production-grade double-entry accounting system that serves as the foundation for loan
operations in a fintech platform. This system must handle high transaction volumes while maintaining
absolute data integrity.

## Implementation

### Tech stack:

- Language: Java 21
- Framework: Spring Boot 4
- RDBMS: PostgreSQL
- Testing: JUnit, Testcontainers
- Containerization: Docker and Docker Compose

### Features

- Loan operations: disbursement, repayment, write-offs
- Double-entry accounting
- Highly configurable entities
- Role-based Access Control (RBAC)
- Audit logging

## Decisions and assumptions

### Code structure

The application is structured into the following domains:

- accounts: operations on accounts
- auditlogs: configurations enabling audit logging
- common: shared data/functions
- products: operations on products
- reports: operations on reports
- security: authentication and authorization
- transactions: operations on transactions
- users: operations on users

Within each domain, additional packages are created:

- data: contains entities and repositories
- services: contains business logic
- dtos: contains data transfer objects
- router: contains REST endpoints

### Multicurrency support

A single account can maintain multiple currency balances. This prevents frequent forex calculations and balance
fluctuations.

### User management

To properly handle users/entities in the system, the following configuration entities have been created:

- Permissions: basic configuration specifying whether an operation can be performed by a user
- Roles: grouping of permissions and assignable to users
- Users: actual entities that can perform actions/transactions in the system
- Types: basic classification of users (CUSTOMER, PARTNER, API_USER, INTERNAL_USER, SYSTEM)

### Account management

To properly define the intent of accounts, the following configuration entities have been created:

- Currency: currencies supported as defined by the ISO 4217 standard
- Account types: basic classification of accounts showing intent and behaviour on debit and credit. Can be chained in
  hierarchy leading up to the standard account types (ASSET, LIABILITY, EQUITY, INCOME, EXPENSE)
- Accounts: actual storage of funds involved in transactions. Can contain multiple currency balances
- Account balances: actual storage of account balances in a specific currency

### Transaction management

To properly define the intent of transactions and the accounts to be affected, the following configuration entities have
been created:

- Transaction types: basic transaction unit showing the accounts to be debited and credited
- Reason types: bundling of transaction types to show the reason for the transaction

### Auditing

To ensure data integrity, auditing is enabled on all entities. This is done at the database level through the use of
triggers on insert, update and delete operations. The application adds the current user during transactions to be added
to the audit record.

Can be improved to include read access auditing.

### Reporting

Work in progress to achieve accurate reporting.

### Testing

Integration tests are used to verify the correctness of the application end-to-end. Testcontainers are used to isolate
the database from the host machine while still using same kind of database as the production environment.

### Data validation

To ensure data integrity, data validation is done at both the application and database layers.

- application: input data is validated using JSR 380 Bean Validation
- database: foreign key constraints are enforced

### Containerization

To ensure the application is portable, the application uses Docker and Docker Compose. Both the application and database
can be started by running `docker-compose up` or `docker-compose up --build` from the root of the project.

The database is initialized with the required schema and seeded with data defined in resources.

### Performance

Database indexes are created to improve performance. Profiling is needed to identify potential bottlenecks.

## Not in scope

- Scoring
- Loan approvals
- Interest calculation jobs
- Actual transfer of funds via mobile money
- Notifications to users