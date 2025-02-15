import React, { useEffect, useState } from 'react';
import { useContext } from 'react';
import { AuthContext } from '../../Context/AuthContext';
import { useNavigate } from 'react-router-dom';
import Loading from '../../components/Common/Loading';

function Login() {

  const navigate = useNavigate();
  const { login, user } = useContext(AuthContext);

  const [form, setForm] = useState({ email: '', password: '' });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (user) {
      if (user.role === 'ADMIN') navigate('/admin');
      if (user.role === 'MEMBER') navigate('/');
    }
  }, [user,navigate]);

  // Handle form inputs
  const formHandler = (event) => {
    setForm((prev) => ({
      ...prev,
      [event.target.name]: event.target.value,
    }));
  };

  // Handle form submission
  const submitHandler = async (event) => {
    event.preventDefault();
    setLoading(true);
    setError(null); 
    try {
      await login(form);
      if ( user && user.role === 'ADMIN') navigate('/admin',{replace:true});
      if ( user && user.role === 'MEMBER') navigate('/member',{replace:true});
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <h2>Login</h2>
      
      {error && <p className="error-message">{error}</p>}

      <form onSubmit={submitHandler}>
        <div className="form-group">
          <label htmlFor="email">Email</label>
          <input
            type="email"
            id="email"
            name="email"
            value={form.email}
            onChange={formHandler}
            autoComplete='on'
            placeholder="Enter your email"
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="password">Password</label>
          <input
            type="password"
            id="password"
            name="password"
            value={form.password}
            onChange={formHandler}
            autoComplete="current-password"
            placeholder="Enter your password"
            required
          />
        </div>
        { loading ?(<Loading/>) :(<button type="submit" >Submit</button> )}
      </form>
    </div>
  );
}

export default Login;
