import React from 'react';
import Menu from './menu';
import styles from './styles.css';

class Home extends React.Component {

    render(){
        return (
            <div>
                <div alt="menu">
                    <Menu />
                </div>
                Medical record project for New Bulgarian University.
            </div>
        );
    }
}

export default Home;