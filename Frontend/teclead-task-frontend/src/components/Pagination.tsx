import { Page } from "./UserList";


function Pagination(page: Page) {

  console.log("PAGE");
  console.log(page);

  const first = 0;
  const last = page.totalElement-1;
  const current = page.number-1
  const prev = current-1;
  const next = current+1;



  return (
    <>
      <ul className="pagination">
        <li className="page-item">
          <a className="page-link" href="#" aria-label="Previous">
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
        <li className="page-item">
          <a className="page-link" href="#">
            {prev}
          </a>
        </li>
        <li className="page-item">
          <a className="page-link" href="#">
            {current}
          </a>
        </li>
        <li className="page-item">
          <a className="page-link" href="#">
            {next}
          </a>
        </li>
        <li className="page-item">
          <a className="page-link" href="#" aria-label="Next">
            <span aria-hidden="true">&raquo;</span>
          </a>
        </li>
      </ul>
    </>
  );
}

export default Pagination;
