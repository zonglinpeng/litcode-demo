import api from '../../../api/litcode'
import Chip from '@mui/material/Chip';
import { useEffect, useState } from "react";

// interface PopularTagProps {
//     data: Question[]
// }

const PopularTag = () => {
    const questionAPI = api.question()

    const [tag, setTag] = useState<string>("")

    useEffect(() => {
        (async () => {
            let t = await questionAPI.popularTag()
            console.log(t)
            setTag(t)
        })()
    }, [questionAPI])

    const a = (s: string) => { return `Most Popular Tag: ${s}` }
    return <Chip label={a(tag)} variant="outlined" />
}

export default PopularTag