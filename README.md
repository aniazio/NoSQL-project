# NoSQL Capstone Project

## Starting the project

To start Cassandra containers please run `docker-compose up -d`. It will take a moment to start. 

When containers start, you can start an application and use REST API to interact with resources. For more information see `docs` folder.

To stop containers run `docker-compose down`. If you want to delete all data use `docker-compose down -v`.

## Description

REST API with Cassandra Database, for task manager. Database schema consist users and tasks. 

Basic CRUD operations are available:
- Create new user and create new task for a user
- Read all users, specified user's data, specified user's tasks, specified task
- Update task status
- Delete specified task

For more documentation see `docs` folder.
