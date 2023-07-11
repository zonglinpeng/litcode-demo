import { Stack } from "@mui/material"

import Item from '../item'
import TextField from '@mui/material/TextField';
import ListView from "./listView";
import PopularTag from "./PopularTag";
import api from '../../api/litcode'
import { Container } from '@mui/material';
import './index.css'
import { Question } from "../../api/litcode/model/Question";
import { useEffect, useState } from "react";

const Questions = () => {
    const [qs, setQs] = useState<Question[]>([])
    const questionAPI = api.question()

    const onChange = async (e: any) => {
        let query = e.target.value
        let qs = await questionAPI.keyWordsSearch(query)
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
            <Stack className="questions-main" spacing={2}>
                <Item>
                    <PopularTag ></PopularTag>
                </Item>

                <Item>
                    <TextField id="search" fullWidth label="Search" variant="outlined" onChange={onChange} />
                </Item>
                <Item>
                    <ListView data={qs}></ListView>
                </Item>
            </Stack>
        </Container>
    )
}

export default Questions