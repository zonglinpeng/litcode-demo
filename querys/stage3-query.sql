-- Stage 3: Two advanced queries

-- 1. Tag of question that users liked the most, select tag_id, and the number of likes for that tag
SELECT H.tag_id, T.tag_name, SUM(Q.likes) Likes
FROM TagHaveQuestion H JOIN Question Q ON(Q.id=H.question_id) JOIN Tag T ON(T.id = H.tag_id)
GROUP BY H.tag_id
HAVING Likes >= all(
 SELECT SUM(Q1.likes)
 FROM TagHaveQuestion H1 JOIN Question Q1 ON(Q1.id=H1.question_id)
 GROUP BY H1.tag_id
);

-- after deleting tag_id
SELECT T.tag_name, SUM(Q.likes) Likes
FROM TagHaveQuestion H JOIN Question Q ON(Q.id=H.question_id) JOIN Tag T ON(T.tag_name = H.tag_name)
GROUP BY H.tag_name
HAVING Likes >= all(
    SELECT SUM(Q1.likes)
    FROM TagHaveQuestion H1 JOIN Question Q1 ON(Q1.id=H1.question_id)
    GROUP BY H1.tag_name
);


-- 2. A list of question_id and qestion title with a specific tag is like by a given user or created by the user.
-- user_id = 1, tag_name = "Array"
set @UserId = 1;
set @TagName = "Array";
(
    SELECT Q.id, Q.title
    FROM Question Q JOIN TagHaveQuestion H on(Q.id = H.question_id) JOIN Tag T on (H.tag_id = T.id) JOIN UserReactionQuestion R using (question_id)
    WHERE T.tag_name = @TagName and R.reaction_type = "like" and R.user_id = @UserId
)
UNION
(
    SELECT Q.id, Q.title
    FROM Question Q JOIN TagHaveQuestion H on(Q.id = H.question_id) JOIN Tag T on (H.tag_id = T.id) JOIN UserContributeQuestion C using (question_id)
    WHERE T.tag_name = @TagName and C.user_id = @UserId
);

-- after deleting tag_id from ddl
set @UserId = 1;
set @TagName = "Array";
(
    SELECT Q.id, Q.title
    FROM Question Q JOIN TagHaveQuestion H on(Q.id = H.question_id) JOIN Tag T on (H.tag_name = T.tag_name) JOIN UserReactionQuestion R using (question_id)
    WHERE T.tag_name COLLATE utf8mb4_unicode_ci = @TagName and R.reaction_type = "like" and R.user_id = @UserId
)
UNION
(
    SELECT Q.id, Q.title
    FROM Question Q JOIN TagHaveQuestion H on(Q.id = H.question_id) JOIN Tag T on (H.tag_name = T.tag_name) JOIN UserContributeQuestion C using (question_id)
    WHERE T.tag_name COLLATE utf8mb4_unicode_ci = @TagName and C.user_id = @UserId
);

