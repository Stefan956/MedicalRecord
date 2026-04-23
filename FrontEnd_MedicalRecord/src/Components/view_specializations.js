import React from 'react';
import axios from 'axios';
import Menu from './menu';
import styles from './styles.css';


class Specialization extends React.Component{
    state = {
        specializations: [],
        message: ''
    }
    componentDidMount(){
        const url = "http://localhost:8080/api/specialization/list";
        axios.get(url)
            .then(response=>{
                this.setState({specializations:response.data})
            })
    }
    handleDeleteById = id => {
        const url = "http://localhost:8080/api/specialization/delete/" + id;
        axios.delete(url)
            .then(response=>{
                this.setState(prevState => ({
                    specializations: prevState.specializations.filter(s => s.id !== id),
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
                        <th>Name</th>
                        <th>Delete</th>
                    </tr>
                </thead>
                <tbody>
                    {this.state.specializations.map(specialization =>
                        <tr key={specialization.id}>
                            <td>{specialization.id}</td>
                            <td>{specialization.name}</td>
                            <td><button onClick={() => this.handleDeleteById(specialization.id)}>Delete</button></td>
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

export default Specialization;