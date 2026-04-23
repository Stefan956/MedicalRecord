import React from 'react';
import axios from 'axios';
import Menu from './menu';

class ViewAppointments extends React.Component {
    state = {
        appointments: [],
        message: ''
    }

    componentDidMount() {
        axios.get('http://localhost:8080/api/appointment/list')
            .then(response => this.setState({ appointments: response.data }));
    }

    handleDelete = id => {
        axios.delete('http://localhost:8080/api/appointment/delete/' + id)
            .then(() => {
                this.setState(prev => ({
                    appointments: prev.appointments.filter(a => a.id !== id),
                    message: 'Deleted Successfully'
                }));
                setTimeout(() => this.setState({ message: '' }), 3000);
            });
    }

    handleEdit = id => {
        this.props.history.push('/update-appointment/' + id);
    }

    render() {
        const { appointments, message } = this.state;
        return (
            <div>
                <Menu />
                {message && <p style={{ color: 'green', fontWeight: 'bold', padding: '8px' }}>{message}</p>}
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Date</th>
                            <th>Patient</th>
                            <th>Doctor</th>
                            <th>Illness</th>
                            <th>Medicine</th>
                            <th>Sick Leave Start</th>
                            <th>Sick Leave End</th>
                            <th>Edit</th>
                            <th>Delete</th>
                        </tr>
                    </thead>
                    <tbody>
                        {appointments.map(a => (
                            <tr key={a.id}>
                                <td>{a.id}</td>
                                <td>{a.appointment_date}</td>
                                <td>{a.patient ? a.patient.first_name + ' ' + a.patient.last_name : '-'}</td>
                                <td>{a.doctor ? a.doctor.first_name + ' ' + a.doctor.last_name : '-'}</td>
                                <td>{a.illness}</td>
                                <td>{a.medicine}</td>
                                <td>{a.sick_leave_start_date}</td>
                                <td>{a.sick_leave_end_date}</td>
                                <td><button onClick={() => this.handleEdit(a.id)}>Edit</button></td>
                                <td><button onClick={() => this.handleDelete(a.id)}>Delete</button></td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        );
    }
}

export default ViewAppointments;
