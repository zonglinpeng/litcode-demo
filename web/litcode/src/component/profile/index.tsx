import ListView from "./listView"
import api from '../../api/litcode'
import { useEffect, useState } from "react";
import { Question } from "../../api/litcode/model/Question";
import './index.css'
import Item from '../item'
import { Stack } from "@mui/material"
import { Container } from '@mui/material';
import Button from '@mui/material/Button';
import { useNavigate } from 'react-router-dom';

const Profile = () => {
    const [qs, setQs] = useState<Question[]>([])
    const questionAPI = api.question()
    let navigate = useNavigate()

    useEffect(() => {
        (async () => {
            let qs = await questionAPI.search(0, 50)
            console.log(qs)
            setQs(qs)
        })()
    }, [questionAPI])

    const onCreateClick = () => {
        navigate(`/qeditor/new`)
    }

    return (
        <Container >
            <Stack className="profile-main" spacing={2}>
                <Item>
                    <Button variant="contained" size="large" className="create-question-button" onClick={onCreateClick} >Create Question</Button>
                </Item>
                <Item>
                    <ListView data={qs}></ListView>
                </Item>
            </Stack>
        </Container>
    )
}

export default Profile