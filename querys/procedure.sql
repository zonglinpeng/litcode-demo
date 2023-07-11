drop procedure create_update_transaction;
DELIMITER //
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;

CREATE PROCEDURE create_update_transaction(
    IN INisUpdate VARCHAR(100),
    IN INtitle VARCHAR(100),
    IN INdescription VARCHAR(1000),
    IN INcode_content VARCHAR(100),
    IN INhint VARCHAR(100),
    IN INdifficulty VARCHAR(50),
    IN INquestion_id INT,
    IN INtagName VARCHAR(100),
    OUT OUTqid INT,
    OUT OUTtag_name VARCHAR(100)
    )
    BEGIN
        -- DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;

        START TRANSACTION;

        SET @maxid = (SELECT max(id) FROM Question);

        IF INisUpdate = "false" THEN
            INSERT INTO Question (title,description,code_content,hint,difficulty) 
            VALUES (INtitle, INdescription, INcode_content, INhint, INdifficulty);

            INSERT INTO TagHaveQuestion (question_id, tag_name) VALUES (@maxid + 1, INtagName);

            SET OUTqid = @maxid + 1;
        ELSE 
            UPDATE Question SET title = INtitle,
                                description = INdescription,
                                code_content = INcode_content,
                                hint = INhint,
                                difficulty = INdifficulty
                    WHERE id = INquestion_id; 

            UPDATE TagHaveQuestion SET tag_name = INtagName
                    WHERE question_id = INquestion_id;

            SET OUTqid = INquestion_id;
            
        END IF;

        COMMIT; 
        SET OUTtag_name = (SELECT tag_name from TagHaveQuestion where question_id = OUTqid );

    END //

DELIMITER ;


--  
call create_update_transaction(
    "true", 
    "ABC",
    "description",
    "code",
    "hint",
    "difficult",
    3,
    "Hash Table",
    @qid,
    @tag_name
);

select @qid, @tag_name;
