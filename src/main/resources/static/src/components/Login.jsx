import { useState } from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import { Register } from "./register";  // Register bileşenini ekliyoruz
import { AppIndex} from "../App_index";  // AppIndex bileşenini ekliyoruz

export const Login = () => {

    let [username, setUsername] = useState();
    let [password, setPassword] = useState();
    let [isLoginValid, setIsLoginValid] = useState();
    let [display, setDisplay] = useState("d-none");

    // Giriş, kayıt ve AppIndex görünümleri için state'ler
    const [showRegister, setShowRegister] = useState(false);
    const [loginVisible, setLoginVisible] = useState(true);
    const [showAppIndex, setShowAppIndex] = useState(false);  // AppIndex bileşeni için yeni state

    const handleUsername = (event) => {
        setUsername(event.target.value);
    }

    const handlePassword = (event) => {
        setPassword(event.target.value);
    }

    const handleLogin = (event) => {
        event.preventDefault();
        if (username === "Admin" && password === "12345") {
            setIsLoginValid(true);
            setLoginVisible(false);  // Giriş formunu gizle
            setShowAppIndex(true);   // AppIndex bileşenini göster
        } else {
            setIsLoginValid(false);
            setDisplay("d-block");
        }
    }

    const handleButtonClick = () => {
        setShowRegister(true);  // Kayıt formunu göster
        setLoginVisible(false); // Giriş formunu gizle
    }

    const handleReturnToLogin = () => {
        setShowRegister(false);  // Kayıt formunu gizle
        setLoginVisible(true);   // Giriş formunu göster
    };

    return (
        <div className="m-3">
            {/* Eğer loginVisible true ise giriş formu gösterilir */}
            {loginVisible && (
                <form className="form-control p-3" style={{ width: "25rem" }}>
                    <label htmlFor="username" className="form-label">Kullanıcı Adı</label>
                    <input type="text" id="username" className="form-control" onChange={handleUsername} />
                    <label htmlFor="password" className="form-label">Şifre</label>
                    <input type="password" id="password" className="form-control" onChange={handlePassword} />
                    <button className="btn btn-info mt-3 w-100" onClick={handleLogin}>Giriş Yap</button>
                    <button className="btn btn-info mt-3 w-100" type="button" onClick={handleButtonClick}>Kayıt Ol</button>
                    {isLoginValid === false && <p className={display}>Giriş Başarısız</p>}
                </form>
            )}

            {/* Eğer showRegister true ise kayıt formu gösterilir */}
            {showRegister && <Register returnToLogin={handleReturnToLogin} />}

            {/* Eğer showAppIndex true ise AppIndex bileşeni gösterilir */}
            {showAppIndex && <AppIndex />}
        </div>
    );
};
