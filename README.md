# Reddit Clone using Spring And Angular <img src="https://github.com/ahmedrafat-SW/reddit/assets/129176607/cf6b4719-2075-43f9-aee4-4657fd139c1a" width="30" height="30" >

**A vibrant online community where users can engage in discussions, share content, and discover a world of diverse perspectives.**
**Built with Angular for a sleek and dynamic front-end experience, paired with the robust Spring Boot framework and MySQL backend,**
**Our platform offers a seamless and responsive environment for users to connect.**

**Angular Client repo: https://github.com/ahmedrafat-SW/reddit-ui**
# API Documentation

## AuthController

### `POST /api/auth/sign-up`
Registers a new user.

- **Request Body**: `RegisterRequest`
- **Response**: `200 OK` with message "User Registration Successful"

### `GET /api/auth/accountVerification/{token}`
Verifies user account with the given token.

- **Path Variable**: `token` (String)
- **Response**: `200 OK` with message "Account is Activated Successfully."

### `POST /api/auth/login`
Authenticates a user and returns a JWT.

- **Request Body**: `LoginRequest`
- **Response**: `200 OK` with `AuthenticationResponse`

### `POST /api/auth/refresh/token`
Generates a new JWT using a refresh token.

- **Request Body**: `RefreshTokenRequest`
- **Response**: `201 Created` with `AuthenticationResponse`

### `POST /api/auth/logout`
Logs out the user and deletes the refresh token.

- **Request Body**: `RefreshTokenRequest`
- **Response**: `200 OK` with message "Refresh Token Successfully deleted."

## CommentController

### `POST /api/comments`
Creates a new comment.

- **Request Body**: `CommentDto`
- **Response**: `201 Created` with `CommentDto`

### `GET /api/comments/by-postId/{id}`
Gets comments by post ID.

- **Path Variable**: `id` (long)
- **Response**: `200 OK` with list of `CommentDto`

### `GET /api/comments/by-user/{name}`
Gets comments by username.

- **Path Variable**: `name` (String)
- **Response**: `200 OK` with list of `CommentDto`

## PostController

### `POST /api/posts`
Creates a new post.

- **Request Body**: `PostRequest`
- **Response**: `201 Created` with `PostResponse`

### `GET /api/posts`
Gets all posts.

- **Response**: `200 OK` with list of `PostResponse`

### `GET /api/posts/{id}`
Gets a post by ID.

- **Path Variable**: `id` (int)
- **Response**: `200 OK` with `PostResponse`

### `GET /api/posts/by-subreddit/{id}`
Gets posts by subreddit ID.

- **Path Variable**: `id` (int)
- **Response**: `200 OK` with list of `PostResponse`

### `GET /api/posts/by-user/{name}`
Gets posts by username.

- **Path Variable**: `name` (String)
- **Response**: `200 OK` with list of `PostResponse`

## SubredditController

### `POST /api/subreddit`
Creates a new subreddit.

- **Request Body**: `SubredditDto`
- **Response**: `201 Created` with `SubredditDto`

### `GET /api/subreddit/{id}`
Gets a subreddit by ID.

- **Path Variable**: `id` (int)
- **Response**: 
  - `200 OK` with `SubredditDto`
  - `404 Not Found` with message "Can't find this subreddit"

### `GET /api/subreddit`
Gets all subreddits.

- **Response**: `200 OK` with list of `SubredditDto`

## VoteController

### `POST /api/votes`
Creates a vote.

- **Request Body**: `VoteDto`
- **Response**: `201 Created`
