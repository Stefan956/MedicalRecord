import React from 'react';
import axios from 'axios';
import Menu from './menu';
import styles from './styles.css';


class Doctor extends React.Component{
    state = {
        doctors: [],

    }
    componentDidMount(){
        const url = "http://localhost:8080/api/doctor/list";
        axios.get(url)
            .then(response=>{
                this.setState({doctors:response.data})
            })
    }
    componentDidUpdate(){
        const url = "http://localhost:8080/api/doctor/list";
        axios.get(url)
            .then(response=>{
                this.setState({doctors:response.data})
            })
    }
    handleDeleteAll = () => {
        const url = "http://http://localhost:8080/doctors";
        axios.delete(url)
            .then(response=>{
                console.log("All items were deleted succesfully !");
            })
    }
    handleDeleteById = id => {
        const url = "http://localhost:8080/api/doctor/delete/" + id;
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
                        <th>First name</th>
                        <th>Delete</th>
                    </tr>
                    {this.state.doctors.map(doctor =>
                        <tr key={doctor.id}>
                            <td> {doctor.id} </td>
                            <td> {doctor.first_name} </td>
                            <td> {doctor.last_name} </td>
                            <td>
                                <button onClick={() => this.handleDeleteById(doctor.id)}>Delete</button>
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

export default Doctor;