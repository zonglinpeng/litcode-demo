import json
from pathlib import Path
from typing import Dict, List, Tuple
from pprint import pprint
from dataclasses import dataclass
import pymysql.cursors
import pymysql
from contextlib import contextmanager
from contextlib import closing
import random


def exceptionCatcher(func):
    def wrap(*args, **kwargs):
        result = None
        try: 
            result = func(*args, **kwargs)
        except pymysql.err.IntegrityError:
            pass
        return result
    return wrap

    
@dataclass
class Question:
    id: int
    title: str
    description: str
    code_content: str
    hint: str
    difficulty: str
    likes: int
    dislikes: int


@dataclass
class Tag:
    tag_name: str


@dataclass
class TagHaveQuestion:
    tag_name: str
    question_id: int


@dataclass
class User:
    id: int
    oauth_id: str
    oauth_platform: str
    refresh_token: str


@dataclass
class UserContibuteQuestion:
    user_id: int
    questions_id: int


@dataclass
class UserReactionQuestion:
    user_id: int
    question_id: int
    reaction_type: str


class LeetcodeAssets:
    def __init__(self, path: Path) -> None:
        self._rootPath = path
        self._qTagsPath = path / "QuestionTags.json"
        self._qsPath = path / "questions"

    def qTags(self) -> Tuple[List[Tag], Dict[str, List[int]]]:
        tags = []
        tags_qs = dict()
        # i = 1
        with self._qTagsPath.open("r") as f:
            o = json.load(f)
            edges = o["questionTopicTags"]["edges"]
            for edge in edges:
                node = edge["node"]
                name = node["name"]
                qids = node["questionIds"]
                tags.append(Tag(name))
                tags_qs[name] = qids
                # i += 1
        return tags, tags_qs

    def qs(self) -> Dict[str, Question]:
        rst = dict()
        for fpath in self._qsPath.iterdir():
            with fpath.open("r") as f:
                o = json.load(f)
                q = o["question"]
                q_id = q["questionId"]
                q_title = q["title"]
                q_description = q["content"] if q["content"] != None else ""
                q_hint = q["hints"][0] if len(q["hints"]) != 0 else ""
                q_difficulty = q["difficulty"]
                q_likes = q["likes"]
                q_dislikes = q["dislikes"]

                q_code_content = ""
                q_codeSnippets = [] if q["codeSnippets"] == None else q["codeSnippets"]

                for q_codeSnippet in q_codeSnippets:
                    if q_codeSnippet["lang"] == "Python3":
                        q_code_content = q_codeSnippet["code"]

                rst[q_id] = Question(
                    q_id,
                    q_title,
                    q_description,
                    q_code_content,
                    q_hint,
                    q_difficulty,
                    q_likes,
                    q_dislikes,
                )
        return rst


class LeetcodeAssetsLoader:
    def __init__(self, host: str, user: str, passwd: str, database="litcode") -> None:
        self.conn = pymysql.connect(
            host=host, user=user, password=passwd, database=database
        )
    
    @exceptionCatcher
    def insertQ(self, q: Question) -> None:
        with self.conn.cursor() as cursor:
            sql = "INSERT INTO Question (id, title, description, code_content, hint, difficulty, likes, dislikes) VALUES (%s, %s, %s, %s, %s, %s, %s, %s);"
            cursor.execute(
                sql,
                (
                    q.id,
                    q.title,
                    q.description,
                    q.code_content,
                    q.hint,
                    q.difficulty,
                    q.likes,
                    q.dislikes,
                ),
            )
        self.conn.commit()

    @exceptionCatcher
    def insertTag(self, t: Tag) -> None:
        with self.conn.cursor() as cursor:
            sql = "INSERT INTO Tag (tag_name) VALUES (%s);"
            cursor.execute(sql, (t.tag_name))
        self.conn.commit()

    @exceptionCatcher
    def insertTagHaveQuestions(self, t: TagHaveQuestion) -> None:
        with self.conn.cursor() as cursor:
            sql = "INSERT INTO TagHaveQuestion (question_id, tag_name) VALUES (%s, %s);"
            cursor.execute(sql, (t.question_id, t.tag_name))
        self.conn.commit()

    @exceptionCatcher
    def insertUser(self, user: User) -> None:
        with self.conn.cursor() as cursor:
            sql = "INSERT INTO User (id, oauth_id, oauth_platform, refresh_token) VALUES (%s, %s, %s, %s);"
            cursor.execute(
                sql, (user.id, user.oauth_id, user.oauth_platform, user.refresh_token)
            )
        self.conn.commit()

    @exceptionCatcher
    def insertUserContributeQuestion(self, u: UserContibuteQuestion):
        with self.conn.cursor() as cursor:
            sql = "INSERT INTO UserContributeQuestion (user_id, question_id) VALUES (%s, %s);"
            cursor.execute(sql, (u.user_id, u.questions_id))
        self.conn.commit()

    @exceptionCatcher
    def insertUserReactionQuestion(self, u: UserReactionQuestion):
        with self.conn.cursor() as cursor:
            sql = "INSERT INTO UserReactionQuestion (user_id, question_id, reaction_type) VALUES (%s, %s, %s);"
            cursor.execute(sql, (u.user_id, u.question_id, u.reaction_type))
        self.conn.commit()

    def tags():
        pass

    def close(self) -> None:
        self.conn.close()


def main() -> None:
    leetcodeAssetsPath = "/Users/hsun/dev/leetcodeasset/asset"
    # leetcodeAssetsPath = "/mnt/j/cygwin64/home/zongl/ws/leetcodeasset/asset"
    # host = "35.232.9.106"
    host = "127.0.0.1"
    user = "root"
    passwd = "666"
    assets = LeetcodeAssets(Path(leetcodeAssetsPath))
    qtags, qtagQs = assets.qTags()
    qs = assets.qs()

    # pprint(qs)
    # pprint(qtags)
    # pprint(qtagQs)

    with closing(LeetcodeAssetsLoader(host, user, passwd)) as loader:
        loader: LeetcodeAssetsLoader
        # User
        loader.insertUser(User(1, "", "", ""))
        # Question
        for _, q in qs.items():
            loader.insertQ(q)
        # UserContributeQuestion
        for _, q in qs.items():
            loader.insertUserContributeQuestion(UserContibuteQuestion(1, q.id))
        # UserReactionQuestion
        for _, q in qs.items():
            reaction = random.choice(["like", "dislike"])
            loader.insertUserReactionQuestion(UserReactionQuestion(1, q.id, reaction))
        # Tag
        for t in qtags:
            loader.insertTag(t)
        # TagHaveQuestion
        seenTqs = set()
        for tname, tqs in qtagQs.items():
            for tq in tqs:
                if tq in seenTqs:
                    continue
                try:
                    loader.insertTagHaveQuestions(TagHaveQuestion(tname, tq))
                except pymysql.err.IntegrityError:
                    pass
                finally:
                    seenTqs.add(tq)


if __name__ == "__main__":
    main()
