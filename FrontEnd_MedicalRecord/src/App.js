import React from 'react';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom";
import Home from './Components/home.js';
import ViewDoctors from './Components/view_doctors.js';
import ViewPatients from './Components/view_patients.js';
import ViewSpecialization from './Components/view_specializations.js';
import CreateDoctor from './Components/create_doctor.js';
import CreatePatient from './Components/create_patient.js';
import CreateSpecialization from './Components/create_specialization.js';

function App() {
  return (
    <div className="App">
      <Router>
        <Switch>
          <Route exact component={Home} path="/" />
          <Route component={ViewDoctors} path="/doctors" />
          <Route component={ViewPatients} path="/patients" />
          <Route component={ViewSpecialization} path="/specializations" />
          <Route component={CreateSpecialization} path="/create-specialization" />
          <Route component={CreateDoctor} path="/create-doctor" />
          <Route component={CreatePatient} path="/create-patient" />
        </Switch>
      </Router>
    </div>
  );
}

export default App;
