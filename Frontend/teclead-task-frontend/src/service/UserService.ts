import axios from "axios";

export interface User {
  id: number;
  name: string;
  vorname: string;
  email: string;
} 

const API_URL = "http://localhost:8080/api/v1";

class UserService {
  getUsers() {
    return axios.get(API_URL + "/users");
  }

  getUserById(id: number) {
    return axios.get(API_URL + "/user/" + id);
  }

  getUsersPaginated(url: string){
    return axios.get(url);
  }

  deleteUser(id: number){
    axios.delete(API_URL + "/user/"+id);
  }
}

export default new UserService();
