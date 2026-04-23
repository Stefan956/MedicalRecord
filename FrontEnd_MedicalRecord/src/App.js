import React from 'react';
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import Home from './Components/home.js';
import ViewDoctors from './Components/view_doctors.js';
import ViewPatients from './Components/view_patients.js';
import ViewSpecialization from './Components/view_specializations.js';
import ViewAppointments from './Components/view_appointments.js';
import CreateDoctor from './Components/create_doctor.js';
import CreatePatient from './Components/create_patient.js';
import CreateSpecialization from './Components/create_specialization.js';
import CreateAppointment from './Components/create_appointment.js';
import UpdateAppointment from './Components/update_appointment.js';
import PatientAppointments from './Components/patient_appointments.js';
import DoctorAppointments from './Components/doctor_appointments.js';

function App() {
  return (
    <div className="App">
      <Router>
        <Switch>
          <Route exact component={Home} path="/" />
          <Route component={ViewDoctors} path="/doctors" />
          <Route component={ViewPatients} path="/patients" />
          <Route component={ViewSpecialization} path="/specializations" />
          <Route component={ViewAppointments} path="/appointments" />
          <Route component={CreateSpecialization} path="/create-specialization" />
          <Route component={CreateDoctor} path="/create-doctor" />
          <Route component={CreatePatient} path="/create-patient" />
          <Route component={CreateAppointment} path="/create-appointment" />
          <Route component={UpdateAppointment} path="/update-appointment/:id" />
          <Route component={PatientAppointments} path="/patient-appointments/:id" />
          <Route component={DoctorAppointments} path="/doctor-appointments/:id" />
        </Switch>
      </Router>
    </div>
  );
}

export default App;
