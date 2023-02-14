import React from 'react';
import axios from 'axios';
import Menu from "./menu";
import Grid from '@material-ui/core/Grid';
import Button from '@material-ui/core/Button';
import styles from './styles.css';

class Create extends React.Component {
    state = {first_name:"", last_name:"", egn:"",}
    constructor(props) {
        super(props);
        this.state = {
            first_name: '',
            last_name: '',
            egn: ''
        };
        this.handleChange = this.handleChange.bind(this);
    }

    handleChange (evt, field) {
        this.setState({ [field]: evt.target.value });
    }
    handleSubmit=()=>{
        const url = "http://localhost:8080/api/patient/create";
        axios.post(url,{id:0, first_name: this.state.first_name, last_name: this.state.last_name, egn: this.state.egn})
            .then(response=>{
                console.log("Item added");
            })
            .catch(error=>{console.log(error)})

    }
    render(){
        return (
            <div>
                <div alt="menu">
                    <Menu />
                </div>
                <Grid item xs={6} class="grid">
                    <h2>Create a patient</h2>
                    <p><input className="pillChange" value={this.state.first_name} placeholder="Enter first name"
                              onChange={(event) => this.handleChange(event, "first_name")}></input></p>
                    <p><input className="pillChange" value={this.state.last_name} placeholder="Enter last name"
                              onChange={(event) => this.handleChange(event, "last_name")}></input></p>
                    <p><input className="pillChange" value={this.state.egn} placeholder="Enter egn"
                              onChange={(event) => this.handleChange(event, "egn")}></input></p>
                    <div class="submitButton">
                        <Button variant="contained" color="primary" onClick={this.handleSubmit}>
                            Submit
                        </Button></div>
                </Grid>
            </div>
        );
    }
}

export default Create;
