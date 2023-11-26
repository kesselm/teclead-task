import { useEffect, useState } from "react";
import UserService, { User } from "../service/UserService";
import { CanceledError } from "axios";

export interface Page {
  size: number;
  totalElement: number;
  totalPage: number;
  number: number;
}

const UseUsers = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [error, setError] = useState("");
  const [page, setPage] = useState<Page>();

  const [isLoading, setLoading] = useState(false);

  useEffect(() => {
    const controller = new AbortController();
    setLoading(true);
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
    setLoading(false);
    return () => controller.abort();
  }, []);

  return { users, error, isLoading, page, setUsers, setPage, setError};
};

export default UseUsers;
