import React from 'react';
import axios from 'axios';
import Menu from './menu';
import Button from '@material-ui/core/Button';
import Grid from '@material-ui/core/Grid';

class CreateAppointment extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            appointment_date: '',
            illness: '',
            medicine: '',
            sick_leave_start_date: '',
            sick_leave_end_date: '',
            patient_id: '',
            doctor_id: '',
            patients: [],
            doctors: [],
            message: ''
        };
        this.handleChange = this.handleChange.bind(this);
    }

    componentDidMount() {
        axios.get('http://localhost:8080/api/patient/list')
            .then(res => this.setState({ patients: res.data }));
        axios.get('http://localhost:8080/api/doctor/list')
            .then(res => this.setState({ doctors: res.data }));
    }

    handleChange(evt, field) {
        this.setState({ [field]: evt.target.value });
    }

    handleSubmit = () => {
        const { appointment_date, illness, medicine, sick_leave_start_date, sick_leave_end_date, patient_id, doctor_id } = this.state;
        axios.post('http://localhost:8080/api/appointment/create', {
            appointment_date,
            illness,
            medicine,
            sick_leave_start_date: sick_leave_start_date || null,
            sick_leave_end_date: sick_leave_end_date || null,
            patient_id: parseInt(patient_id),
            doctor_id: parseInt(doctor_id)
        })
        .then(() => {
            this.setState({
                appointment_date: '', illness: '', medicine: '',
                sick_leave_start_date: '', sick_leave_end_date: '',
                patient_id: '', doctor_id: '',
                message: 'Created Successfully'
            });
            setTimeout(() => this.setState({ message: '' }), 3000);
        })
        .catch(error => console.log(error));
    }

    render() {
        const { patients, doctors, message } = this.state;
        return (
            <div>
                <Menu />
                <Grid item xs={6} style={{ padding: '16px' }}>
                    <h2>Create an Appointment</h2>
                    <p>
                        <label>Patient: </label>
                        <select value={this.state.patient_id} onChange={e => this.handleChange(e, 'patient_id')}>
                            <option value="">Select a patient</option>
                            {patients.map(p => (
                                <option key={p.id} value={p.id}>{p.first_name} {p.last_name}</option>
                            ))}
                        </select>
                    </p>
                    <p>
                        <label>Doctor: </label>
                        <select value={this.state.doctor_id} onChange={e => this.handleChange(e, 'doctor_id')}>
                            <option value="">Select a doctor</option>
                            {doctors.map(d => (
                                <option key={d.id} value={d.id}>{d.first_name} {d.last_name}</option>
                            ))}
                        </select>
                    </p>
                    <p><label>Appointment Date: </label><input value={this.state.appointment_date} type="date"
                        onChange={e => this.handleChange(e, 'appointment_date')} /></p>
                    <p><label>Diagnose: </label><input value={this.state.illness} placeholder="Enter diagnose"
                        onChange={e => this.handleChange(e, 'illness')} /></p>
                    <p><label>Prescription: </label><input value={this.state.medicine} placeholder="Enter prescription"
                        onChange={e => this.handleChange(e, 'medicine')} /></p>
                    <p><label>Sick Leave Start Date: </label><input value={this.state.sick_leave_start_date} type="date"
                        onChange={e => this.handleChange(e, 'sick_leave_start_date')} /></p>
                    <p><label>Sick Leave End Date: </label><input value={this.state.sick_leave_end_date} type="date"
                        onChange={e => this.handleChange(e, 'sick_leave_end_date')} /></p>
                    {message && <p style={{ color: 'green', fontWeight: 'bold' }}>{message}</p>}
                    <Button variant="contained" color="primary" onClick={this.handleSubmit}>Submit</Button>
                </Grid>
            </div>
        );
    }
}

export default CreateAppointment;
