import UserListDetails from "./UserListDetails";
import Pagination from "./Pagination";
import UseUsers from "../hooks/UseUsers.ts";
import { CanceledError } from "axios";
import UserService,  { User } from "../service/UserService";


function UserList() {
  
  const {users, error, isLoading, page, setUsers, setPage, setError} = UseUsers();

  const deleteUser = (user: User): void => {
    UserService.deleteUser(user.id);
    const response = UserService.getUsers();
    response
    .then((res) => {
      console.log(res);
      setUsers(res.data._embedded.Users);
      setPage(res.data.page);
    })
    .catch((err) => {
      if (err instanceof CanceledError) return;
      setError(err.message);
    });
  }
 
  return (
    <>
      {error && <p className="text-danger">{error}</p>}
      {isLoading && <div className="spinner-border"></div>}
      <div className="table-responsive">
        <table className="table table-striped">
          <thead className="table-dark">
            <tr>
              <th scope="col">ID</th>
              <th scope="col">Name</th>
              <th scope="col">Vorname</th>
              <th scope="col">EMail</th>
              <th></th>
              <th></th>
            </tr>
          </thead>
          <tbody className="table-group-divider">
            {users.map((user) => (
              <UserListDetails user={user} deleteUser={deleteUser} />
            ))}
          </tbody>
        </table>
      </div>
      {<Pagination page={page} />}
    </>
  );
}

export default UserList;
