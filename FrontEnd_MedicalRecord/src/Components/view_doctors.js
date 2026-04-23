import React from 'react';
import axios from 'axios';
import Menu from './menu';
import styles from './styles.css';


class Doctor extends React.Component{
    state = {
        doctors: [],
        message: ''
    }
    componentDidMount(){
        const url = "http://localhost:8080/api/doctor/list";
        axios.get(url)
            .then(response=>{
                this.setState({doctors:response.data})
            })
    }
    handleDeleteById = id => {
        const url = "http://localhost:8080/api/doctor/delete/" + id;
        axios.delete(url)
            .then(response=>{
                this.setState(prevState => ({
                    doctors: prevState.doctors.filter(d => d.id !== id),
                    message: 'Deleted Successfully'
                }));
                setTimeout(() => this.setState({ message: '' }), 3000);
            })
    }

    Table() {
        return (
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>First name</th>
                        <th>Last name</th>
                        <th>Appointments</th>
                        <th>Delete</th>
                    </tr>
                </thead>
                <tbody>
                    {this.state.doctors.map(doctor =>
                        <tr key={doctor.id}>
                            <td>{doctor.id}</td>
                            <td>{doctor.first_name}</td>
                            <td>{doctor.last_name}</td>
                            <td><button onClick={() => this.props.history.push('/doctor-appointments/' + doctor.id)}>Appointments</button></td>
                            <td><button onClick={() => this.handleDeleteById(doctor.id)}>Delete</button></td>
                        </tr>
                    )}
                </tbody>
            </table>
        );
    }


    render(){
        return (
            <div>
                <div alt="menu">
                    <Menu />
                </div>
                {this.state.message && <p style={{color: 'green', fontWeight: 'bold', padding: '8px'}}>{this.state.message}</p>}
                {this.Table()}
            </div>
        );
    }
}

export default Doctor;