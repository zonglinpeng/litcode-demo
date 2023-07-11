import { Stack } from "@mui/material"

import Item from '../item'
import ListView from "../profile/listView";
import api from '../../api/litcode'
import { Container } from '@mui/material';
import { Question } from "../../api/litcode/model/Question";
import { useEffect, useState } from "react";
import MenuItem from "@mui/material/MenuItem";
import Select from "@mui/material/Select";
import { tagOptions } from "./data";
import './index.css'

const TagQuestions = () => {
    const [qs, setQs] = useState<Question[]>([])
    const [tag, setTag] = useState<string>("Array")

    const questionAPI = api.question()
    const userAPI = api.user()

    const onTagChange = async (e: any) => {
        const t = e.target.value
        setTag(t)
        let qs = await userAPI.userRelateQuestion({
            userID: 1,
            tagName: t,
        })
        console.log(qs)
        setQs(qs)
    }

    useEffect(() => {
        (async () => {
            let qs = await questionAPI.search(0, 50)
            console.log(qs)
            setQs(qs)
        })()
    }, [questionAPI])

    return (
        <Container >
            <Stack className="tag-questions-main" spacing={2}>
                <Item>
                    <Select
                        className="tag-question-selector"
                        labelId="tag-question-selector"
                        defaultValue={"Array"}
                        label="Tag"
                        value={tag}
                        onChange={onTagChange}
                    >
                        {
                            tagOptions.map((t) => {
                                return (
                                    <MenuItem value={t.value}>{t.label}</MenuItem>
                                )
                            })
                        }
                    </Select>
                </Item>
                <Item>
                    <ListView data={qs}></ListView>
                </Item>
            </Stack>
        </Container>
    )
}

export default TagQuestions