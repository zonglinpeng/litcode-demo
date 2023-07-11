import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import './index.css'
import { Link } from "react-router-dom";
import Avatar from '@mui/material/Avatar';
import api from '../../api/litcode';
import { useEffect, useState } from 'react';
import { UserProfile } from '../../api/litcode/model/UserProfile';

export default function Header() {
    const userAPI = api.user()
    const [profile, setProfile] = useState<UserProfile | null>(null)

    useEffect(() => {
        (
            async () => {
                const rst = await userAPI.profile()
                setProfile(rst)
            }
        )()
    }, [userAPI])

    return (
        <Box id="header" >
            <AppBar className="appbar" position="static">
                <Toolbar>
                    <Button id="home" color="inherit" component={Link} to={'/'} >
                        <Typography id="home" variant="h6" color="inherit" component="div" >
                            LITCODE
                        </Typography>
                    </Button>
                    <Box sx={{ flexGrow: 1 }}></Box>
                    <Button color="inherit" component={Link} to={'/questions'}>Questions</Button>
                    <Button color="inherit" component={Link} to={'/profile'}>Profile</Button>
                    <Button color="inherit" component={Link} to={'/tag-questions'}>Tag</Button>

                    {
                        profile !== null && (
                            <Avatar className='user-avatar' src={profile.picture} sx={{ width: 35, height: 35 }}></Avatar>
                        )
                    }
                </Toolbar>
            </AppBar>
        </Box>
    );
}
