# Project Description of Litcode

## Project Summary

Litcode is the platform to help you enhance your programming skills, expand your knowledge and prepare for technical interviews.

Through Litcode, developers can not only practice their programming skills by solving the challenges across the platform.

Developers can also design and propose their own challenges by the standardized unit test framework. 

## Project Detailed Description

Litcode is a community-based platform designed for software developers. It aims to help developers enhance their programming skills, expand their knowledge and prepare for technical interviews in a more interactive manner. Software developers nowadays are used to solving off-the-shelf problems for interview preparation, however, thinking from the interviewer's perspective is usually neglected. And Litcode is here to help. Litcode maintains a database for coding challenges with not only classic and common coding challenges but also user defined questions. On this platform, each user can design and propose their own questions as well as test cases. Users can also create question lists based on their own preferences. Other than that, users can also search questions based on difficulty level, keywords or topic. To solve questions, Litcode supports online code editing and testing in the Python programming language. 

## Usefulness

### Usefulness description

As professional software engineers who will be contributing to the cutting-edge technology in the modern industry we need to keep sharpening our skills and expertise in utilizing algorithms to solve real world problems, meanwhile optimizing our solutions to improve efficiency. 

Litcode is the platform that collects thousands of entries of mathametic or algorithmic problems that orient from real life and fall under different topics. In the process of uploading their problem sets on litcode, users are able to think and rethink on better solutions on the current problem and, more importantly, derive similar questions in retrospect.

Litcode also encourages other users to visit and solve your questions set, furthermore, challenging themselves to come up with a solution with better time and space complexity.

### Similar applications and how yours is different?

Leetcode is the most famous and popular website that has similar functions with our project. We also get inspired by Leetcode. However, we do have some differences in functions which we think are better than Leetcode for coding enthusiasts and programming learners. 

First, our application is totally community based. We will provide the basic database, then every user can start coding with it. Users can submit their own test case for testing. These test cases will update to our database, So other users can see the test cases and use them later. Therefore, users not only can train their ability on programming, but also can train their ability about software testing. 

Second, every user can submit their own questions to our database, and share them with other users. Users can share their brilliant ideas with other one, so that we can work together and improve our skills. 

Realness: How and where you’ll get the data?

Our questions data source will come from Leetcode's unofficial graphql API. These environments will only be used for testing, and we will not use these data in a commercial use case. Other data will be derived directly from users, and users will create their own content. In the test environment, we will use mock data. 

### Description of the Functionalities

1.Describe what data is stored in the database. (Where is the data from, what attributes and information would be stored?)

Our questions data source will come from Leetcode's unofficial graphql API. These environments will only be used for testing, and we will not use these data in a commercial use case. Other data will be derived directly from users, and users will create their own content. In the test environment, we will use mock data. 

For questions, we will store id, title, content, difficulty, author, tags, code snippets.

For questions collection, we will store question id, collection creator.

For users, we will use OAuth2, so we will store user id, refresh token and access token and user profiles.

2. What are the basic functions of your web application? (What can users of this website do? What simple and complex features are there?)

Users could log in to the Litcode platform through their Google Account. A User has to login to access the problems that were either previously created by the user or the created by other users. 

Users could search for questions and questions collections that already exist in the Litcode platform. These questions are created or maintained by the official or user.

Users can solve the problems already existing on Litcode. The questions can be either created by the other users or previously created by himself. Solutions will be submitted to the Litcode so that later on every user can see the solutions of the question.

Users can also create new problems at their will, including the problem statement, unit test cases, and also his solution to the problem. Note that a user can only create the statement and the unit tests, but not the solution.

3. What would be a good creative component (function) that can improve the functionality of your application? (What is something cool that you want to include? How are you planning to achieve it?)

Since there are expected to be thousands of questions and more are to be added by users, it will be nice if we can apply a custom filter when searching for a category of questions. 

To build up a structured learning system, we believe categorizing the questions by their topics is a good functionality so that users can practice their coding skills by topics. It is nice to add tag(s) that are preset in Litcode to each of the questions so that later on users can search the question for its tag(s).

4. A low fidelity UI mockup: (a sketch) of your interface (at least two images)


## Project work distribution

### Zonglin Peng:

- Developing and maintaining the infrastructures, i.e. the CI/CD pipelines of the website. 
- Additionally, he will be contributing to the backend APIs.

### Huiming Sun

- Backend, create service contract, documentation, and implementation of Litcode's RESTful API.
- DevOps, create CI/CD pipeline, manage database and deployment mechanism infrastructure

### Yifan Chen

- Frontend, user interface design, connect the front end to the database
- Keep track of the progress of all members, coordinate work among different members

### Zhimeng Pi

- Database system design and management
- Quality Assurance
- Collect data and prepare the report
