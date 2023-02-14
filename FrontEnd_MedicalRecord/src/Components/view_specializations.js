import React from 'react';
import axios from 'axios';
import Menu from './menu';
import styles from './styles.css';


class Specialization extends React.Component{
    state = {
        specializations: [],

    }
    componentDidMount(){
        const url = "http://localhost:8080/api/specialization/list";
        axios.get(url)
            .then(response=>{
                this.setState({specializations:response.data})
            })
    }
    componentDidUpdate(){
        const url = "http://localhost:8080/api/specialization/list";
        axios.get(url)
            .then(response=>{
                this.setState({specializations:response.data})
            })
    }
    handleDeleteAll = () => {
        const url = "http://http://localhost:8080/specialization/delete/all";
        axios.delete(url)
            .then(response=>{
                console.log("All items were deleted succesfully !");
            })
    }
    handleDeleteById = id => {
        const url = "http://localhost:8080/api/specialization/delete/" + id;
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
                        <th>Name</th>
                        <th>Delete</th>
                    </tr>
                    {this.state.specializations.map(specialization =>
                        <tr key={specialization.id}>
                            <td> {specialization.id} </td>
                            <td> {specialization.name} </td>
                            <td>
                                <button onClick={() => this.handleDeleteById(specialization.id)}>Delete</button>
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

export default Specialization;