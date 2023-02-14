import React from 'react';
import axios from 'axios';
import Menu from './menu';
import styles from './styles.css';


class Patient extends React.Component{
    state = {
        patients: [],

    }
    componentDidMount(){
        const url = "http://localhost:8080/api/patient/list";
        axios.get(url)
            .then(response=>{
                this.setState({patients:response.data})
            })
    }
    componentDidUpdate(){
        const url = "http://localhost:8080/api/patient/list";
        axios.get(url)
            .then(response=>{
                this.setState({patients:response.data})
            })
    }
    handleDeleteAll = () => {
        const url = "http://http://localhost:8080/patients";
        axios.delete(url)
            .then(response=>{
                console.log("All items were deleted succesfully !");
            })
    }
    handleDeleteById = id => {
        const url = "http://localhost:8080/api/patient/delete/" + id;
        axios.delete(url)
            .then(response=>{
                console.log("Item no: " + id + " was deleted succesfully !");
            })
    }

    Table() {
        let table =
            <div alt="Table">
                <table>
                    <tr>
                        <th>ID</th>
                        <th>First name</th>
                        <th>Last name</th>
                        <th>Delete</th>
                    </tr>
                    {this.state.patients.map(patient =>
                        <tr key={patient.id}>
                            <td> {patient.id} </td>
                            <td> {patient.first_name} </td>
                            <td> {patient.last_name} </td>
                            <td>
                                <button onClick={() => this.handleDeleteById(patient.id)}>Delete</button>
                            </td>
                        </tr>
                    )}
                </table>
            </div>;
        return table;
    }


    render(){
        return (
            <div>
                <div alt="menu">
                    <Menu />
                </div>
                {this.Table()}
            </div>
        );
    }
}

export default Patient;