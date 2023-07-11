 DELIMITER // 
CREATE TRIGGER ReactUpdateTrigger
BEFORE UPDATE ON UserReactionQuestion
	FOR EACH ROW
	BEGIN
		SET @prev_react = (SELECT reaction_type FROM UserReactionQuestion
                    WHERE question_id = new.question_id AND user_id = new.user_id);

        SET @new_react = new.reaction_type;

        IF @prev_react = "dislike" AND @new_react = "like" THEN
            UPDATE Question 
            SET  likes = likes + 1, dislikes = dislikes - 1
            WHERE id = new.question_id;
        ELSEIF @prev_react = "like" AND @new_react = "dislike" THEN
            UPDATE Question 
            SET  dislikes = dislikes + 1, likes = likes - 1
            WHERE id = new.question_id;
        END IF;
    END;//
 DELIMITER ;




DELIMITER // 
CREATE TRIGGER ReactInsertTrigger
BEFORE INSERT ON UserReactionQuestion
	FOR EACH ROW
	BEGIN
		SET @prev_react = (SELECT reaction_type FROM UserReactionQuestion
                    WHERE question_id = new.question_id AND user_id = new.user_id);

        SET @new_react = new.reaction_type;

        IF @prev_react IS NULL AND @new_react = "like" THEN
            UPDATE Question 
            SET likes = likes + 1
            WHERE id = new.question_id;
        ELSEIF @prev_react IS NULL AND @new_react = "dislike" THEN
            UPDATE Question 
            SET  dislikes = dislikes + 1
            WHERE id = new.question_id;
        END IF;
    END;//
DELIMITER ;

DELIMITER // 
CREATE TRIGGER ReactDeleteTrigger
BEFORE DELETE ON UserReactionQuestion
	FOR EACH ROW
	BEGIN
		SET @prev_react = (SELECT reaction_type FROM UserReactionQuestion
                    WHERE question_id = old.question_id AND user_id = old.user_id);

        IF @prev_react = 'like' THEN
            UPDATE Question 
            SET likes = likes - 1
            WHERE id = old.question_id;
        ELSEIF @prev_react = "dislike" THEN
            UPDATE Question 
            SET  dislikes = dislikes - 1
            WHERE id = old.question_id;
        END IF;
    END;//
DELIMITER ;