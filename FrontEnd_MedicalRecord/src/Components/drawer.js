import React from 'react';
import clsx from 'clsx';
import { makeStyles } from '@material-ui/core/styles';
import Drawer from '@material-ui/core/Drawer';
import Button from '@material-ui/core/Button';
import List from '@material-ui/core/List';
import Divider from '@material-ui/core/Divider';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import MenuIcon from '@material-ui/icons/Menu';

const useStyles = makeStyles({
    list: {
        width: 250,
    },
    fullList: {
        width: 'auto',
    },
});

export default function TemporaryDrawer() {
    const classes = useStyles();
    const [state, setState] = React.useState({
        top: true,
        left: false,
        bottom: false,
        right: false,
    });

    const toggleDrawer = (anchor, open) => (event) => {
        if (event.type === 'keydown' && (event.key === 'Tab' || event.key === 'Shift')) {
            return;
        }

        setState({ ...state, [anchor]: open });
    };

    const list = (anchor) => (
        <div
            className={clsx(classes.list, {
                [classes.fullList]: anchor === 'top' || anchor === 'bottom',
            })}
            role="presentation"
            onClick={toggleDrawer(anchor, false)}
            onKeyDown={toggleDrawer(anchor, false)}
        >
            <List>
                <ListItem>
                   <a href="/" class="link"><ListItemText primary="Home" /></a>
                </ListItem>
                <Divider />
                <ListItem>
                    <a href="/create-patient" class="link"><ListItemText primary="Create a patient" /></a>
                </ListItem>
                <Divider />
                <ListItem>
                    <a href="/patients" class="link"><ListItemText primary="View patients" /></a>
                </ListItem>
                <Divider />
                <ListItem>
                    <a href="/create-specialization" class="link"><ListItemText primary="Create a specialization" /></a>
                </ListItem>
                <Divider />
                <ListItem>
                    <a href="/specializations" class="link"><ListItemText primary="View specializations" /></a>
                </ListItem>
                <Divider />
                <ListItem>
                    <a href="/create-doctor" class="link"><ListItemText primary="Create a doctor" /></a>
                </ListItem>
                <Divider />
                <ListItem>
                    <a href="/doctors" class="link"><ListItemText primary="View doctors" /></a>
                </ListItem>
                <Divider />
            </List>
        </div>
    );

    return (
        <div>
            {['left'].map((anchor) => (
                <React.Fragment key={anchor}>
                    <Button onClick={toggleDrawer(anchor, true)}>
                        <MenuIcon style={{fill: "white"}} />
                    </Button>
                    <Drawer anchor={anchor} open={state[anchor]} onClose={toggleDrawer(anchor, false)}>
                        {list(anchor)}
                    </Drawer>
                </React.Fragment>
            ))}
        </div>
    );
}
