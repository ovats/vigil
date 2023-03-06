# Users
curl -X 'POST' 'http://localhost:8080/users' -d '{"name": "Carl Sagan", "email": "c.sagan@mail.com"}'
curl -X 'POST' 'http://localhost:8080/users' -d '{"name": "Albert Einstein", "email": "a.einstein@mail.com"}'
curl -X 'POST' 'http://localhost:8080/users' -d '{"name": "Julia Roberts", "email": "j.roberts@mail.com"}'

# Posts
curl -X 'POST' 'http://localhost:8080/posts' -d '{"userId": 1, "title": "Post #1, userId 1", "createdAt": "2023-02-03T18:20:28.661Z"}'
curl -X 'POST' 'http://localhost:8080/posts' -d '{"userId": 2, "title": "Post #2, userId 2", "createdAt": "2023-02-04T20:43:37.532Z"}'
curl -X 'POST' 'http://localhost:8080/posts' -d '{"userId": 3, "title": "Post #3, userId 3", "createdAt": "2023-02-05T14:10:08.123Z"}'

# Comments
curl -X 'POST' 'http://localhost:8080/posts/1/comment'  -d '{  "userId": 3,  "text": "comment on post #1, userId 3 (01)", "createdAt": "2020-04-06T23:21:45.135Z" }'
curl -X 'POST' 'http://localhost:8080/posts/1/comment'  -d '{  "userId": 3,  "text": "comment on post #1, userId 3 (02)", "createdAt": "2020-04-07T21:21:44.134Z" }'
curl -X 'POST' 'http://localhost:8080/posts/1/comment'  -d '{  "userId": 3,  "text": "comment on post #1, userId 3 (03)", "createdAt": "2020-04-08T20:21:43.133Z" }'
curl -X 'POST' 'http://localhost:8080/posts/2/comment'  -d '{  "userId": 2,  "text": "comment on post #2, userId 2 (04)", "createdAt": "2020-04-09T19:20:42.132Z" }'
curl -X 'POST' 'http://localhost:8080/posts/2/comment'  -d '{  "userId": 2,  "text": "comment on post #2, userId 2 (05)", "createdAt": "2020-04-10T18:20:41.132Z" }'
