import React from 'react';
import axios from 'axios';
import Menu from './menu';

class PatientAppointments extends React.Component {
    state = {
        appointments: [],
        patientName: ''
    }

    componentDidMount() {
        const id = this.props.match.params.id;
        axios.get('http://localhost:8080/api/patient/id/' + id)
            .then(res => this.setState({ patientName: res.data.first_name + ' ' + res.data.last_name }));
        axios.get('http://localhost:8080/api/patient/' + id + '/history')
            .then(res => this.setState({ appointments: res.data }));
    }

    render() {
        const { appointments, patientName } = this.state;
        return (
            <div>
                <Menu />
                <h2 style={{ padding: '8px' }}>Appointment history for {patientName}</h2>
                {appointments.length === 0
                    ? <p style={{ padding: '8px' }}>No appointments found.</p>
                    : <table>
                        <thead>
                            <tr>
                                <th>Date</th>
                                <th>Doctor</th>
                                <th>Diagnose</th>
                                <th>Prescription</th>
                                <th>Sick Leave Start</th>
                                <th>Sick Leave End</th>
                            </tr>
                        </thead>
                        <tbody>
                            {appointments.map(a => (
                                <tr key={a.id}>
                                    <td>{a.appointment_date}</td>
                                    <td>{a.doctor ? a.doctor.first_name + ' ' + a.doctor.last_name : '-'}</td>
                                    <td>{a.illness}</td>
                                    <td>{a.medicine}</td>
                                    <td>{a.sick_leave_start_date || '-'}</td>
                                    <td>{a.sick_leave_end_date || '-'}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                }
                <br />
                <button onClick={() => this.props.history.push('/patients')}>Back to Patients</button>
            </div>
        );
    }
}

export default PatientAppointments;
