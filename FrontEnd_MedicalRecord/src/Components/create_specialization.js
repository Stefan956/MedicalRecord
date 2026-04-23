import React from 'react';
import axios from 'axios';
import Menu from "./menu";
import Grid from '@material-ui/core/Grid';
import Button from '@material-ui/core/Button';
import styles from './styles.css';

class Create extends React.Component {
    state = {name:""}
    constructor(props) {
        super(props);
        this.state = {
            name: '',
            message: ''
        };
        this.handleChange = this.handleChange.bind(this);
    }

    handleChange (evt, field) {
        this.setState({ [field]: evt.target.value });
    }
    handleSubmit=()=>{
        const url = "http://localhost:8080/api/specialization/create";
        axios.post(url,{id:0, name: this.state.name})
            .then(response=>{
                this.setState({ name: '', message: 'Created Successfully' });
                setTimeout(() => this.setState({ message: '' }), 3000);
            })
            .catch(error=>{console.log(error)})
    }
    render(){
        return (
            <div>
                <div alt="menu">
                    <Menu />
                </div>
                <Grid item xs={6} className="grid">
                    <h2>Create a specialization</h2>
                    <p><input className="pillChange" value={this.state.name} placeholder="Enter name of specialization"
                              onChange={(event) => this.handleChange(event, "name")}></input></p>
                    {this.state.message && <p style={{color: 'green', fontWeight: 'bold'}}>{this.state.message}</p>}
                    <div className="submitButton">
                        <Button variant="contained" color="primary" onClick={this.handleSubmit}>
                            Submit
                        </Button></div>
                </Grid>
            </div>
        );
    }
}

export default Create;
