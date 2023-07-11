import api from '../../../../api/litcode'
import { useEffect, useState } from "react";
import "./index.css";
import ThumbUpOutlinedIcon from '@mui/icons-material/ThumbUpOutlined';
import ThumbUpRoundedIcon from '@mui/icons-material/ThumbUpRounded';
import ThumbDownOutlinedIcon from '@mui/icons-material/ThumbDownOutlined';
import ThumbDownRoundedIcon from '@mui/icons-material/ThumbDownRounded';

interface ReactProps {
    questionId: string
}

const ReactButtons = (prop: ReactProps) => {
    const questionAPI = api.question()

    // const [tag, setTag] = useState<string>("")
    const [isliked, setLiked] = useState<boolean>()
    const [isdisliked, setDisliked] = useState<boolean>()
    const [likeCnt, setLikeCnt] = useState<Number>()
    const [dislikeCnt, setDislikeCnt] = useState<Number>()

    useEffect(() => {
        (async () => {
            // let t = await questionAPI.reactToQuestion(questionId)
            // get initial reaction_type, initialize the states
            console.log(prop.questionId)
            let react_type = await questionAPI.getReactToQuestion(prop.questionId)
            console.log(react_type, prop.questionId) // like or dislike

            if (react_type === "like") {
                setLiked(true)
                setDisliked(false)
            }
            else if (react_type === "dislike") {
                setLiked(false)
                setDisliked(true)
            }
            else { // no reaction
                setLiked(false)
                setDisliked(false)
            }
            
            let rst = await questionAPI.getReactCnt(prop.questionId)
            console.log("getReactCnt",rst)
            setLikeCnt(rst["likes"])
            setDislikeCnt(rst["dislikes"])

        })()
    }, [prop.questionId, questionAPI])


    const onClickReact = async (reactType: string) => {
        // to like/dislike it, it can be update the reaction or insert a new reaction
        if ((reactType === "like" && !isliked) || (reactType === 'dislike' && !isdisliked)) {
            let r = await questionAPI.reactToQuestion(prop.questionId, reactType, 1)
            if (r === "fail"){
                return
            }
            if (reactType === "like") {
                setLiked(true);
                setDisliked(false);
            }
            else {
                setLiked(false);
                setDisliked(true);
            }

        }
        else if ((reactType === "like" && isliked) || (reactType === "dislike" && isdisliked)) {
            // to delete like or dislike
            let r = await questionAPI.deleteLikeQuestion(prop.questionId, reactType, 1)
            if (r === "fail") {
                return
            }
            if (reactType === "like") {
                setLiked(false);
            }
            else {
                setDisliked(false);
            }
        }
        setTimeout(async () => {
            let rst = await questionAPI.getReactCnt(prop.questionId)
            console.log("getReactCnt", rst["likes"], rst["dislikes"])
            setLikeCnt(rst["likes"])
            setDislikeCnt(rst["dislikes"])
        }, 0.1); // wait 0.1 s before call the getReactCnt

    }

    return (
        <div>
            <a className="like btn" onClick={() => onClickReact("like")}>
                {isliked
                    ? <ThumbUpRoundedIcon /> 
                    : <ThumbUpOutlinedIcon />
                }
            </a>
            <b>{likeCnt} </b>
            <a className="dislike btn" onClick={() => onClickReact("dislike")}>
                {isdisliked
                    ? <ThumbDownRoundedIcon />
                    : <ThumbDownOutlinedIcon />
                }
            </a>
            <b>{dislikeCnt}</b>
        </div>
    )
}

export default ReactButtons