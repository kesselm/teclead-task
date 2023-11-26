import { User } from "../service/UserService";


interface UserListDetailsProp {
  user: User;
  deleteUser: (arg: User) => void
}


const UserListDetails: React.FC<UserListDetailsProp>= ( { user, deleteUser }) => {
  const textLeftAlign = "text-start fw-normal";

  return (
    <>
      <tr>
        <th className={textLeftAlign}>
          {user.id}
        </th>
        <th className={textLeftAlign}>
          {user.name}
        </th>
        <th className={textLeftAlign}>
            {user.vorname}</th>
        <th className={textLeftAlign}>
            {user.email}</th>
        <th>
        <button className="btn btn-outline-danger" 
            onClick={() => deleteUser(user)}>Delete</button>
        </th>
        <th>
            <button>Edit</button>
        </th>
      </tr>
    </>
  );
}

export default UserListDetails;
