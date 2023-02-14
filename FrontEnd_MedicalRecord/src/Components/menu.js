import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import Drawer from './drawer';

const useStyles = makeStyles((theme) => ({
    root: {
        flexGrow: 1,
        transform: 'translate(-2%,-15%)',
        width: '120%'
    },
    menuButton: {
        marginRight: theme.spacing(2),
    },
    title: {
        flexGrow: 1,
    },
}));

export default function ButtonAppBar() {
    const classes = useStyles();
    return (
        <div className={classes.root}>
            <AppBar position="static" style={{backgroundColor: '#363d5d'}}>
                <Toolbar>
                    <Drawer />
                    <Typography variant="h6" className={classes.title}>
                        Medical Record
                    </Typography>
                </Toolbar>
            </AppBar>
        </div>
    );
}
