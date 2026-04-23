import React from 'react';
import axios from 'axios';
import Menu from './menu';
import styles from './styles.css';


class Patient extends React.Component{
    state = {
        patients: [],
        message: ''
    }
    componentDidMount(){
        const url = "http://localhost:8080/api/patient/list";
        axios.get(url)
            .then(response=>{
                this.setState({patients:response.data})
            })
    }
    handleDeleteById = id => {
        const url = "http://localhost:8080/api/patient/delete/" + id;
        axios.delete(url)
            .then(response=>{
                this.setState(prevState => ({
                    patients: prevState.patients.filter(p => p.id !== id),
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
                        <th>History</th>
                        <th>Delete</th>
                    </tr>
                </thead>
                <tbody>
                    {this.state.patients.map(patient =>
                        <tr key={patient.id}>
                            <td>{patient.id}</td>
                            <td>{patient.first_name}</td>
                            <td>{patient.last_name}</td>
                            <td><button onClick={() => this.props.history.push('/patient-appointments/' + patient.id)}>History</button></td>
                            <td><button onClick={() => this.handleDeleteById(patient.id)}>Delete</button></td>
                        </tr>
                    )}
                </tbody>
            </table>
        );
    }


    render(){
        return (
            <div>
                <div>
                    <Menu />
                </div>
                {this.state.message && <p style={{color: 'green', fontWeight: 'bold', padding: '8px'}}>{this.state.message}</p>}
                {this.Table()}
            </div>
        );
    }
}

export default Patient;