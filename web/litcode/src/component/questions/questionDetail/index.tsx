import {
    useParams
} from "react-router-dom";

import { useEffect, useState } from "react";
import api from '../../../api/litcode'
import { Question } from "../../../api/litcode/model/Question";
import ReactButtons from "./reactButtons";

const QuestionViewer = () => {
    let { questionID } = useParams<string>();
    const [q, setQ] = useState<Question>({
        id: "",
        title: "",
        description: "",
        code_content: "",
        hint: "",
        difficulty: "",
        likes: 0,
        dislikes: 0,
    })
    const questionAPI = api.question()

    useEffect(() => {
        (async () => {
            let q = await questionAPI.searchID(questionID!)
            console.log(q)
            setQ(q)
        })()
    }, [questionID, questionAPI])

    return (
        // add reaction button : thumbup and thumbdown
        <div>
            <div><ReactButtons questionId={questionID!} /> </div>
            <div>{questionID}: {q.title}</div>
            <div dangerouslySetInnerHTML={{ __html: q.description }}></div>
        </div>
    )
}

export default QuestionViewer