import { useState } from "react";
import 'bootstrap/dist/css/bootstrap.min.css';

export const Register = ({ returnToLogin }) => {

    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const handleRegister = (event) => {
        event.preventDefault();
        // Burada kayıt işlemleri yapılabilir, örneğin API'ye istek gönderme.
        alert(`Kayıt başarılı! Hoş geldiniz, ${firstName} ${lastName}`);
    }

    return (
        <div className="m-3">
            <form className="form-control p-3" style={{ width: "25rem" }} onSubmit={handleRegister}>
                <label htmlFor="firstName" className="form-label">Ad</label>
                <input
                    type="text"
                    id="firstName"
                    className="form-control"
                    value={firstName}
                    onChange={(e) => setFirstName(e.target.value)}
                />
                <label htmlFor="lastName" className="form-label">Soyad</label>
                <input
                    type="text"
                    id="lastName"
                    className="form-control"
                    value={lastName}
                    onChange={(e) => setLastName(e.target.value)}
                />
                <label htmlFor="email" className="form-label">E-posta</label>
                <input
                    type="email"
                    id="email"
                    className="form-control"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
                <label htmlFor="password" className="form-label">Şifre</label>
                <input
                    type="password"
                    id="password"
                    className="form-control"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <button className="btn btn-success mt-3 w-100" type="submit">Kayıt Ol</button>

                {/* Bu butona tıklanınca kullanıcı Login bileşenine döner */}
                <button 
                    className="btn btn-success mt-3 w-100" 
                    type="button"
                    onClick={returnToLogin}
                >
                    Zaten bir hesabınız var mı?
                </button>
            </form>
        </div>
    );
};
