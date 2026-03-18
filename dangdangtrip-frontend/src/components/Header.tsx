import { Link } from "react-router-dom";
import logo from "../assets/logo.png";

export default function Header() {
    return (
        <header style={{padding: "24px 0" }}>
            <div style={{ maxWidth: "1000px", margin: "0 auto", padding: "0 24px" }}>
                <Link to="/">
                  <img src={logo} alt="로고" style={{ height: "38px", cursor: "pointer" }} />
                </Link>
            </div>
        </header>
    );
}